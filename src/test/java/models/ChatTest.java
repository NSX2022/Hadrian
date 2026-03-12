package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testingClasses.AbstractTest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;

class ChatTest extends AbstractTest {
    private Chat chat;
    
    @BeforeEach
    void setUp() throws UnknownHostException {
        HashSet<String> users = new HashSet<>(Arrays.asList("1.1.1.1", "2.2.2.2", "3.3.3.3"));
        user = new User(InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getHostName());
        chat = new Chat(users, TEST_MESSAGE, user);
    }
    
    @Test
    void getUsers() throws UnknownHostException {
        assert chat.getUsers().size() == 4;
        assert chat.getUsers().contains(InetAddress.getLocalHost().getHostAddress());
    }
    
    @Test
    void userHasChat() {
        assert user.getChats().contains(chat);
    }
    
    @Test
    void getMessages() {
        assert chat.getMessages().size() == 1;
        assert chat.getMessages().getFirst() == chat.getMessages().getLast();
    }
    
    @Test
    void getLastMessage() {
        assert chat.getLastMessage() == chat.getMessages().getFirst();
        assert chat.getLastMessage().text().equals(TEST_MESSAGE);
    }
    
    @Test
    void addUser() {
        String hostAddress = InetAddress.getLoopbackAddress().getHostAddress();
        chat.addUser(hostAddress);
        assert chat.getUsers().contains(hostAddress);
        assert !chat.addUser("1.1.1.1");
    }
    
    @Test
    void removeUser() {
        String hostAddress = InetAddress.getLoopbackAddress().getHostAddress();
        
        chat.addUser(hostAddress);
        
        assert chat.getUsers().contains(hostAddress);
        assert chat.removeUser(hostAddress);
        assert !chat.removeUser("255.255.255.255");
    }
    
    @Test
    void addMessage() {
        chat.addMessage(TEST_MESSAGE, user);
        
        assert chat.getLastMessage().text().equals(TEST_MESSAGE);
    }
    
    @Test
    void addMessageObject() {
        Message messageObject = new Message(TEST_MESSAGE, user);
        
        chat.addMessage(messageObject);
        
        assert chat.getMessages().contains(messageObject);
        assert chat.getLastMessage().equals(messageObject);
    }
}
