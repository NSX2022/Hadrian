package models;

import org.junit.jupiter.api.Test;
import testingClasses.AbstractTest;

import java.util.Date;

class MessageTest extends AbstractTest {
    @Test
    void compareDates() {
        Message autoDate = new Message(TEST_MESSAGE, user),
                givenDate = new Message(TEST_MESSAGE, user, new Date());
        
        assert autoDate.date().toString().equals(givenDate.date().toString());
    }
}
