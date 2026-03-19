import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class HotkeyExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotkey Demo");
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        
        Action saveAction = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Ctrl+S pressed! Saving...");
            }
        };
        
        KeyStroke saveStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        String actionKey = "saveAction";
        
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveStroke, actionKey);
        panel.getActionMap().put(actionKey, saveAction);
        
        JButton saveButton = new JButton(saveAction);
        panel.add(saveButton);
        
        frame.setVisible(true);
    }
}
