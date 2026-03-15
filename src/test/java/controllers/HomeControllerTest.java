package controllers;

import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;

import javax.swing.*;

public class HomeControllerTest extends AbstractTestController<HomeController> {
    @Override
    protected HomeController createController() {
        return new HomeController(frame);
    }
    
    @Override
    @Test
    protected void buttons() throws NoSuchFieldException {
        JButton chatsButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "chatsButton");
        chatsButton.doClick();
        assert frame.getContentPane().getClass() == ChatsController.class;
        
        JButton helpButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "helpButton");
        helpButton.doClick();
        assert frame.getContentPane().getClass() == HelpController.class;
        
        JButton themeButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "themeButton");
        String before = UIManager.getLookAndFeel().getName();
        themeButton.doClick();
        String after = UIManager.getLookAndFeel().getName();
        assert !before.equals(after);
        
        // TODO: test exit button
    }
}
