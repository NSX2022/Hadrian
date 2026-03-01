package controllers;

import models.Message;

import javax.swing.*;
import java.awt.*;

public class MessageCellRenderer extends JPanel implements ListCellRenderer<Message> {
    private final JLabel senderLabel, messageLabel;
    
    public MessageCellRenderer() {
        setLayout(new BorderLayout());
        
        senderLabel = new JLabel();
        messageLabel = new JLabel();
        
        add(senderLabel, BorderLayout.WEST);
        add(messageLabel, BorderLayout.CENTER);
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list,
                                                  Message message, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        senderLabel.setText(message.sender().getUsername() + ": ");
        messageLabel.setText(message.text());
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
        }
        
        return this;
    }
}
