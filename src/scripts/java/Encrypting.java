import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import utils.NetworkUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class Encrypting {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidCipherTextException {
        AsymmetricCipherKeyPair keys = NetworkUtils.generateKeys();

        String toEncrypt = "This encrypted message is about the hoards of chickens the elites are sacrificing or something";

        byte[] encrypted = NetworkUtils.encrypt(toEncrypt.getBytes(), keys.getPublic());
        System.out.println();
        System.out.print("Encrypted message: ");
        System.out.flush();
        System.out.println(new String(encrypted, StandardCharsets.UTF_8));
        System.out.flush();

        byte[] decrypted = NetworkUtils.decrypt(encrypted, keys.getPrivate());
        System.out.println("Decrypted message: " + new String(decrypted, StandardCharsets.UTF_8));
    }
}
