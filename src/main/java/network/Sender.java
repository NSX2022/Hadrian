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
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Sends messages to a {@code Receiver} on another device using a 5-phase handshake protocol:
 * <ol>
 *   <li>Send a handshake XML (sender_template.xml, no roots/message)</li>
 *   <li>Receive a challenge XML from the receiver containing a large semiprime (PUBNUM)</li>
 *   <li>Factor PUBNUM into its two prime roots and send them back</li>
 *   <li>Wait for an SHAKE confirming the roots are correct</li>
 *   <li>Split the message into hash-chained UDP packets and send them</li>
 * </ol>
 *
 * @see Receiver
 */
public class Sender {

    /** Milliseconds to wait for a response from the receiver before giving up. */
    private static final int SOCKET_TIMEOUT_MS = 15_000;

    /** SHAKE string the receiver sends after verifying the prime roots. */
    static final String SHAKE = "SHAKE";

    /**
     * Overhead (bytes) added by {@link NetworkUtils#packetFactory} to every packet:
     * 32 (hash) + 32 (previousHash) + 4 (packetNum) + 4 (totalPackets) + 4 (contentLength).
     */
    private static final int PACKET_HEADER_BYTES = 76;

    private Config conf;

    public Sender(Config conf) {
        this.conf = conf;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Executes the full 5-phase handshake and sends {@code userMessage} to {@code ip}.
     *
     * @param userMessage plaintext message to send
     * @param ip          IP address of the remote device running {@link Receiver}
     * @return {@code true} if the message was delivered and acknowledged
     */
    public boolean sendMessage(String userMessage, InetAddress ip) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(SOCKET_TIMEOUT_MS);

            // ── Phase 1: Handshake ──────────────────────────────────────────
            byte[] zeros = new byte[32];
            String handshakeXml = buildSenderXml("", "", "", zeros, zeros);
            sendRaw(socket, handshakeXml.getBytes(StandardCharsets.UTF_8), ip);
            NetworkUtils.GetTimestamp("[Sender] Handshake sent to " + ip + " at ");

            // ── Phase 2: Receive PUBNUM challenge ───────────────────────────
            byte[] buf = new byte[Constants.MAX_PACKET_BYTES.value()];
            DatagramPacket response = new DatagramPacket(buf, buf.length);
            socket.receive(response);

            String receiverXml = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
            BigInteger pubNum = parsePubNum(receiverXml);
            byte[] receiverXmlHash = NetworkUtils.hashString(receiverXml, "SHA-256");
            NetworkUtils.GetTimestamp("[Sender] PUBNUM received at ");

            // ── Phase 3: Factor PUBNUM and send roots ───────────────────────
            // NOTE: Primes.calcRoots uses random search and may be slow for large semiprimes.
            // For 5-digit primes the product is ~10 digits, which is tractable.
            // Consider trial division up to sqrt(pubNum) if performance is needed.
            BigInteger[] roots = Primes.calcRoots(pubNum);
            byte[] rootsXmlHashInput = NetworkUtils.hashString(
                    pubNum.toString() + roots[0] + roots[1], "SHA-256");
            String rootsXml = buildSenderXml(
                    roots[0].toString(), roots[1].toString(), "",
                    rootsXmlHashInput, receiverXmlHash);
            sendRaw(socket, rootsXml.getBytes(StandardCharsets.UTF_8), ip);
            NetworkUtils.GetTimestamp("[Sender] Prime roots sent at ");

            // ── Phase 4: Wait for SHAKE ───────────────────────────────────────
            socket.receive(response);
            String shake = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8).trim();
            if (!SHAKE.equals(shake)) {
                Logging.log("Authentication rejected by receiver (incorrect roots)", Level.WARNING);
                return false;
            }
            NetworkUtils.GetTimestamp("[Sender] SHAKE received at ");

            // ── Phase 5: Send hash-chained message packets ──────────────────
            sendPacketChain(socket, userMessage, ip);
            NetworkUtils.GetTimestamp("[Sender] All packets sent at ");
            return true;

        } catch (SocketTimeoutException e) {
            Logging.log("Timed out waiting for receiver", Level.SEVERE, e);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Logging.log("Failed to send message", Level.SEVERE, e);
            e.printStackTrace();
            return false;
        }
    }

    // Packet chain

    /**
     * Splits {@code message} into chunks that fit in {@link Constants#MAX_PACKET_BYTES},
     * wraps each chunk in a {@link PacketHeader} with a SHA-256 hash chain, and sends them.
     *
     * <p>Hash chaining: each packet's hash is {@code SHA-256(previousHash || chunkContent || packetNum)}.
     * The first packet's {@code previousHash} is an all-zero byte array.</p>
     */
    private void sendPacketChain(DatagramSocket socket, String message, InetAddress ip)
            throws IOException, NoSuchAlgorithmException {

        int maxContentBytes = Constants.MAX_PACKET_BYTES.value() - PACKET_HEADER_BYTES;
        byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
        int totalPackets = (int) Math.ceil((double) msgBytes.length / maxContentBytes);

        byte[] previousHash = new byte[32]; // all zeros for first packet

        for (int i = 0; i < totalPackets; i++) {
            int start = i * maxContentBytes;
            int end   = Math.min(start + maxContentBytes, msgBytes.length);
            String chunk = new String(msgBytes, start, end - start, StandardCharsets.UTF_8);

            // hash = SHA-256(previousHash_hex + chunk + packetNum)
            String hashInput = NetworkUtils.SHA256ToHex(previousHash) + chunk + i;
            byte[] hash = NetworkUtils.hashString(hashInput, "SHA-256");

            PacketHeader header = new PacketHeader(hash, previousHash, i, totalPackets, chunk);
            byte[] packetBytes = NetworkUtils.packetFactory(header);
            sendRaw(socket, packetBytes, ip);

            previousHash = hash;
            Logging.log("Sent packet " + (i + 1) + "/" + totalPackets, Level.FINE);
        }
    }

    // XML helpers

    /**
     * Loads {@code sender_template.xml} from the classpath and fills every field.
     *
     * @param root1        first prime factor (empty string during handshake)
     * @param root2        second prime factor (empty string during handshake)
     * @param message      message body (empty during handshake / roots phase)
     * @param hash         SHA-256 hash of this XML's content
     * @param previousHash SHA-256 hash of the previous XML in the chain
     */
    private String buildSenderXml(String root1, String root2, String message,
                                  byte[] hash, byte[] previousHash) throws Exception {
        String template = loadTemplate("templates/sender_template.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(template.getBytes(StandardCharsets.UTF_8)));

        String macHex;
        try {
            macHex = NetworkUtils.macBytesToHex(NetworkUtils.getMacAddress());
        } catch (Exception e) {
            macHex = "UNKNOWN";
        }

        setField(doc, "HASH",          NetworkUtils.SHA256ToHex(hash));
        setField(doc, "PREVIOUS_HASH", NetworkUtils.SHA256ToHex(previousHash));
        setField(doc, "USERNAME",      conf.getUsername());
        setField(doc, "MAC_ADDRESS",   macHex);
        setField(doc, "DEVICE_NAME",   InetAddress.getLocalHost().getHostName());
        setField(doc, "ROOT1",         root1);
        setField(doc, "ROOT2",         root2);
        setField(doc, "MESSAGE",       message);

        return docToString(doc);
    }

    /** Extracts the {@code <PUBNUM>} value from a receiver XML string. */
    private BigInteger parsePubNum(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        String value = doc.getElementsByTagName("PUBNUM").item(0).getTextContent().trim();
        return new BigInteger(value);
    }

    private String loadTemplate(String name) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(name)) {
            if (is == null) throw new FileNotFoundException("Template not found on classpath: " + name);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void setField(Document doc, String tag, String value) {
        NodeList nodes = doc.getElementsByTagName(tag);
        if (nodes.getLength() > 0) nodes.item(0).setTextContent(value);
    }

    private String docToString(Document doc) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    // Networking

    private void sendRaw(DatagramSocket socket, byte[] data, InetAddress ip) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, conf.getPort());
        socket.send(packet);
    }

    public Config getConf() { return conf; }
    public void setConf(Config conf) { this.conf = conf; }
}