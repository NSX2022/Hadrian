package utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import testingClasses.AbstractTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

class LoggingTest extends AbstractTest {
    private static final List<Level> levels = Arrays.asList(Level.INFO, Level.WARNING, Level.SEVERE);
    private final String LOG_FILE = "app.log";
    
    @ParameterizedTest
    @FieldSource("levels")
    void log(Level level) throws IOException {
        Logging.log(TEST_MESSAGE, level);
        
        String content = Files.readString(Paths.get(LOG_FILE));
        
        assert content.endsWith(level.getName() + ": " + TEST_MESSAGE + "\n");
    }
    
    @ParameterizedTest
    @FieldSource("levels")
    void errorLog(Level level) throws IOException {
        String exceptionMessage = "Test Exception";
        Exception exception = new Exception(exceptionMessage);
        Logging.log(TEST_MESSAGE, level, exception);
        
        String[] content = Files.readString(Paths.get(LOG_FILE)).split("\n\n");
        String last = content[content.length - 1];
        
        assert last.contains(level.getName() + ": " + TEST_MESSAGE + "\njava.lang.Exception: " + exceptionMessage);
    }
}
