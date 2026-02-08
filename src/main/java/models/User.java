package models;

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
    }
    
    public User(String ip, String username, LinkedHashSet<Chat> chats) {  // existing user (info from server ???)
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
    // endregion
    
    // TODO fill in any more user data
}
