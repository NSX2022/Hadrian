package models;

import org.junit.jupiter.api.Test;
import testingClasses.AbstractTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

class UserTest extends AbstractTest {
    @Test
    void tutorial() {
        assert user.getChats().size() == 1;
        
        Chat tutorial = user.getChat(0);
        assert tutorial.getMessages().size() > 1;
        
        HashSet<String> tutorialUsers = tutorial.getUsers();
        assert tutorialUsers.contains(user.getIp()) &&
                tutorialUsers.contains(InetAddress.getLoopbackAddress().getHostAddress());
    }
    
    @Test
    void existingUser() throws UnknownHostException {
        User newUser = new User(
                InetAddress.getLocalHost().getHostAddress(),
                InetAddress.getLocalHost().getHostName(),
                new ArrayList<>()
        );
        
        assert newUser.getChats().isEmpty();
    }
}
