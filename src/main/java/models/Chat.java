package models;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Dataclass representing a singular chat.
 */
public class Chat {
    private final HashSet<String> users;
    private final LinkedList<Message> messages;
    
    public Chat(HashSet<String> users, String firstMessage, User user) {
        this.users = users;
        
        users.add(user.getIp());
        messages = new LinkedList<>();
        
        addMessage(firstMessage, user);
        
        user.addChat(this);
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
     * @see #addMessage(Message)
     */
    public void addMessage(String text, User sender) {
        addMessage(new Message(text, sender));
    }
    
    /**
     * Adds a message object to the end of the chat's message collection
     *
     * @param message message object to be added to chat's message collection
     * @see Message
     * @see #addMessage(String, User)
     */
    public void addMessage(Message message) {
        messages.add(message);
    }
}
