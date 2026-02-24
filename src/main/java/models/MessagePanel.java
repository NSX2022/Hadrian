package models;

import javax.swing.*;

public final class MessagePanel extends JPanel {
    private JPanel contentPanel;
    private JLabel senderLabel;
    private JLabel messageLabel;
    
    public MessagePanel(String senderName, String messageText) {
        add(contentPanel);
        
        senderLabel.setText(senderName);
        messageLabel.setText(messageText);
    }
    
    public JPanel getContentPanel() {
        return contentPanel;
    }
}
