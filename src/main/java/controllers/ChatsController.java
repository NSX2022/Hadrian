package controllers;

import app.App;
import models.Chat;
import models.ChatPanel;
import models.Screens;
import models.User;
import utils.Logging;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

public class ChatsController extends AbstractController implements Loadable {
    private final JFrame appFrame;
    private JPanel contentPanel;
    private User user;
    private JButton backButton;
    private JPanel chatsPanel;
    private JTextField usersField;
    private JTextArea messageArea;
    private JButton createChatButton;
    
    public ChatsController(JFrame appFrame) {
        super(appFrame, Screens.CHATS);
        this.appFrame = appFrame;
        
        backButton.addActionListener(e -> App.changeScreen(Screens.HOME));
        usersField.addActionListener(e -> messageArea.grabFocus());
        createChatButton.addActionListener(e -> createChat());
        
        bindKey("ESCAPE", "back", () -> App.changeScreen(Screens.HOME));
    }
    
    @Override
    public void load(User user) {
        this.user = user;
        
        for (int i = 0; i < user.getChats().size(); i++) {
            Chat chat = user.getChat(i);
            ChatPanel panel = new ChatPanel(appFrame, i, user, chat.getUsers(), chat.getLastMessage().text());
            chatsPanel.add(panel, i);
        }
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
    
    private void createChat() {
        if (invalidFields())
            return;
        
        HashSet<String> users = new HashSet<>(List.of(usersField.getText().split(",")));
        Chat chat = new Chat(users, messageArea.getText(), user);
        user.addChat(chat);
        
        ChatController controller = new ChatController(appFrame, chat);
        controller.init();
        controller.load(user);
        
        App.draw();
        
        Logging.log("Created New Chat With Members: " + usersField.getText(), Level.INFO);
    }
    
    private boolean invalidFields() {
        if (usersField.getText().isBlank() || messageArea.getText().isBlank()) {
            // TODO blank field notification
            Logging.log("One Or More Fields Are Blank", Level.WARNING);
            return true;
        }
        
        String regex = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                       "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                       "(\\s*,\\s*((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                       "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d))*$";
        
        if (!usersField.getText().matches(regex)) {
            // TODO invalid IP listing
            Logging.log("Invalid IP Listing", Level.WARNING);
            return true;
        }
        
        return false;
    }
}
