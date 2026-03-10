package utils;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Enumeration;
import java.sql.Timestamp;
import java.security.Security;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class NetworkUtils {


    public static void GetTimestamp(String info){
        System.out.println(info + new Timestamp((new Date()).getTime()));
    }

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

    // Encryption

    public static AsymmetricCipherKeyPair generateKeys() throws NoSuchAlgorithmException{
    // Source: http://stackoverflow.com/questions/3087049/bouncy-castle-rsa-keypair-generation-using-lightweight-api
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new RSAKeyGenerationParameters
                (
                        new BigInteger("10001", 16), // publicExponent
                        SecureRandom.getInstance("DRBG"),
                        4096, // strength
                        255 // certainty, change to 100 if too slow
                ));

        return generator.generateKeyPair();
    }

    public static byte[] encrypt(byte[] data, AsymmetricKeyParameter publicKey){
        Security.addProvider(new BouncyCastleProvider());

        RSAEngine engine = new RSAEngine();
        engine.init(true, publicKey); // true if encrypting

        byte[] encryptedData = engine.processBlock(data, 0, data.length);

        return encryptedData;
    }

    public static byte[] decrypt(byte[] encrypted, AsymmetricKeyParameter privateKey) throws InvalidCipherTextException {
        Security.addProvider(new BouncyCastleProvider());

        AsymmetricBlockCipher engine = new RSAEngine();
        engine.init(false, privateKey); // false for decryption

        byte[] decrypted = engine.processBlock(encrypted, 0, encrypted.length);

        return decrypted;
    }

    // Packets

    /**
     * Returns a UDP packet designed for being a part of a chain of packets
     * @see #hashString(String, String)
     * @return
     */
    public static byte[] packetFactory(PacketHeader packetHeader){
        byte[] content = packetHeader.text().getBytes(StandardCharsets.UTF_8);
        // 4 = int size, 32 = SHA-256 size
        int size = 32 // hash
                + 32 // previousHash
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
     * @param dataArray Byte array. Used for decrypted packets
     * @return PacketHeader record
     */
    public static PacketHeader parseHeader(byte[] dataArray){
        ByteBuffer buffer = ByteBuffer.wrap(dataArray);

        // SHA-256 hashes
        byte[] hash = new byte[32];
        buffer.get(hash);

        byte[] previousHash = new byte[32];
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