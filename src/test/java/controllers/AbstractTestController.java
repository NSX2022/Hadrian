package controllers;

import app.App;
import models.Screens;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testingClasses.ReflectionUtils;
import testingClasses.AbstractTest;

abstract class AbstractTestController<T extends AbstractController> extends AbstractTest {
    protected T controller;
    
    @BeforeAll
    static void setUp() throws NoSuchFieldException {
        ReflectionUtils.setPrivateStatic(App.class, "frame", frame);
        ReflectionUtils.setPrivateStatic(App.class, "user", user);
    }
    
    @BeforeEach
    void initController() {
        controller = createController();
    }
    
    protected abstract T createController();
    
    @Test
    protected abstract void buttons() throws Exception;
    
    @Test
    void contentPanel() {
        assert controller.getContentPanel() != null;
    }
    
    @Test
    void title() throws NoSuchFieldException {
        Screens screen = (Screens) ReflectionUtils.getPrivateInstance(controller, "screen");
        String title = frame.getTitle();
        assert title.contains(screen.name()) && title.contains("Hadrian");
    }
}
