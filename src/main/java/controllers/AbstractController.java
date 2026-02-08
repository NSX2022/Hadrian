package controllers;

import jexer.TLabel;
import jexer.TWidget;
import jexer.TWindow;
import models.Screens;
import utils.Logging;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class AbstractController {
    protected TWindow root;
    public Screens screen;
    protected TLabel notifLabel;
    
    public AbstractController(TWindow root, Screens screen) {
        this.root = root;
        this.screen = screen;
        
        show(root);
        
        new TLabel(
                root,
                "# " + screen.name(),
                2, 1
        );
        
        notifLabel = root.addLabel(
                "",
                0,  // changed later
                root.getScreen().getHeight() / 2 - 1
        );
    }
    
    protected abstract void show(TWindow root);
    
    public void hide() {
        for (TWidget child : new ArrayList<>(root.getChildren()))
            child.remove();
    }
    
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
