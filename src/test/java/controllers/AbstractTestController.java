package controllers;

import app.App;
import models.User;
import testingClasses.ReflectionUtils;

import javax.swing.*;

abstract class AbstractTestController<T extends AbstractController> {
    protected final String TEST_MESSAGE = "Message Test";
    protected T controller;
    protected User user;
    protected JFrame frame;
    
    public AbstractTestController() throws NoSuchFieldException, IllegalAccessException {
        user = new User(null, null);
        frame = new JFrame();
        
        ReflectionUtils.setPrivate(App.class, "frame", frame);
    }
}
