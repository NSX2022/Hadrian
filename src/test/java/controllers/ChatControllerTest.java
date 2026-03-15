package controllers;

import models.Chat;
import models.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;

import javax.swing.*;

class ChatControllerTest extends AbstractTestController<ChatController> {
    private static Chat chat;
    
    @BeforeAll
    static void setUp() {
        chat = user.getChat(0);
    }
    
    @Override
    protected ChatController createController() {
        ChatController chatController = new ChatController(frame, chat);
        chatController.load(user);
        return chatController;
    }
    
    @Override
    @Test
    protected void buttons() throws NoSuchFieldException {
        JButton backButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "backButton");
        backButton.doClick();
        assert frame.getContentPane().getClass() == ChatsController.class;
        
        int messages = chat.getMessages().size();
        JButton sendButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "sendButton");
        JTextField messageField = (JTextField) ReflectionUtils.getPrivateInstance(controller, "messageField");
        messageField.setText(TEST_MESSAGE);
        sendButton.doClick();
        int newMessages = chat.getMessages().size();
        assert messages + 1 == newMessages && chat.getLastMessage().text().equals(TEST_MESSAGE);
    }
    
    @Test
    void members() throws NoSuchFieldException {
        DefaultListModel<String> memberModel =
                (DefaultListModel<String>) ReflectionUtils.getPrivateInstance(controller, "memberModel");
        assert memberModel.size() == chat.getUsers().size();
    }
    
    @Test
    void messages() throws NoSuchFieldException {
        DefaultListModel<Message> messageModel =
                (DefaultListModel<Message>) ReflectionUtils.getPrivateInstance(controller, "messageModel");
        assert !messageModel.isEmpty();
    }
    
    @Test
    void sendMessage() throws NoSuchMethodException, NoSuchFieldException {
        JTextField messageField = (JTextField) ReflectionUtils.getPrivateInstance(controller, "messageField");
        messageField.setText(TEST_MESSAGE);
        ReflectionUtils.invokePrivateInstance(controller, "sendMessage", new Class[0]);
        
        assert chat.getLastMessage().text().equals(TEST_MESSAGE);
    }
}
