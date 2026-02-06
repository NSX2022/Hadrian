package app;

import controllers.AbstractController;
import controllers.HomeController;
import controllers.MessageController;
import jexer.TApplication;
import jexer.TWindow;
import models.Screens;

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
        new App().run();
    }
    
    public static void changeScreen(AbstractController current, Screens toScreen) throws UnsupportedEncodingException {
        current.hide();
        
        AbstractController controller = switch (toScreen) {
            case HOME -> new HomeController(root);
            case MESSAGE -> new MessageController(root);
            // TODO add more cases with other pages
            default -> throw new IllegalArgumentException("Unknown screen type:" + toScreen);
        };
    }
}
