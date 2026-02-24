package app;

import controllers.*;
import models.Screens;
import models.User;
import utils.Logging;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public final class App {
    private static User user;
    private static JFrame frame;
    
    public static void main(String[] args) throws FileNotFoundException {
        Config conf = new Config();
        conf.readConfig();
        
        try {
            InetAddress host = InetAddress.getLocalHost();
            user = new User(host.getHostAddress(), host.getHostName());
        } catch (UnknownHostException e) {
            Logging.log("Failed To Get User IP Address", Level.SEVERE, e);
            throw new RuntimeException(e);
        }
        
        frame = initJFrame();
    }
    
    private static JFrame initJFrame() {
        JFrame appFrame = new JFrame("Hadrian");
        new HomeController(appFrame).init();
        
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = ge.getMaximumWindowBounds();
        double width = bounds.getWidth(), height = bounds.getHeight();
        
        appFrame.setPreferredSize(new Dimension((int) width, (int) height));
        
        appFrame.pack();
        appFrame.setLocationRelativeTo(null);  // Set the frame location to the center of the screen
        appFrame.setVisible(true);
        
        return appFrame;
    }

    public static void changeScreen(Screens screen) {
        AbstractController controller = switch (screen) {
            case HOME -> new HomeController(frame);
            case CHATS -> new ChatsController(frame);
            case HELP -> new HelpController(frame);
            default -> throw new IllegalArgumentException("Unknown screen type:" + screen);
        };
        
        controller.init();
        
        if (controller instanceof Loadable l)
            l.load(user);
        
        draw();
    }
    
    public static void draw() {
        frame.revalidate();
        frame.repaint();
    }
    
    public static User getUser() {
        return user;
    }
}
