package app;

import controllers.AbstractController;
import controllers.HelpController;
import controllers.HomeController;
import controllers.MessageController;
import jexer.TApplication;
import jexer.TWindow;
import models.Screens;
import models.User;
import utils.Logging;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class App extends TApplication {
    private static TWindow root;
    private static User user;
    
    public App() throws UnsupportedEncodingException {
        super(BackendType.XTERM);
        
        root = addWindow(
                "Hadrian",
                0, 0,
                getScreen().getWidth(), getScreen().getHeight(),
                TWindow.NOCLOSEBOX | TWindow.NOZOOMBOX | TWindow.OVERRIDEMENU
        );
        
        new HomeController(root);
        
        try {
            user = new User(InetAddress.getLocalHost().getHostAddress(), "someUsername");
        } catch (UnknownHostException e) {
            Logging.log("Failed To Get User IP Address", Level.SEVERE, e);
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        Config conf = new Config();
        conf.readConfig();
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
            case MESSAGES -> new MessageController(root);
            case HELP -> new HelpController(root);
            default -> throw new IllegalArgumentException("Unknown screen type:" + toScreen);
        };
    }
    
    public static User getUser() {
        return user;
    }
}
