package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class NetworkUtils {


    /**
     * Returns the address of an active Wifi interface that isn't inactive and isn't a loopback interface (e.g, localhost)
     * @return {@code byte[]} Wifi interface address
     * @throws SocketException
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
     * @return hash bytes
     * @throws NoSuchAlgorithmException
     */
    public static byte[] hashString(String input, String hashType) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashType);
        byte[] hash = digest.digest(
                input.getBytes(StandardCharsets.UTF_8));

        return hash;
    }
}
