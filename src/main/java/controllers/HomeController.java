package controllers;

import app.App;
import models.Screens;

import javax.swing.*;

/**
 * Controller for the initial home page in the Hadrian application.
 */
public class HomeController extends AbstractController {
    private JPanel contentPanel;
    private JButton chatsButton;
    private JButton helpButton;
    private JButton exitButton;
    private JButton settingsButton;
    
    public HomeController(JFrame appFrame) {
        super(appFrame, Screens.HOME);
        
        chatsButton.addActionListener(e -> App.changeScreen(Screens.CHATS));
        helpButton.addActionListener(e -> App.changeScreen(Screens.HELP));
        settingsButton.addActionListener(e -> App.changeScreen(Screens.SETTINGS));
        exitButton.addActionListener(e -> System.exit(0));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected JPanel getContentPanel() {
        return contentPanel;
    }
}
