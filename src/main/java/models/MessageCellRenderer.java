package models;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing a single message cell, handles displaying functionality of a Message object.
 *
 * @see Message
 * @see JPanel
 * @see ListCellRenderer
 */
public final class MessageCellRenderer extends JPanel implements ListCellRenderer<Message> {
    private final JLabel timeLabel, senderLabel, messageLabel;
    
    public MessageCellRenderer() {
        setLayout(new BorderLayout());
        
        timeLabel = new JLabel();
        senderLabel = new JLabel();
        messageLabel = new JLabel();
        
        add(senderLabel, BorderLayout.WEST);
        add(messageLabel, BorderLayout.CENTER);
        add(timeLabel, BorderLayout.EAST);
    }
    
    /**
     * Initializes the functionality for an individual list cell.
     *
     * @param list JList object being referenced
     * @param message individual message cell being rendered
     * @param index index of cell in list
     * @param isSelected true if cell is selected (clicked), false otherwise
     * @param cellHasFocus true if the cell is focused, false otherwise
     * @return the current cell renderer object
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list,
                                                  Message message, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        senderLabel.setText(message.sender().getUsername() + ": ");
        messageLabel.setText(message.text());
        timeLabel.setText(message.date().toString());
        
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            timeLabel.setVisible(true);
        } else {
            setBackground(list.getBackground());
            timeLabel.setVisible(false);
        }
        
        return this;
    }
}
