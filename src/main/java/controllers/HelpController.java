package controllers;

import app.App;
import jexer.TAction;
import jexer.TButton;
import jexer.TWindow;
import jexer.backend.Screen;
import models.Screens;

import java.io.UnsupportedEncodingException;

public class HelpController extends AbstractController {
    public HelpController(TWindow root) {
        super(root, Screens.HELP);
    }
    
    @Override
    protected void show(TWindow root) {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        new TButton(
                root,
                "< Back",
                menuX, menuY,
                new TAction() {
                    @Override
                    public void DO() {
                        try {
                            App.changeScreen(HelpController.this, Screens.HOME);
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        
        // TODO add page elements
    }
}
