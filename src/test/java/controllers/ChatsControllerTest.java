package controllers;

import models.ChatPanel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import testingClasses.ReflectionUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

class ChatsControllerTest extends AbstractTestController<ChatsController> {
    @Override
    protected void createController() {
        controller = new ChatsController(frame);
        controller.load(user);
    }
    
    @Test
    void chatsPanel() throws NoSuchFieldException, IllegalAccessException {
        JPanel chatsPanel = (JPanel) ReflectionUtils.getPrivate(controller, "chatsPanel");
        
        int count = 0;
        for (Component component : chatsPanel.getComponents())
            if (component instanceof ChatPanel)
                count++;
        
        assert count == 1;
    }
    
    @Test
    void createChat() throws NoSuchFieldException, IllegalAccessException,
                             InvocationTargetException, NoSuchMethodException {
        JTextField usersField = (JTextField) ReflectionUtils.getPrivate(controller, "usersField");
        JTextArea messageArea = (JTextArea) ReflectionUtils.getPrivate(controller, "messageArea");
        
        usersField.setText("1.1.1.1, 2.2.2.2");
        messageArea.setText(TEST_MESSAGE);
        ReflectionUtils.invokePrivate(controller, "createChat", new Class[0]);
        
        assert user.getChats().size() == 2;
    }
    
    @ParameterizedTest
    @CsvSource({
            ",",
            "1.1.1.1,",
            "1.1.1.1 2.2.2.2,Message Test",
            "255.255.256.255,Message Test"
    })
    void createChatErrors(String users, String message) throws NoSuchFieldException, IllegalAccessException,
                                                               InvocationTargetException, NoSuchMethodException {
        JTextField usersField = (JTextField) ReflectionUtils.getPrivate(controller, "usersField");
        JTextArea messageArea = (JTextArea) ReflectionUtils.getPrivate(controller, "messageArea");
        
        usersField.setText(users);
        messageArea.setText(message);
        
        assert ReflectionUtils.invokePrivate(
                controller,
                "invalidFields",
                new Class[0]
        ) instanceof Boolean isInvalid && isInvalid;
    }
}
