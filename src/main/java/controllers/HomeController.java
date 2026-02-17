package controllers;

import app.App;
import jexer.*;
import jexer.backend.Screen;
import models.Screens;
import utils.Logging;

import java.util.logging.Level;

/**
 * Controller for the initial "Home" page in Hadrian application.
 */
public class HomeController extends AbstractController {
    private TText logArea;
    
    public HomeController(TWindow root) {
        super(root, Screens.HOME);
        show();
    }
    
    @Override
    protected void show() {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        // TODO make window black + further customize theme
        
        logArea = root.addText(
                "Logger Initialized... ",
                width / 2 - 2,
                height / 2,
                width / 2,
                height / 2
        );
        logArea.setLineSpacing(0);
        logArea.rightJustify();
        
        // TODO customize button themes
        
        root.addButton(
                "> Toggle Server",
                menuX, menuY,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Toggle Server Button Selected");
                        // TODO server functionality
                    }
                }
        );
        
        root.addButton(
                "> Messages",
                menuX, menuY + 4,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Messages Button Selected");
                        changeScreen(Screens.MESSAGES);
                        log("Switched To Messages Screen");
                    }
                }
        );
        
        root.addButton(
                "> Help",
                menuX, menuY + 8,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Help Button Selected");
                        changeScreen(Screens.HELP);
                        log("Switched To Help Screen");
                    }
                }
        );
        
        root.addButton(
                "< Exit",
                menuX, menuY + 12,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Program Exited\n");
                        root.getApplication().exit();
                    }
                }
        );
    }
    
    /**
     * Wrapper for screen changing functionality that uses the same first parameter.
     *
     * @param screen screen to change to - from enum list of pages
     * @see Screens
     * @see App#changeScreen(AbstractController, Screens)
     *
     */
    private void changeScreen(Screens screen) {
        App.changeScreen(HomeController.this, screen);
    }
    
    private void log(String message) {
        logArea.addLine(message + " ");
        Logging.log(message, Level.INFO);
    }
}
