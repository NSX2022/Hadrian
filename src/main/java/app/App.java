package app;

import config.Config;
import controllers.AbstractController;
import controllers.HelpController;
import controllers.HomeController;
import jexer.TApplication;
import jexer.TWindow;
import models.Screens;
import utils.Logging;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

public class App extends TApplication {
    private static TWindow root;
    
    public App() throws UnsupportedEncodingException {
        super(BackendType.XTERM);
        
        root = addWindow(
                "Hadrian",
                0, 0,
                getScreen().getWidth(), getScreen().getHeight(),
                TWindow.NOCLOSEBOX | TWindow.NOZOOMBOX | TWindow.OVERRIDEMENU
        );
        
        new HomeController(root);
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        Config conf = new Config();
        try {
            conf.readConfig();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(401);
        }
        new App().run();
    }
    
    public static void changeScreen(AbstractController current, Screens toScreen) {
        if (current.screen == toScreen) {
            String message = "Already on page: " + current.screen.name();
            Logging.log(message, Level.INFO);
            current.displayNotif(message);
            return;
        }
        
        current.hide();
        
        AbstractController controller = switch (toScreen) {
            case HOME -> new HomeController(root);
            //case MESSAGES -> new MessageController(root, getCurrentUser());
            case HELP -> new HelpController(root);
            default -> throw new IllegalArgumentException("Unknown screen type:" + toScreen);
        };
    }
}
