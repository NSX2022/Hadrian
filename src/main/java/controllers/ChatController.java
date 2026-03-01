package controllers;

import app.App;
import models.*;

import javax.swing.*;
import java.util.LinkedList;

public class ChatController extends AbstractController implements Loadable {
    private JPanel contentPanel;
    private User user;
    private final Chat data;
    private JButton backButton;
    private JLabel chatNameLabel;
    private final DefaultListModel<String> memberModel;
    private JList<String> membersList;
    private final DefaultListModel<Message> messageModel;
    private JList<Message> messageList;
    private JTextField messageField;
    private JButton sendButton;
    
    public ChatController(JFrame appFrame, Chat data) {
        super(appFrame, Screens.CHAT);
        this.data = data;
        
        memberModel = new DefaultListModel<>();
        membersList.setModel(memberModel);
        messageModel = new DefaultListModel<>();
        messageList.setModel(messageModel);
        messageList.setCellRenderer(new MessageCellRenderer());
        
        backButton.addActionListener(e -> App.changeScreen(Screens.CHATS));
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        
        bindKey("ESCAPE", "back", () -> App.changeScreen(Screens.CHATS));
    }

    @Override
    public void load(User user) {
        this.user = user;
        
        // load messages
        LinkedList<Message> messages = data.getMessages();
        for (int i = 0; i < messages.size(); i++)
            messageModel.add(i, messages.get(i));
        
        // load users
        for (String member : data.getUsers())
            memberModel.addElement(member);
        
        App.draw();
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }

    private void sendMessage() {
        Message message = new Message(messageField.getText(), user);
        
        data.addMessage(message);
        messageModel.add(messageModel.size(), message);
        
        messageField.setText("");
        
        App.draw();
    }
}
