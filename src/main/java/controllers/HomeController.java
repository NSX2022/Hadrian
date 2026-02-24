package controllers;

import app.App;
import models.Screens;

import javax.swing.*;

/**
 * Controller for the initial "Home" page in Hadrian application.
 */
public class HomeController extends AbstractController {
    private JPanel contentPanel;
    private JButton chatsButton;
    private JButton helpButton;
    private JButton exitButton;
    
    public HomeController(JFrame appFrame) {
        super(appFrame, Screens.HOME);
        
        chatsButton.addActionListener(actionEvent -> App.changeScreen(Screens.CHATS));
        helpButton.addActionListener(actionEvent -> App.changeScreen(Screens.HELP));
        exitButton.addActionListener(actionEvent -> System.exit(0));
    }
    
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
}
