package controllers;

import models.Chat;
import models.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

class ChatControllerTest extends AbstractTestController<ChatController> {
    private static Chat chat;
    
    @BeforeAll
    static void setUp() {
        chat = user.getChat(0);
    }
    
    @Override
    protected void createController() {
        controller = new ChatController(frame, chat);
        controller.load(user);
    }
    
    @Test
    void members() throws NoSuchFieldException, IllegalAccessException {
        DefaultListModel<String> memberModel =
                (DefaultListModel<String>) ReflectionUtils.getPrivate(controller, "memberModel");
        assert memberModel.size() == chat.getUsers().size();
    }
    
    @Test
    void messages() throws NoSuchFieldException, IllegalAccessException {
        DefaultListModel<Message> messageModel =
                (DefaultListModel<Message>) ReflectionUtils.getPrivate(controller, "messageModel");
        assert !messageModel.isEmpty();
    }
    
    @Test
    void sendMessage() throws InvocationTargetException, NoSuchMethodException,
                              IllegalAccessException, NoSuchFieldException {
        JTextField messageField = (JTextField) ReflectionUtils.getPrivate(controller, "messageField");
        messageField.setText(TEST_MESSAGE);
        ReflectionUtils.invokePrivate(controller, "sendMessage", new Class[0]);
        
        assert chat.getLastMessage().text().equals(TEST_MESSAGE);
    }
}
