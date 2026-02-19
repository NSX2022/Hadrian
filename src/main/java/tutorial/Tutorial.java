package tutorial;

import models.Chat;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;

public final class Tutorial extends Chat {
    private final User user, tutorialBot;
    
    public Tutorial(User user, User tutorialBot) {
        super(
                0,
                new ArrayList<>(Arrays.asList(user.getIp(), tutorialBot.getIp())),
                "This is the tutorial chat.",
                user
        );
        this.user = user;
        this.tutorialBot = tutorialBot;
        
        fillChat();
    }
    
    private void fillChat() {
        addMessage("This is a practice chat — nothing you send here leaves your computer.", tutorialBot);
        addMessage("You can type anything and press Enter to send it.", tutorialBot);
        addMessage("Try sending me a text.", tutorialBot);
    }
}
