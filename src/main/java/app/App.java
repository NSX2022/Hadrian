package app;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
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

/**
 * Instantiate and run the App class to start the Hadrian application
 *
 * @see #run()
 */
public final class App {
    private static User user;
    private static JFrame frame;
    private static boolean isDarkMode;
    
    public static void main(String[] args) {
        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Entry point for Hadrian application
     * <p>
     * Initializes the main application window, data loading, and setup process
     *
     * @throws FileNotFoundException if config file cannot be found
     */
    public static void run() throws FileNotFoundException, UnknownHostException {
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
    
    /**
     * Creates the home page of the application
     * <p>
     * Organizational method to initialize application JFrame - meant to reduce constructor length
     * <p>
     * Sets JFrame size to be the maximum window size based on graphics environment.
     *
     * @return created and fully initialized JFrame
     * @see HomeController
     * @see JFrame
     * @see GraphicsEnvironment
     */
    private static JFrame initJFrame() {
        toggleDarkMode();
        UIManager.put("defaultFont", new Font("Hack FC Ligatured", Font.PLAIN, 30));
        
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
    
    /**
     * Change application page to desired screen using an enum.
     * <p>
     * Creates page, initializes page, loads user data, redraws page, in sequence.
     *
     * @param screen screen to change to as an enum
     * @see Screens
     * @see AbstractController#init()
     * @see Loadable#load(User)
     * @see #draw()
     */
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
        
        Logging.log("Switched To Page: " + screen.name(), Level.INFO);
    }
    
    /**
     * "Redraws" the application JFrame object.
     * <p>
     * Displays any changes made since the last draw.
     */
    public static void draw() {
        frame.revalidate();
        frame.repaint();
    }
    
    public static void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        
        if (isDarkMode)
            FlatDarkLaf.setup();  // Flat-Laf Dark Application Theme
        else
            FlatLightLaf.setup();  // Flat-Laf Light Application Theme
    }
    
    public static User getUser() {
        return user;
    }
}
