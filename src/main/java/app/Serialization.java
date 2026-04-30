package app;

import utils.Logging;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.logging.Level;

public class Serialization {
    private static File getConfigDir() {
        String os = System.getProperty("os.name").toLowerCase(),
                home = System.getProperty("user.home");
        Path path;
        
        if (os.contains("win"))
            path = Path.of(System.getenv("LOCALAPPDATA"), "Hadrian");
        else if (os.contains("mac"))
            path = Path.of(home, "Library", "Application Support", "Hadrian");
        else
            path = Path.of(home, ".config", "Hadrian");
        
        File folder = new File(path.toUri());
        
        if (!folder.exists()) {
            String message = "Failed To Locate Config Folder";
            Logging.log(message, Level.SEVERE);
            throw new RuntimeException(message);
        }
        
        return folder;
    }
    
    public static <T extends Serializable> void serialize(T data, String filename) {
        Path path = getConfigDir().toPath().resolve(SHA256Hash(filename) + ".hadrian");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            out.writeObject(data);
        } catch (IOException e) {
            Logging.log("Failed To Serialize Object:" + filename, Level.SEVERE, e);
            throw new RuntimeException(e);
        }
    }
    
    public static <T extends Serializable> HashSet<T> deserialize() {
        HashSet<T> files = new HashSet<>();
        for (File file : getConfigDir().listFiles()) {
            if (!file.exists()) {
                String message = "Failed To Deserialize File:";
                Logging.log(message + file.getName(), Level.SEVERE);
                throw new RuntimeException(message);
            }
            
            String extension = file.getName().substring(file.getName().lastIndexOf('.'));
            if (!extension.equals(".hadrian"))
                continue;
            
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                files.add((T) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                Logging.log("Failed To Deserialize Object:" + file.getName(), Level.SEVERE, e);
                throw new RuntimeException(e);
            }
        }
        
        return files;
    }
    
    private static String SHA256Hash(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
