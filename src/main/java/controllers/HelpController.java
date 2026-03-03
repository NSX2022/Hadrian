package controllers;

import app.App;
import models.Screens;

import javax.swing.*;

/**
 * Class representing entire help page in the Hadrian application.
 */
public class HelpController extends AbstractController {
    private JPanel contentPanel;
    private JButton backButton;
    
    public HelpController(JFrame appFrame) {
        super(appFrame, Screens.HELP);
        
        backButton.addActionListener(e -> App.changeScreen(Screens.HOME));
        
        bindKey("ESCAPE", "back", () -> App.changeScreen(Screens.HOME));
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
}
