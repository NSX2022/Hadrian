package utils;

import java.io.IOException;
import java.util.logging.*;

/**
 * Logger utility for Hadrian application.
 * <p>
 * Initializes static logger for logging messages to
 * a file with different severity levels.
 * <p>
 * Logger can be used by referencing {@code Logging.log()}, no initialization required.
 *
 * @see Logger
 * @see Logging#log(String, Level)
 */
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
    
    /**
     * Write logs to 'app.log' with timestamps, severity level, and message.
     *
     * @param message message to be displayed in log
     * @param level severity of message as java utility logging object
     * @see Logger
     * @see Level
     */
    public static void log(String message, Level level) {
        logger.log(level, message);
    }
}
