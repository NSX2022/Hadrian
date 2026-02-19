package controllers;

import app.App;
import jexer.TAction;
import jexer.TWindow;
import jexer.backend.Screen;
import models.Screens;
import utils.Logging;

import java.util.logging.Level;

public class HelpController extends AbstractController {
    public HelpController(TWindow root) {
        super(root, Screens.HELP);
    }
    
    @Override
    protected void show() {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        root.addButton(
                "< Back",
                menuX, menuY,
                new TAction() {
                    @Override
                    public void DO() {
                        App.changeScreen(HelpController.this, Screens.HOME);
                        Logging.log("Returning To Home Page", Level.INFO);
                    }
                }
        );
        
        // TODO add page elements
    }
}
