package models;

import static app.Serialization.serialize;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Dataclass representing a singular user in the Hadrian application.
 */
public class User implements Serializable {
    private final ArrayList<Chat> chats;
    private String ip, username;
    
    /**
     * Constructor for creating a new user and initializing the tutorial chat.
     *
     * @param ip user's IP address
     * @param username username to display the user as (typically the hostname)
     */
    public User(String ip, String username) {
        this.ip = ip;
        this.username = username;
        chats = new ArrayList<>();
        
        createTutorial();
    }
    
    /**
     * Constructor for initializing an already existing user, not creating a tutorial.
     *
     * @param ip user's IP address
     * @param username username to display the user as (typically the hostname)
     * @param chats any chats the user is currently a member of
     */
    public User(String ip, String username, ArrayList<Chat> chats) {
        this.ip = ip;
        this.username = username;
        this.chats = chats;
    }
    
    /**
     * Adds the initialized Hadrian tutorial to the user's chats
     */
    private void createTutorial() {
        User tutorialBot = new User(
                InetAddress.getLoopbackAddress().getHostAddress(),
                "TutorialBot",
                null
        );
        
        Chat tutorial = new Chat(
                new HashSet<>(List.of(tutorialBot.getIp())),
                "This is the tutorial chat.",
                this
        );
        
        tutorial.addMessage("Nothing you send here leaves your computer.", tutorialBot);
        tutorial.addMessage("You can type anything and press [Enter] to send it.", tutorialBot);
        tutorial.addMessage("Try sending me a message.", tutorialBot);
        
        serialize(tutorial, tutorial.getMessages().getFirst().text());
    }
    
    // region Getters/Setters
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public ArrayList<Chat> getChats() {
        return chats;
    }
    
    public Chat getChat(int index) {
        return chats.get(index);
    }
    
    public void addChat(Chat chat) {
        chats.add(chat);
    }
    
    public void removeChat(Chat chat) {
        chats.remove(chat);
    }
    
    public void removeChat(int index) {
        chats.remove(index);
    }
    // endregion
}
