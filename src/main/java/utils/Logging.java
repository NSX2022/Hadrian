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
 * @see #log(String, Level) Standard Logging Method
 * @see #log(String, Level, Exception) Exception Logging Method
 */
public final class Logging {
    private static final Logger logger = Logger.getLogger(Logging.class.getName());
    
    static {
        try {
            // disable logs from appearing in terminal
            logger.setUseParentHandlers(false);
            for (Handler h : logger.getHandlers())
                logger.removeHandler(h);
            
            // output to a .log file
            FileHandler handler = new FileHandler("app.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            
            logger.setLevel(Level.ALL);  // display all levels of severity, not just error or warning
        } catch (IOException e) {
            log("Error setting up file logging", Level.SEVERE, e);
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
     * @see #log(String, Level, Exception) Exception Logging Method
     */
    public static void log(String message, Level level) {
        logger.log(level, message);
    }
    
    /**
     * Overload logging method to also display an exception
     * <p>
     * Write error log to 'app.log' with timestamp, severity level, message, and thrown error.
     *
     * @param message message to be displayed in log
     * @param level severity of message as java utility logging object
     * @param error error thrown with log
     * @see Logger
     * @see Level
     * @see #log(String, Level) Standard Logging Method
     */
    public static void log(String message, Level level, Exception error) {
        logger.log(level, message, error);
    }
}
