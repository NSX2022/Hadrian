package controllers;

import app.App;
import models.Chat;
import models.ChatPanel;
import models.Screens;
import models.User;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;

/**
 * A class representing the entire chats page in the Hadrian application.
 * <p>
 * Handles creation of new chats and displaying existing chats
 */
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
            ChatPanel panel = new ChatPanel(appFrame, i, chat);
            chatsPanel.add(panel, i);
        }
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
    
    /**
     * Method handling the creation of a new chat only if select fields have provided valid information.
     */
    private void createChat() {
        if (invalidFields())
            return;
        
        HashSet<String> users = new HashSet<>(List.of(usersField.getText().split(",")));
        Chat chat = new Chat(users, messageArea.getText(), user);
        
        ChatController controller = new ChatController(appFrame, chat);
        controller.init();
        controller.load(user);
        
        App.draw();
        
        displayNotif("Created New Chat With Members: " + usersField.getText());
    }
    
    /**
     * Checks if users field and message field are valid.
     * <p>
     * Checks if either field is empty, checks if users field contains valid IP addresses using regex.
     *
     * @return true if fields are valid, false otherwise
     * @see #createChat()
     */
    private boolean invalidFields() {
        if (usersField.getText().isBlank() || messageArea.getText().isBlank()) {
            displayNotif("One Or More Fields Are Blank");
            return true;
        }
        
        String regex = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                       "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                       "(\\s*,\\s*((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                       "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d))*$";
        
        if (!usersField.getText().matches(regex)) {
            displayNotif("Invalid IP Listing");
            return true;
        }
        
        return false;
    }
}
