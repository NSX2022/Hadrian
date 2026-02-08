package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Dataclass representing a singular chat.
 */
public class Chat {
    private final int localID;
    private final HashSet<User> users;
    private final LinkedList<String> messages;
    
    public Chat(int localID, ArrayList<User> members, String firstMessage) {
        this.localID = localID;
        users = new HashSet<>(members);
        messages = new LinkedList<>();
        
        messages.add(firstMessage);
    }
    
    public int getId() {
        return localID;
    }
    
    public HashSet<User> getUsers() {
        return users;
    }
    
    public LinkedList<String> getMessages() {
        return messages;
    }
    
    /**
     * Adds a user to a chat's user collection.
     *
     * @param user User object to be added to collection
     * @return true if user was not already in collection and added successfully, false otherwise.
     * @see User
     */
    public boolean addUser(User user) {
        return users.add(user);
    }
    
    /**
     * Removes a user from a chat's user collection
     *
     * @param user User object to be removed from collection
     * @return true if user was in collection and removed successfully, false otherwise.
     * @see User
     */
    public boolean removeUser(User user) {
        return users.remove(user);
    }
    
    /**
     * Adds a message to the end of the chat's message collection
     *
     * @param message message to be added to the end of the collection
     */
    public void addMessage(String message) {
        messages.add(message);
    }
}
