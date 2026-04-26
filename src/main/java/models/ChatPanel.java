package models;

import app.App;
import controllers.ChatController;
import utils.Logging;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.logging.Level;

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
        
        TitledBorder border = (TitledBorder) contentPanel.getBorder();
        border.setTitle("Chat " + (chatIndex + 1));
        
        openChatButton.addActionListener(e -> {
            ChatController controller = new ChatController(appFrame, chat);
            controller.init();
            controller.load(App.getUser());
            
            App.draw();
        });
        
        truncateLabel(membersLabel);
        truncateLabel(recentMessageLabel);
    }
    
    /**
     * Truncates a label to a constant character limit integer
     * <p>
     * If label length is above constant limit, the last 3 characters before cutoff are replaced with "..."
     * (i.e.: Some Test Messag...)
     *
     * @param label JLabel object to truncate the text of
     * @see JLabel
     * @see #maxLabelLength
     */
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
            String member = membersArray[i], hostName = "unknown";
            
            if (App.getConfig().getHideIp()) {
                try {
                    hostName = InetAddress.getByName(member).getHostName();
                } catch (UnknownHostException e) {
                    Logging.log("Failed To Retrieve Host Name", Level.WARNING, e);
                }
            } else {
                hostName = membersArray[i];
            }
            
            membersString.append(hostName);
            
            if (i != membersArray.length - 1)
                membersString.append(", ");
        }
        
        membersLabel.setText(membersString.toString());
    }
}
