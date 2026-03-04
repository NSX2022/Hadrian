package models;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Dataclass representing a singular user in the Hadrian application.
 */
public class User {
    private String ip, username;
    private final HashMap<Integer, Chat> chats;
    
    public User(String ip, String username) {  // new user
        this.ip = ip;
        this.username = username;
        chats = new HashMap<>();
        
        createTutorial();
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
    }
    
    public User(String ip, String username, HashMap<Integer, Chat> chats) {
        this.ip = ip;
        this.username = username;
        this.chats = chats;
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
    
    public HashSet<Chat> getChats() {
        return new HashSet<>(chats.values());
    }
    
    public Chat getChat(int index) {
        return chats.get(index);
    }
    
    public void addChat(Chat chat) {
        chats.put(chats.size(), chat);
    }
    
    public void removeChat(int index) {
        chats.remove(index);
    }
    // endregion
}
