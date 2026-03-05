package controllers;

import app.App;
import models.*;
import models.MessageCellRenderer;
import utils.Logging;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * A class representing a singular chat page in the Hadrian application, handling all functionality.
 * <p>
 * All chats should be able to function using this class
 */
public class ChatController extends AbstractController implements Loadable {
    private JPanel contentPanel;
    private User user;
    private final Chat data;
    private JButton backButton;
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
        for (String member : data.getUsers()) {
            String hostName = "unknown";
            
            try {
                hostName = InetAddress.getByName(member).getHostName();
            } catch (UnknownHostException e) {
                Logging.log("Failed To Retrieve Host Name", Level.WARNING, e);
            } finally {
                memberModel.addElement(hostName + " [" + member + "]");
            }
        }
        
        App.draw();
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
    
    /**
     * Method containing all message sending functionality.
     * <p>
     * Updates current chat object, displays message, clears text field.
     */
    private void sendMessage() {
        if (messageField.getText().isBlank()) {
            displayNotif("Cannot Send Blank Message");
            return;
        }
        
        Message message = new Message(messageField.getText(), user);
        
        data.addMessage(message);
        messageModel.add(messageModel.size(), message);
        
        messageField.setText("");
        
        App.draw();
    }
}
