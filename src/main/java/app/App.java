package app;

import config.Config;
import controllers.AbstractController;
import controllers.HelpController;
import controllers.HomeController;
import controllers.MessageController;
import jexer.TApplication;
import jexer.TWindow;
import models.Screens;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

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
            conf.read_config();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(401);
        }
        new App().run();
    }
    
    public static void changeScreen(AbstractController current, Screens toScreen) throws UnsupportedEncodingException {
        if (current.screen == toScreen) {
            //log("Already on page: " + current.screen.name());
            return;
        }
        
        current.hide();
        
        AbstractController controller = switch (toScreen) {
            case HOME -> new HomeController(root);
            case MESSAGE -> new MessageController(root);
            case HELP -> new HelpController(root);
            // TODO add more cases with other pages
            default -> throw new IllegalArgumentException("Unknown screen type:" + toScreen);
        };
    }
}
