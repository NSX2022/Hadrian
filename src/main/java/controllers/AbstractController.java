package controllers;

import jexer.TLabel;
import jexer.TWidget;
import jexer.TWindow;
import models.Screens;

import java.util.ArrayList;

public abstract class AbstractController {
    protected TWindow root;
    public Screens screen;
    
    public AbstractController(TWindow root, Screens screen) {
        this.root = root;
        this.screen = screen;
        
        show(root);
        
        new TLabel(
                root,
                "# " + screen.name(),
                2, 1
        );
    }
    
    protected abstract void show(TWindow root);
    
    public void hide() {
        for (TWidget child : new ArrayList<>(root.getChildren()))
            child.remove();
    }
}
