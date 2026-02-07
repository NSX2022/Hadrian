package utils;

import java.io.IOException;
import java.util.logging.*;

public class Logging {
    private static final Logger logger = Logger.getLogger(Logging.class.getName());
    
    static {
        try {
            logger.setUseParentHandlers(false);
            
            for (Handler h : logger.getHandlers())
                logger.removeHandler(h);
            
            FileHandler handler = new FileHandler("app.log", true);
            handler.setFormatter(new SimpleFormatter());
            
            logger.addHandler(handler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error setting up file logging", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void log(String message, Level level) {
        logger.log(level, message);
    }
}
