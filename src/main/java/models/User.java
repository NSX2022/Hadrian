package models;

import tutorial.Tutorial;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Dataclass representing a singular user.
 */
public class User {
    private String ip, username;
    private final HashMap<Integer, Chat> chats;
    
    public User(String ip, String username) {  // new user
        this.ip = ip;
        this.username = username;
        chats = new HashMap<>();
        
        new Tutorial(
                this,
                new User(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        "TutorialBot",
                        null
                )
        );
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
    
    // TODO fill in any more user data
}
