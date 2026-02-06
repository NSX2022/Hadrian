package controllers;

import jexer.TWidget;
import jexer.TWindow;

import java.util.ArrayList;

public abstract class AbstractController {
    protected TWindow root;
    
    public AbstractController(TWindow root) {
        this.root = root;
        show(root);
    }
    
    protected abstract void show(TWindow root);
    
    public void hide() {
        for (TWidget child : new ArrayList<>(root.getChildren()))
            child.remove();
    }
}
