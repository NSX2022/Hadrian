package controllers;

import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;

import javax.swing.*;

public class HelpControllerTest extends AbstractTestController<HelpController> {
    @Override
    protected HelpController createController() {
        return new HelpController(frame);
    }
    
    @Override
    @Test
    protected void buttons() throws NoSuchFieldException {
        JButton backButton = (JButton) ReflectionUtils.getPrivateInstance(controller, "backButton");
        backButton.doClick();
        assert frame.getContentPane().getClass() == HomeController.class;
    }
}
