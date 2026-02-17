package models;

import java.util.Date;

public record Message(String text,
                      User sender,
                      Date date) {
    
    public Message(String text, User sender) {
        this(text, sender, new Date());
    }
}
