package network;

import app.Config;
import utils.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Listens for incoming messages from {@link Sender} devices using a 5-phase protocol:
 * <ol>
 *   <li>Receive a handshake XML; validate the sender against config black/whitelists</li>
 *   <li>Generate two large primes, compute their product (PUBNUM), and send it back</li>
 *   <li>Receive ROOT1 and ROOT2 from the sender; verify ROOT1 × ROOT2 == PUBNUM</li>
 *   <li>Send {@link Sender#SHAKE} to unlock message transmission</li>
 *   <li>Receive hash-chained packets, verify each hash, and reassemble the message</li>
 * </ol>
 *
 * <p>Sessions are tracked per sender IP so multiple senders can be handled concurrently
 * on the same listener thread.</p>
 *
 * @see Sender
 */
public class Receiver {

    /** Number of digits in each prime used to build PUBNUM. */
    private static final int PRIME_DIGITS = 6;

    // ── Session bookkeeping ──────────────────────────────────────────────────

    /** Lifecycle of a single sender session. */
    private enum SessionState {
        /** PUBNUM sent; waiting for the sender to return its prime roots. */
        AWAITING_ROOTS,
        /** Roots verified and SHAKE sent; waiting for binary message packets. */
        AWAITING_PACKETS
    }

    /**
     * All per-sender state needed to carry a session through all 5 phases.
     */
    private static class ReceiverSession {
        SessionState state;

        // Phase 2 – challenge
        BigInteger factor1;
        BigInteger factor2;
        BigInteger pubNum;

        // Phase 5 – packet reassembly
        int totalPackets = -1;
        final Map<Integer, String> chunks = new HashMap<>();
        /** Hash of the last validated packet; used to verify the chain. */
        byte[] lastValidatedHash = new byte[32]; // all zeros before first packet

        ReceiverSession(BigInteger factor1, BigInteger factor2) {
            this.factor1 = factor1;
            this.factor2 = factor2;
            this.pubNum  = factor1.multiply(factor2);
            this.state   = SessionState.AWAITING_ROOTS;
        }
    }

    private volatile boolean open = false;
    private Config conf;
    private Thread serverThread;

    /** Active sessions keyed by sender IP string. */
    private final ConcurrentHashMap<String, ReceiverSession> sessions = new ConcurrentHashMap<>();

    public Receiver(Config conf) {
        this.conf = conf;
    }

    // -------------------------------------------------------------------------
    // Server lifecycle
    // -------------------------------------------------------------------------

    /**
     * Reads the configuration and creates a thread to manage message listening.
     * <p>
     * The thread can only be stopped or restarted using the provided methods.
     *
     * @return a configured, ready-to-start server thread
     * @throws FileNotFoundException  if the config file cannot be found
     * @throws UnknownHostException   if localhost cannot be resolved
     */
    private Thread serverFactory() throws FileNotFoundException, UnknownHostException {
        conf.readConfig();

        return new Thread(() -> {
            DatagramSocket socket;
            try {
                socket = new DatagramSocket(conf.getPort());
            } catch (SocketException e) {
                Logging.log("Failed to initialise server socket", Level.SEVERE, e);
                throw new RuntimeException(e);
            }

            byte[] buf = new byte[Constants.MAX_PACKET_BYTES.value()];

            while (!serverThread.isInterrupted()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet); // Blocks safely here until data actually arrives

                    // If the receiver is toggled off, discard incoming packets
                    if (!this.open) {
                        continue;
                    }

                    String senderIp   = packet.getAddress().getHostAddress();
                    int    senderPort = packet.getPort();
                    byte[] data       = Arrays.copyOf(packet.getData(), packet.getLength());

                    // Dispatch based on whether this looks like XML or a binary packet
                    if (isXml(data)) {
                        handleXmlMessage(socket, data, senderIp, senderPort, packet.getAddress());
                    } else {
                        handleBinaryPacket(data, senderIp);
                    }

                } catch (SocketException e) {
                    // Handle socket closure gracefully when thread is interrupted
                    if (serverThread.isInterrupted()) {
                        break;
                    }
                    Logging.log("Socket exception", Level.SEVERE, e);
                } catch (Exception e) {
                    Logging.log("Error processing received datagram", Level.SEVERE, e);
                }
            }

            socket.close();
        });
    }

    /** Creates and starts a new server
     * @see #serverFactory() */
    public void startServer() throws FileNotFoundException, UnknownHostException {
        serverThread = serverFactory();
        serverThread.start();
    }

    /** Stops the listener thread. */
    public void stopServer() {
        serverThread.interrupt();
    }

    // -------------------------------------------------------------------------
    // Message handling
    // -------------------------------------------------------------------------

    /**
     * Routes an XML datagram through the correct protocol phase.
     *
     * <ul>
     *   <li>No active session for this IP → Phase 1/2 (validate + send PUBNUM)</li>
     *   <li>Session in {@link SessionState#AWAITING_ROOTS} → Phase 3/4 (verify roots + SHAKE)</li>
     * </ul>
     */
    private void handleXmlMessage(DatagramSocket socket, byte[] data,
                                  String senderIp, int senderPort,
                                  InetAddress senderAddress) {
        try {
            String xml = new String(data, StandardCharsets.UTF_8);
            Document doc = parseXml(xml);

            if (!sessions.containsKey(senderIp)) {
                // ── Phase 1 → 2: Validate sender and issue PUBNUM challenge ──
                handleHandshake(socket, doc, xml, senderIp, senderPort, senderAddress);
            } else {
                ReceiverSession session = sessions.get(senderIp);
                if (session.state == SessionState.AWAITING_ROOTS) {
                    // ── Phase 3 → 4: Verify roots and send SHAKE ────────────────
                    handleRootsVerification(socket, doc, session, senderIp, senderPort, senderAddress);
                }
                // Any XML arriving during AWAITING_PACKETS is ignored
            }

        } catch (Exception e) {
            Logging.log("Error handling XML from " + senderIp, Level.SEVERE, e);
        }
    }

    /**
     * Phase 1 → 2: Validates the sender's handshake XML against the config
     * black/whitelists, then generates a PUBNUM challenge and sends it back.
     */
    private void handleHandshake(DatagramSocket socket, Document doc, String rawXml,
                                 String senderIp, int senderPort,
                                 InetAddress senderAddress) throws Exception {

        String senderMac = getField(doc, "MAC_ADDRESS");

        // ── Blacklist checks ──────────────────────────────────────────────────
        if (conf.getIpBlacklist().contains(senderIp)) {
            Logging.log("Rejected blacklisted IP: " + senderIp, Level.WARNING);
            return;
        }
        if (conf.getMacBlacklist().contains(senderMac)) {
            Logging.log("Rejected blacklisted MAC: " + senderMac, Level.WARNING);
            return;
        }

        // ── Whitelist checks (only if whitelists are non-empty) ───────────────
        List<String> ipWhitelist  = conf.getIpWhitelist();
        List<String> macWhitelist = conf.getMacWhitelist();

        if (!ipWhitelist.isEmpty() && !ipWhitelist.contains(senderIp)) {
            Logging.log("Rejected non-whitelisted IP: " + senderIp, Level.WARNING);
            return;
        }
        if (!macWhitelist.isEmpty() && !macWhitelist.contains(senderMac)) {
            Logging.log("Rejected non-whitelisted MAC: " + senderMac, Level.WARNING);
            return;
        }

        // ── Generate PUBNUM challenge ─────────────────────────────────────────
        BigInteger[] factors = Primes.generatePrimes(PRIME_DIGITS);
        ReceiverSession session = new ReceiverSession(factors[0], factors[1]);
        sessions.put(senderIp, session);

        byte[] rawXmlHash = NetworkUtils.hashString(rawXml, "SHA-256");
        String responseXml = buildReceiverXml(session.pubNum, rawXmlHash, new byte[32]);
        sendTo(socket, responseXml.getBytes(StandardCharsets.UTF_8), senderAddress, senderPort);

        NetworkUtils.GetTimestamp("[Receiver] PUBNUM challenge sent to " + senderIp + " at ");
    }

    /**
     * Phase 3 → 4: Checks that ROOT1 × ROOT2 equals this session's PUBNUM.
     * Sends {@link Sender#SHAKE} on success, or removes the session on failure.
     */
    private void handleRootsVerification(DatagramSocket socket, Document doc,
                                         ReceiverSession session,
                                         String senderIp, int senderPort,
                                         InetAddress senderAddress) throws Exception {

        String root1Str = getField(doc, "ROOT1");
        String root2Str = getField(doc, "ROOT2");

        if (root1Str.isEmpty() || root2Str.isEmpty()) {
            Logging.log("Received empty roots from " + senderIp, Level.WARNING);
            return;
        }

        BigInteger root1 = new BigInteger(root1Str);
        BigInteger root2 = new BigInteger(root2Str);

        if (!root1.multiply(root2).equals(session.pubNum)) {
            Logging.log("Root verification FAILED for " + senderIp
                    + " – removing session", Level.WARNING);
            sessions.remove(senderIp);
            return;
        }

        // Roots are correct → advance session and send SHAKE
        session.state = SessionState.AWAITING_PACKETS;
        sendTo(socket, Sender.SHAKE.getBytes(StandardCharsets.UTF_8), senderAddress, senderPort);
        NetworkUtils.GetTimestamp("[Receiver] SHAKE sent to " + senderIp + " at ");
    }

    /**
     * Phase 5: Parses an incoming binary packet, verifies its hash against the chain,
     * stores its content, and reassembles the full message when all packets arrive.
     */
    private void handleBinaryPacket(byte[] data, String senderIp) {
        ReceiverSession session = sessions.get(senderIp);
        if (session == null || session.state != SessionState.AWAITING_PACKETS) {
            Logging.log("Binary packet from unknown/unverified sender " + senderIp
                    + " – dropped", Level.WARNING);
            return;
        }

        try {
            PacketHeader header = NetworkUtils.parseHeader(data);

            // ── Set expected total on first packet ────────────────────────────
            if (session.totalPackets == -1) {
                session.totalPackets = header.totalPackets();
            }

            // ── Verify hash chain ─────────────────────────────────────────────
            String expectedHashInput = NetworkUtils.SHA256ToHex(header.previous_hash())
                    + header.text()
                    + header.packetNum();
            byte[] expectedHash = NetworkUtils.hashString(expectedHashInput, "SHA-256");

            if (!Arrays.equals(expectedHash, header.hash())) {
                Logging.log("Hash mismatch on packet " + header.packetNum()
                        + " from " + senderIp + " – discarded", Level.WARNING);
                return;
            }

            // Verify this packet's previousHash matches the last validated hash
            if (header.packetNum() > 0
                    && !Arrays.equals(header.previous_hash(), session.lastValidatedHash)) {
                Logging.log("Previous-hash chain broken at packet " + header.packetNum()
                        + " from " + senderIp + " – discarded", Level.WARNING);
                return;
            }

            session.lastValidatedHash = header.hash();
            session.chunks.put(header.packetNum(), header.text());

            Logging.log("Received packet " + (header.packetNum() + 1)
                    + "/" + session.totalPackets + " from " + senderIp, Level.FINE);

            // ── Reassemble when all packets have arrived ───────────────────────
            if (session.chunks.size() == session.totalPackets) {
                reassembleMessage(senderIp, session);
            }

        } catch (Exception e) {
            Logging.log("Error parsing binary packet from " + senderIp, Level.SEVERE, e);
        }
    }

    /**
     * Joins all validated chunks in order and delivers the complete message.
     * Cleans up the session afterward.
     */
    private void reassembleMessage(String senderIp, ReceiverSession session) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < session.totalPackets; i++) {
            String chunk = session.chunks.get(i);
            if (chunk == null) {
                Logging.log("Missing packet " + i + " from " + senderIp
                        + " – cannot reassemble", Level.WARNING);
                return;
            }
            sb.append(chunk);
        }

        String fullMessage = sb.toString();
        NetworkUtils.GetTimestamp("[Receiver] Full message received from " + senderIp + " at ");
        System.out.println("[" + senderIp + "] " + fullMessage);

        // TODO: pass fullMessage to the UI/message handler layer
        sessions.remove(senderIp);
    }

    // XML helpers

    /**
     * Loads {@code reciever_template.xml} from the classpath and fills every field.
     *
     * @param pubNum       the semiprime challenge to embed
     * @param hash         SHA-256 hash of this XML
     * @param previousHash SHA-256 hash of the previous XML in the chain
     */
    private String buildReceiverXml(BigInteger pubNum,
                                    byte[] hash,
                                    byte[] previousHash) throws Exception {
        String template = loadTemplate("templates/reciever_template.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(
                new ByteArrayInputStream(template.getBytes(StandardCharsets.UTF_8)));

        setField(doc, "HASH",          NetworkUtils.SHA256ToHex(hash));
        setField(doc, "PREVIOUS_HASH", NetworkUtils.SHA256ToHex(previousHash));
        setField(doc, "PUBNUM",        pubNum.toString());
        setField(doc, "USERNAME",      conf.getUsername());

        return docToString(doc);
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    private String getField(Document doc, String tag) {
        NodeList nodes = doc.getElementsByTagName(tag);
        return (nodes.getLength() > 0) ? nodes.item(0).getTextContent().trim() : "";
    }

    private void setField(Document doc, String tag, String value) {
        NodeList nodes = doc.getElementsByTagName(tag);
        if (nodes.getLength() > 0) nodes.item(0).setTextContent(value);
    }

    private String loadTemplate(String name) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(name)) {
            if (is == null) throw new FileNotFoundException("Template not found on classpath: " + name);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String docToString(Document doc) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    // Helpers
    /** Returns {@code true} if {@code data} starts with the UTF-8 bytes of {@code "<?xml"}. */
    private boolean isXml(byte[] data) {
        if (data.length < 5) return false;
        String prefix = new String(data, 0, Math.min(data.length, 5), StandardCharsets.UTF_8);
        return prefix.startsWith("<?xml");
    }

    private void sendTo(DatagramSocket socket, byte[] data,
                        InetAddress address, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }

    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public Config getConf() { return conf; }
    public void setConf(Config conf) { this.conf = conf; }
    public Thread getServerThread() { return serverThread; }
}