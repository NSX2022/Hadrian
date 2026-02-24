package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Dataclass representing a singular chat.
 */
public class Chat {
    private final int localID;
    private final HashSet<String> users;
    private final LinkedList<Message> messages;
    
    public Chat(int localID, ArrayList<String> members, String firstMessage, User user) {
        this.localID = localID;
        users = new HashSet<>(members);
        if (!members.contains(user.getIp()))
            users.add(user.getIp());
        messages = new LinkedList<>();
        
        addMessage(firstMessage, user);
        
        user.addChat(this);
    }
    
    public int getId() {
        return localID;
    }
    
    public HashSet<String> getUsers() {
        return users;
    }
    
    public LinkedList<Message> getMessages() {
        return messages;
    }
    
    public Message getLastMessage() {
        return messages.getLast();
    }
    
    /**
     * Adds a user's IP address to a chat's user collection.
     *
     * @param user User IP to be added to collection
     * @return true if user was not already in collection and added successfully, false otherwise.
     */
    public boolean addUser(String user) {
        return users.add(user);
    }
    
    /**
     * Removes a user's IP from a chat's user collection
     *
     * @param user User IP to be removed from collection
     * @return true if user was in collection and removed successfully, false otherwise.
     */
    public boolean removeUser(String user) {
        return users.remove(user);
    }
    
    /**
     * Adds a message object to the end of the chat's message collection
     *
     * @param text text to be added to the end of the collection
     * @see Message
     */
    public void addMessage(String text, User sender) {
        Message message = new Message(text, sender);
        messages.add(message);
    }
}
