package controllers;

import app.App;
import models.Screens;

import javax.swing.*;

public class HelpController extends AbstractController {
    private JPanel contentPanel;
    private JButton backButton;
    private JList<String> helpMessagesList;
    
    public HelpController(JFrame appFrame) {
        super(appFrame, Screens.HELP);
        
        backButton.addActionListener(actionEvent -> App.changeScreen(Screens.HOME));
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
}
