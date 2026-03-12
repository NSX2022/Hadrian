package models;

import org.junit.jupiter.api.Test;

import java.util.Date;

class MessageTest {
    @Test
    void compareDates() {
        Message autoDate = new Message(null, new User(null, null)),
                givenDate = new Message(null, new User(null, null), new Date());
        
        assert autoDate.date().toString().equals(givenDate.date().toString());
    }
}
