package controllers;

import app.App;
import models.Screens;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Abstract base classes for shared controller logic.
 * This controller should only ever be the parent of controllers.
 * <p>
 * This abstract class is used to enforce consistent structure
 * across controllers in the Hadrian application - it provides
 * required methods, common functionality, or reusable logic for
 * concrete page controllers.
 */
public abstract class AbstractController extends JPanel {
    protected final Screens screen;
    private final JFrame appFrame;
    
    public AbstractController(JFrame appFrame, Screens screen) {
        this.appFrame = appFrame;
        this.screen = screen;
        
        this.appFrame.setTitle("Hadrian | " + screen.name());
        
        bindKey("control Q", "quit", () -> System.exit(0));
        bindKey("control H", "home", () -> App.changeScreen(Screens.HOME));
        bindKey("control M", "messages", () -> App.changeScreen(Screens.CHATS));
    }
    
    public void init() {
        add(getContentPanel());
        this.appFrame.setContentPane(this);
    }
    
    protected abstract JPanel getContentPanel();
    
    protected void bindKey(String keystroke, String actionName, Runnable action) {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(keystroke), actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

//    /**
//     * Displays a notification message in the center of
//     * the user's screen for two seconds, then disappears.
//     * <p>
//     * Method actions run in its own thread,
//     * main program is not affected by timer.
//     *
//     * @param message message to be displayed and centered
//     */
//    public final void displayNotif(String message) {
//        new Thread(() -> {
//            notifLabel.setX((root.getScreen().getWidth() - message.length()) / 2);
//            notifLabel.setLabel(message);
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                Logging.log("Failed To Sleep In Thread", Level.SEVERE, e);
//                throw new RuntimeException(e);
//            }
//
//            notifLabel.setLabel("");
//        }).start();
//    }
}
