package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Security {
    //TODO Security checks (e.g, checksum check on the .jar file using jar signing)
    //We can implement this whenever, preferably right before release as jar signing complicates the build process
    //For other security features, we can obfuscate the jar file, ban commonly used ports, etc.

    public Security(){
        File thisJar = new File(Security.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            byte[] data = Files.readAllBytes(Path.of(thisJar.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println("Failed to read the bytes of " + thisJar.getName());
            throw new RuntimeException(e);
        }

    }
}
