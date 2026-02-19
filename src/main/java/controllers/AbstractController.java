package controllers;

import app.App;
import jexer.TLabel;
import jexer.TWidget;
import jexer.TWindow;
import models.Screens;
import utils.Logging;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Abstract base classes for shared controller logic.
 * This controller should only ever be the parent of controllers.
 * <p>
 * This abstract class is used to enforce consistent structure
 * across controllers in the Hadrian application - it provides
 * required methods, common functionality, or reusable logic for
 * concrete page controllers.
 */
public abstract class AbstractController {
    protected TWindow root;
    public Screens screen;
    protected TLabel notifLabel;
    
    public AbstractController(TWindow root, Screens screen) {
        this.root = root;
        this.screen = screen;
        
        root.addLabel(
                "# " + screen.name(),
                2, 1
        );
        
        notifLabel = root.addLabel(
                "",
                0,  // changed later
                root.getScreen().getHeight() / 2 - 1
        );
        
        if (this instanceof Loadable l)
            l.load(App.getUser());
        
        show();
    }
    
    /**
     * Abstract method required for every controller class
     * <p>
     * Creates the entire UI for each page, excluding certain elements required for
     * all pages that are already created in {@link #AbstractController(TWindow, Screens)}
     */
    protected abstract void show();
    
    /**
     * Removes all page elements from {@code root}, essentially closing the page,
     * so a new one to be displayed in its place.
     */
    public void hide() {
        for (TWidget child : new ArrayList<>(root.getChildren()))
            child.remove();
    }
    
    /**
     * Displays a notification message in the center of
     * the user's screen for two seconds, then disappears.
     * <p>
     * Method actions run in its own thread,
     * main program is not affected by timer.
     *
     * @param message message to be displayed and centered
     */
    public final void displayNotif(String message) {
        new Thread(() -> {
            notifLabel.setX((root.getScreen().getWidth() - message.length()) / 2);
            notifLabel.setLabel(message);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Logging.log("Failed To Sleep In Thread", Level.SEVERE, e);
                throw new RuntimeException(e);
            }
            
            notifLabel.setLabel("");
        }).start();
    }
}
