package utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class NetworkUtils {


    /**
     * Returns the address of an active Wifi interface that isn't inactive and isn't a loopback interface (e.g, localhost)
     * @return {@code byte[]} Wifi interface address
     * @throws SocketException
     * @see #macBytesToHex(byte[])
     */
    public static byte[] getMacAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            // Skip loopback and interfaces that are down
            if (ni.isLoopback() || !ni.isUp()) {
                continue;
            }

            String name = ni.getName().toLowerCase();
            if (name.startsWith("wlan") || name.startsWith("wlp") ||
                    (System.getProperty("os.name").toLowerCase().contains("mac") && name.startsWith("en"))) {

                byte[] mac = ni.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    return mac;
                }
            }
        }

        throw new SocketException("No WiFi adapter found");
    }

    /**
     * Returns a {@code String} representation of a Hexadecimal MAC address
     * @param macAddress
     * @return {@code String} Hexadecimal
     * @see #getMacAddress()
     */
    public static String macBytesToHex(byte[] macAddress) {
        String[] hexadecimal = new String[macAddress.length];
        for (int i = 0; i < macAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", macAddress[i]);
        }
        return String.join("-", hexadecimal);
    }

    /**
     * Converts the bytes returned by {@code hash(input, "SHA-256")} to a Hexadecimal String
     * @param hash
     * @return Hexadecimal String
     */
    public static String SHA256ToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Returns the hash of a String {@code input}
     * @param input
     * @param hashType "SHA-256", "SHA-1", "MD5"
     * @return {@code byte[]} Hash bytes
     * @throws NoSuchAlgorithmException
     */
    public static byte[] hashString(String input, String hashType) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashType);
        byte[] hash = digest.digest(
                input.getBytes(StandardCharsets.UTF_8));

        return hash;
    }

    /**
     * Returns a UDP packet designed for being a part of a chain of packets
     * @see #hashString(String, String)
     * @return
     */
    public static byte[] packetFactory(PacketHeader packetHeader){
        byte[] content = packetHeader.text().getBytes(StandardCharsets.UTF_8);
        int size = 4 + packetHeader.hash().length
                + 4 + packetHeader.previous_hash().length
                + 4 // packetNum
                + 4 // totalPackets
                + 4 + content.length
                ;
        ByteBuffer builder = ByteBuffer.allocate(size);
        // I CAST: INVOCATION CHAIN
        builder.put(packetHeader.hash()).put(packetHeader.previous_hash()).putInt(packetHeader.packetNum())
                .putInt(packetHeader.totalPackets()).putInt(content.length).put(content);

        return builder.array();
    }

    /**
     *
     * @param dataArray
     * @return
     */
    public static PacketHeader parseHeader(byte[] dataArray){
        ByteBuffer buffer = ByteBuffer.wrap(dataArray);
        int hashLen = buffer.getInt();
        byte[] hash = new byte[hashLen];
        buffer.get(hash);

        // Don't need to specify index in getInt() because ByteBuffer advances automatically
        int previousHashLen = buffer.getInt();
        byte[] previousHash = new byte[previousHashLen];
        buffer.get(previousHash);

        int packetNum = buffer.getInt();

        int totalPackets = buffer.getInt();

        int contentLength = buffer.getInt();
        byte[] contentData = new byte[contentLength];
        buffer.get(contentData);
        String content = new String(contentData, StandardCharsets.UTF_8);

        return new PacketHeader(hash, previousHash, packetNum, totalPackets, content);
    }

}