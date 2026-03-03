package models;

import java.util.Date;

/**
 * Dataclass representing a single message in the Hadrian application.
 *
 * @param text text content of a message
 * @param sender user that sent the message
 * @param date time and date the message was sent
 */
public record Message(String text,
                      User sender,
                      Date date) {
    /**
     * Creating a message object without a date.
     * <p>
     * Automatically generates a date for each object using the current date and time
     * @param text text content of a message
     * @param sender user that sent the message
     */
    public Message(String text, User sender) {
        this(text, sender, new Date());
    }
}
