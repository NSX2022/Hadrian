package models;

import app.App;
import controllers.ChatController;

import javax.swing.*;
import java.util.HashSet;

/**
 * A class representing a singular chat panel object.
 */
public final class ChatPanel extends JPanel {
    private final int maxLabelLength = 30;
    private JPanel contentPanel;
    private JButton openChatButton;
    private JLabel membersLabel;
    private JLabel recentMessageLabel;
    
    public ChatPanel(JFrame appFrame, int chatIndex, Chat chat) {
        add(contentPanel);
        
        setMembersLabel(chat.getUsers());
        
        recentMessageLabel.setText(chat.getLastMessage().text());
        openChatButton.setText("Open Chat " + (chatIndex + 1));
        
        openChatButton.addActionListener(e -> {
            ChatController controller = new ChatController(appFrame, chat);
            controller.init();
            controller.load(App.getUser());
            
            App.draw();
        });
        
        truncateLabel(membersLabel);
        truncateLabel(recentMessageLabel);
    }
    
    private void truncateLabel(JLabel label) {
        String text = label.getText();
        
        if (text.length() <= maxLabelLength)
            return;
        
        text = text.substring(0, maxLabelLength - 3) + "...";
        label.setText(text);
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
