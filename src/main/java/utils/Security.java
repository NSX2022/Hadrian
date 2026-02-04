package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Security {
    //TODO Security checks (e.g, checksum check on the .jar file using jar signing)
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
