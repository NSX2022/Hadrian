package models;

import app.App;
import controllers.ChatController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testingClasses.AbstractTest;
import testingClasses.ReflectionUtils;

import javax.swing.*;

class ChatPanelTest extends AbstractTest {
    private static ChatPanel chatPanel;
    private static Chat tutorial;
    
    @BeforeAll
    static void setUp() {
        tutorial = user.getChat(0);
        chatPanel = new ChatPanel(frame, 0, tutorial);
    }
    
    @Test
    void members() throws NoSuchFieldException {
        JLabel membersLabel = (JLabel) ReflectionUtils.getPrivateInstance(chatPanel, "membersLabel");
        
        for (String user : tutorial.getUsers())
            assert membersLabel.getText().contains(user);
        
        assert membersLabel.getText().split(", ").length == tutorial.getUsers().size();
    }
    
    @Test
    void lastMessage() throws NoSuchFieldException {
        JLabel recentMessageLabel = (JLabel) ReflectionUtils.getPrivateInstance(chatPanel, "recentMessageLabel");
        assert recentMessageLabel.getText().equals(tutorial.getLastMessage().text());
    }
    
    @Test
    void button() throws NoSuchFieldException {
        ReflectionUtils.setPrivateStatic(App.class, "frame", frame);
        
        JButton openChatButton = (JButton) ReflectionUtils.getPrivateInstance(chatPanel, "openChatButton");
        openChatButton.doClick();
        
        if (frame.getContentPane() instanceof ChatController controller) {
            Chat data = (Chat) ReflectionUtils.getPrivateInstance(controller, "data");
            assert data == tutorial;
        }
    }
}
