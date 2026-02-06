package controllers;

import app.App;
import jexer.*;
import jexer.backend.Screen;
import models.Screens;

import java.io.UnsupportedEncodingException;

public class HomeController extends AbstractController {
    private TText logArea;
    
    public HomeController(TWindow root) {
        super(root, Screens.HOME);
    }
    
    @Override
    protected void show(TWindow root) {
        int menuX = 4, menuY = 5;
        Screen screen = root.getScreen();
        int width = screen.getWidth(), height = screen.getHeight();
        
        // TODO make window black + further customize theme
        
        logArea = new TText(
                root,
                "Logger Initialized... ",
                width / 2 - 2,
                height / 2,
                width / 2,
                height / 2
        );
        logArea.setLineSpacing(0);
        logArea.rightJustify();
        
        // TODO customize button themes
        
        new TButton(
                root,
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
        
        new TButton(
                root,
                "> Message",
                menuX, menuY + 4,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Message Button Selected");
                        changeScreen(Screens.MESSAGE);
                        log("Switched To Message Screen");
                    }
                }
        );
        
        new TButton(
                root,
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
        
        new TButton(
                root,
                "> Exit",
                menuX, menuY + 12,
                new TAction() {
                    @Override
                    public void DO() {
                        log("Program Exited");
                        root.getApplication().exit();
                    }
                }
        );
    }
    
    private void changeScreen(Screens screen) {
        try {
            App.changeScreen(HomeController.this, screen);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void log(String message) {
        logArea.addLine(message + " ");
    }
}
