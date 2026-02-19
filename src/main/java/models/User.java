package models;

import tutorial.Tutorial;

import java.net.InetAddress;
import java.util.LinkedHashSet;

/**
 * Dataclass representing a singular user.
 */
public class User {
    private String ip, username;
    private final LinkedHashSet<Chat> chats;
    
    public User(String ip, String username) {  // new user
        this.ip = ip;
        this.username = username;
        chats = new LinkedHashSet<>();
        
        new Tutorial(
                this,
                new User(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        "TutorialBot",
                        null
                )
        );
    }
    
    public User(String ip, String username, LinkedHashSet<Chat> chats) {
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
    
    public LinkedHashSet<Chat> getChats() {
        return chats;
    }
    
    public void addChat(Chat chat) {
        chats.add(chat);
    }
    
    public void removeChat(Chat chat) {
        chats.remove(chat);
    }
    // endregion
    
    // TODO fill in any more user data
}
