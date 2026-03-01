package tutorial;

import models.Chat;
import models.User;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public final class Tutorial extends Chat {
    public Tutorial(User user, User tutorialBot) {
        super(
                new HashSet<>(List.of(user.getIp(), tutorialBot.getIp())),
                "This is the tutorial chat.",
                user
        );
        
        addMessage("Nothing you send here leaves your computer.", tutorialBot);
        addMessage("You can type anything and press [Enter] to send it.", tutorialBot);
        addMessage("Try sending me a message.", tutorialBot);
    }
}
