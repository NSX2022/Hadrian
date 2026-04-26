package controllers;

import app.App;
import models.*;
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
    private final Chat data;
    private final DefaultListModel<String> memberModel;
    private final DefaultListModel<Message> messageModel;
    private JPanel contentPanel;
    private User user;
    private JButton backButton;
    private JList<String> membersList;
    private JList<Message> messageList;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane scrollPane;
    
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
    
    /**
     * {@inheritDoc}
     */
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
                if (App.getConfig().getHideIp())
                    memberModel.addElement(hostName);
                else
                    memberModel.addElement(hostName + " [" + member + "]");
            }
        }
        
        App.draw();
        
        scrollToBottom();
    }
    
    /**
     * {@inheritDoc}
     */
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
        
        scrollToBottom();
    }
    
    /**
     * Forces the scroll pane containing chat messages to reach its maximum value
     * (displaying the bottom, most recent messages)
     *
     * @see JScrollPane#getVerticalScrollBar()
     */
    private void scrollToBottom() {
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
}
