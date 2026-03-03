package models;

import app.App;
import controllers.ChatController;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * A class representing a singular chat panel object.
 */
public final class ChatPanel extends JPanel {
    private JPanel contentPanel;
    private JButton openChatButton;
    private JLabel membersLabel;
    private JLabel recentMessageLabel;
    
    public ChatPanel(JFrame appFrame, int chatIndex, User user, HashSet<String> members, String lastMessage) {
        add(contentPanel);
        
        setMembersLabel(members);
        
        recentMessageLabel.setText(lastMessage);
        openChatButton.setText("Open Chat " + (chatIndex + 1));
        
        openChatButton.addActionListener(e -> {
            ChatController controller = new ChatController(appFrame, user.getChat(chatIndex));
            controller.init();
            controller.load(App.getUser());
            
            App.draw();
        });
    }
    
    /**
     * Sets text of member label to comma listing format (i.e.: 1.1.1.1, 2.2.2.2, 3.3.3.3)
     *
     * @param members a collection of unique string values representing each member's IP address
     */
    private void setMembersLabel(HashSet<String> members) {
        StringBuilder membersString = new StringBuilder();
        
        String[] membersArray = new String[0];
        membersArray = members.toArray(membersArray);
        
        for (int i = 0; i < membersArray.length; i++) {
            membersString.append(membersArray[i]);
            
            if (i != membersArray.length - 1)
                membersString.append(", ");
        }
        
        membersLabel.setText(membersString.toString());
        
        // TODO remove all commented depreciated code
        //  handle all to dos
        //  create notification functionality
    }
}
