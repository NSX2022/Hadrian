package utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

class LoggingTest {
    private static final List<Level> levels = Arrays.asList(Level.INFO, Level.WARNING, Level.SEVERE);
    private final String LOG_FILE = "app.log", MESSAGE = "Test Message";
    
    @ParameterizedTest
    @FieldSource("levels")
    void log(Level level) throws IOException {
        Logging.log(MESSAGE, level);
        
        String content = Files.readString(Paths.get(LOG_FILE));
        
        assert content.endsWith(level.getName() + ": " + MESSAGE + "\n");
    }
    
    @ParameterizedTest
    @FieldSource("levels")
    void errorLog(Level level) throws IOException {
        Exception exception = new Exception("Test Exception");
        Logging.log(MESSAGE, level, exception);
        
        String[] content = Files.readString(Paths.get(LOG_FILE)).split("\n\n");
        String last = content[content.length - 1];
        
        assert last.contains(level.getName() + ": " + MESSAGE + "\njava.lang.Exception: Test Exception");
    }
}
