package controllers;

import app.App;
import models.Screens;
import utils.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

/**
 * Abstract base classes for shared controller logic.
 * This controller should only ever be the parent of controllers.
 * <p>
 * This abstract class is used to enforce consistent structure
 * across controllers in the Hadrian application - it provides
 * required methods, common functionality, or reusable logic for
 * concrete page controllers.
 *
 * @see controllers
 */
public abstract class AbstractController extends JPanel {
    protected final Screens screen;
    private final JFrame appFrame;
    
    public AbstractController(JFrame appFrame, Screens screen) {
        this.appFrame = appFrame;
        this.screen = screen;
        
        this.appFrame.setTitle("Hadrian | " + screen.name());
        
        setUniversalKeybinds();
    }
    
    /**
     * Define keybinds that will work universally throughout the application.
     *
     * @see #bindKey(String, String, Runnable)
     */
    private void setUniversalKeybinds() {
        bindKey("control Q", "quit", () -> System.exit(0));
        bindKey("control H", "home", () -> App.changeScreen(Screens.HOME));
        bindKey("control M", "messages", () -> App.changeScreen(Screens.CHATS));
    }
    
    /**
     * Initialize controller layout and content, sets the application window's
     * contentPane as the current controller JPanel, essentially switching pages.
     * <p>
     * This is separate from constructor because Java Swing .form files are
     * initialized after constructor finishes.
     */
    public void init() {
        setLayout(new BorderLayout());
        add(getContentPanel());
        appFrame.setContentPane(this);
    }
    
    /**
     * Gets the contentPanel from the child class, used during the initialization process in this parent class.
     * <p>
     * This exists because Java Swing content panels are initialized after constructors finish.
     *
     * @return JPanel object representing the entire controller page, added to application window
     * @see #init()
     */
    protected abstract JPanel getContentPanel();
    
    /**
     * Sets a keybind for the current controller.
     *
     * @param keystroke key or key pattern to trigger an action
     * @param actionName a unique name for this keystroke and action process
     * @param action an action to run when the keybind is used
     */
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
    
    /**
     * Displays a notification message in the center bottom of
     * the user's screen for five seconds, then disappears.
     * <p>
     * Method actions run in its own thread,
     * main program is not affected by timer.
     *
     * @param message message to be displayed and centered
     */
    public void displayNotif(String message) {
        new Thread(() -> {
            JLabel label = new JLabel(message, SwingConstants.CENTER);
            appFrame.add(label, BorderLayout.SOUTH);
            App.draw();
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logging.log("Failed To Sleep In Thread", Level.SEVERE, e);
                throw new RuntimeException(e);
            } finally {
                Logging.log(message, Level.INFO);
            }
            
            appFrame.remove(label);
            App.draw();
        }).start();
    }
}
