import javax.swing.ImageIcon;
import javax.swing.JFrame;

import jexer.TAction;
import jexer.TApplication;
import jexer.TButton;
import jexer.TWindow;
import jexer.backend.SwingTerminal;

public class MySwingApplication extends TApplication {
    
    public MySwingApplication() throws Exception {
        super(BackendType.SWING, 60, 14, 28);
        
        // Create standard menus for Tool, File, and Window.
        addToolMenu();
        addFileMenu();
        addWindowMenu();
        
        // Add a window with a single button to launch tests.
        TWindow window = new TWindow(this, "Test SwingTerminal", 40, 8,
                                     TWindow.CENTERED);
        
        final TApplication app = this;
        TButton button = window.addButton("Run Tests", 14, 2, new TAction() {
            public void DO() {
                SwingTerminal terminal = (SwingTerminal) app.getScreen();
                terminal.runPerformanceTests(System.err);
            }
        });
        
        
    }
    
    public static void main(String [] args) throws Exception {
        MySwingApplication app = new MySwingApplication();
        
        if (args.length > 0) {
            String filename = args[0];
            if (app.getScreen() instanceof SwingTerminal) {
                SwingTerminal terminal = (SwingTerminal) app.getScreen();
                
                JFrame frame = terminal.getSwingComponent().getFrame();
                if (frame != null) {
                    frame.setIconImage((new ImageIcon(filename)).getImage());
                }
            }
        }
        
        app.run();
    }
}
