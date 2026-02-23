import javax.swing.*;

public class BookEditor extends JFrame {
    private JButton button1Button;
    private JPanel contentPane;
    private JButton button2Button;
    private JButton button3Button;
    
    public BookEditor() {
        setTitle("Book Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        
        // Set the frame location to the center of the screen
        setLocationRelativeTo(null);
        
        // Set the frame visible
        setVisible(true);
    }
}
