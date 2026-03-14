package app;

import controllers.AbstractController;
import controllers.ChatsController;
import controllers.HelpController;
import controllers.HomeController;
import models.Screens;
import models.User;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testingClasses.ReflectionUtils;
import testingClasses.AbstractTest;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class AppTest extends AbstractTest {
    @Test
    void run() throws NoSuchFieldException, IOException {
        App.run();
        
        User appUser = App.getUser();
        assert appUser.getIp().equals(user.getIp()) && appUser.getUsername().equals(user.getUsername());
        
        JFrame frame = (JFrame) ReflectionUtils.getPrivateStatic(App.class, "frame");
        assert frame.getContentPane().getClass() == HomeController.class;
    }
    
    private static Stream<Arguments> screenProvider() {
        return Stream.of(
                Arguments.of(Screens.HOME, HomeController.class),
                Arguments.of(Screens.HELP, HelpController.class),
                Arguments.of(Screens.CHATS, ChatsController.class)
        );
    }
    
    @ParameterizedTest
    @MethodSource("screenProvider")
    void changeScreen(Screens screen, Class<? extends AbstractController> pageControllerClass) throws
                                                                                               NoSuchFieldException,
                                                                                               IOException {
        ReflectionUtils.setPrivateStatic(App.class, "frame", frame);
        ReflectionUtils.setPrivateStatic(App.class, "user", user);
        
        App.changeScreen(screen);
        
        JFrame frame = (JFrame) ReflectionUtils.getPrivateStatic(App.class, "frame");
        assert frame.getContentPane().getClass() == pageControllerClass;
        
        String[] content = Files.readString(Paths.get("app.log")).split("\n\n");
        String last = content[content.length - 1];
        
        assert last.contains(screen.name());
    }
    
    @Test
    void changeScreenError() throws NoSuchFieldException {
        ReflectionUtils.setPrivateStatic(App.class, "frame", frame);
        assertThrows(IllegalArgumentException.class, () -> App.changeScreen(Screens.valueOf("FAKE_ENUM")));
    }
    
    @Test
    void toggleDarkMode() throws NoSuchFieldException, NoSuchMethodException {
        ReflectionUtils.invokePrivateStatic(App.class, "initJFrame", new Class[0]);
        ReflectionUtils.setPrivateStatic(App.class, "frame", frame);
        
        String before = UIManager.getLookAndFeel().getName();
        App.toggleDarkMode();
        String after = UIManager.getLookAndFeel().getName();
        
        assert !before.equals(after);
    }
    
    @Test
    void getUser() throws IOException {
        App.run();
        assert App.getUser() != null;
    }
}
