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
        
        JButton settingsButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "settingsButton");
        settingsButton.doClick();
        assert frame.getContentPane().getClass() == SettingsController.class;
        
        // TODO: test exit button
    }
}
