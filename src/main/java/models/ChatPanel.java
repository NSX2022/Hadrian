package models;

import app.App;
import controllers.ChatController;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

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
        
        openChatButton.addActionListener(actionEvent -> {
            ChatController controller = new ChatController(appFrame, user.getChat(chatIndex));
            controller.init();
            controller.load(App.getUser());
            
            App.draw();
        });
    }
    
    private void setMembersLabel(HashSet<String> members) {
        StringBuilder membersString = new StringBuilder();
        
        String[] membersArray = new String[0];
        membersArray = members.toArray(membersArray);
        
        for (int i = 0; i < membersArray.length; i++) {
            String member = membersArray[i];
            if (membersString.length() + member.length() >= membersLabel.getMaximumSize().width) {
                membersString.append("...");
                break;
            }
            
            membersString.append(member);
            
            if (i != membersArray.length - 1)
                membersString.append(", ");
        }
        membersLabel.setText(membersString.toString());
        
        // TODO remove all commented depreciated code
        //  handle all to dos
        //  create notification functionality
    }
}
