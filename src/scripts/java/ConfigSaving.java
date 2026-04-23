import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigSaving {
    public static void main(String[] args) {
        Path configDir = getConfigDir("Hadrian");
        Path configPath = configDir.resolve("config.json");
        
        System.out.println(configDir);
        System.out.println(configPath);
        
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configDir);
                
                InputStream in = ConfigSaving.class.getClassLoader().getResourceAsStream("defaults.json");
                
                if (in == null)
                    throw new FileNotFoundException("default.json missing");
                
                Files.copy(in, configPath);
                
                System.out.println("Defaults Created");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static Path getConfigDir(String appName) {
        String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        
        if (os.contains("win")) {
            return Path.of(System.getenv("LOCALAPPDATA"), appName);
        } else if (os.contains("mac")) {
            return Path.of(home, "Library", "Application Support", appName);
        } else {
            return Path.of(home, ".config", appName);
        }
    }
}
