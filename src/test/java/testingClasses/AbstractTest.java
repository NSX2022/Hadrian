package testingClasses;

import models.User;
import org.junit.jupiter.api.BeforeAll;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class AbstractTest {
    protected static User user;
    protected static JFrame frame;
    protected final String TEST_MESSAGE = "Message Test";
    
    @BeforeAll
    static void setUp() throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        
        user = new User(
                host.getHostAddress(),
                host.getHostName()
        );
        
        frame = new JFrame();
    }
}
