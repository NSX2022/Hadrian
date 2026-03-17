package controllers;

import models.ChatPanel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import testingClasses.ReflectionUtils;

import javax.swing.*;
import java.awt.*;

class ChatsControllerTest extends AbstractTestController<ChatsController> {
    @Override
    protected ChatsController createController() {
        ChatsController chatsController = new ChatsController(frame);
        chatsController.load(user);
        return chatsController;
    }
    
    @Override
    @Test
    protected void buttons() throws NoSuchFieldException {
        JButton backButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "backButton");
        backButton.doClick();
        assert frame.getContentPane().getClass() == HomeController.class;
        
        JTextField usersField = (JTextField) ReflectionUtils.getPrivateInstance(controller, "usersField");
        JTextArea messageArea = (JTextArea) ReflectionUtils.getPrivateInstance(controller, "messageArea");
        
        usersField.setText("1.1.1.1");
        messageArea.setText(TEST_MESSAGE);
        
        int chats = user.getChats().size();
        JButton createChatButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "createChatButton");
        createChatButton.doClick();
        
        assert chats + 1 == user.getChats().size() && frame.getContentPane().getClass() == ChatController.class;
    }
    
    @Test
    void chatsPanel() throws NoSuchFieldException {
        JPanel chatsPanel = (JPanel) ReflectionUtils.getPrivateInstance(controller, "chatsPanel");
        
        int count = 0;
        for (Component component : chatsPanel.getComponents())
            if (component instanceof ChatPanel)
                count++;
        
        assert count == 1;
    }
    
    @Test
    void createChat() throws NoSuchFieldException, NoSuchMethodException {
        JTextField usersField = (JTextField) ReflectionUtils.getPrivateInstance(controller, "usersField");
        JTextArea messageArea = (JTextArea) ReflectionUtils.getPrivateInstance(controller, "messageArea");
        
        usersField.setText("1.1.1.1, 2.2.2.2");
        messageArea.setText(TEST_MESSAGE);
        int chats = user.getChats().size();
        ReflectionUtils.invokePrivateInstance(controller, "createChat", new Class[0]);
        
        assert chats + 1 == user.getChats().size() && frame.getContentPane().getClass() == ChatController.class;
    }
    
    @ParameterizedTest
    @CsvSource({
            ",",
            "1.1.1.1,",
            "1.1.1.1 2.2.2.2,Message Test",
            "255.255.256.255,Message Test"
    })
    void createChatErrors(String users, String message) throws NoSuchFieldException, NoSuchMethodException {
        JTextField usersField = (JTextField) ReflectionUtils.getPrivateInstance(controller, "usersField");
        JTextArea messageArea = (JTextArea) ReflectionUtils.getPrivateInstance(controller, "messageArea");
        
        usersField.setText(users);
        messageArea.setText(message);
        
        assert ReflectionUtils.invokePrivateInstance(
                controller,
                "invalidFields",
                new Class[0]
        ) instanceof Boolean isInvalid && isInvalid;
    }
}
