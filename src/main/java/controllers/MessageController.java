package controllers;

import jexer.TWindow;
import jexer.backend.Screen;
import models.Screens;

import java.io.UnsupportedEncodingException;

public class MessageController extends AbstractController {
    public MessageController(TWindow root) {
        super(root, Screens.MESSAGE);
    }
    
    @Override
    protected void show(TWindow root) {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        // TODO add page elements
    }
}
