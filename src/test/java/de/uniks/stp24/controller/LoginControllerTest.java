package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.service.LoginService;
import de.uniks.stp24.service.TimerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.HTTP_401;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy //behaves like service, but you can overwrite individual methods (can define behavior in test)
    LoginService loginService = spy(LoginService.class);
    Provider<ResourceBundle> newResources = mock(Provider.class);
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));
    @Spy
    TimerService timerService = spy(TimerService.class);
    @InjectMocks //what you really need to test
    LoginController loginController;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage); //start from ControllerTest
        app.show(loginController); //build start situation
    }

    @Test
    void login() {
        //Title: A user wants to log in
        doReturn(Observable.just(new LoginResult("1", "a", null))).when(loginService).login(any(), any(), anyBoolean()); //always returns a successful login result
        doReturn(null).when(app).show("/lobby");

        //Start:
        //A user is in the Login window.
        //He must enter his name and password.
        //He also must select a language, otherwise it will be English.
        assertEquals("Log in", stage.getTitle());
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Assertions.assertEquals("English",languageComboBox.getValue());

        //Action:
        //The user enters his name and the password. Then he pushes the Log in button.
        clickOn("#nameInput");
        write("alice123\t");
        write("hello456");
        clickOn("#loginButton");

        waitForFxEvents(); //time delay

        //Result: The user has successfully logged in and is now in the Lobby window.
        verify(loginService, times(1)).login("alice123", "hello456", false); //check only whether loginService was called
        verify(app, times(1)).show("/lobby");
    }

    @Test
    void loginWithError(){
        Label errorLabel = (Label) lookup("#errorLabel").query();
        doReturn(Observable.error(new Exception(HTTP_401))).when(loginService).login(any(), any(), anyBoolean());

        //Title: A user wants to log in, but he enters an incorrect password or an incorrect name
        //Start: A user is in the Login window. He needs to enter his name and password to log in.
        //He also must select a language, otherwise it will be English.
        assertEquals("Log in", stage.getTitle());
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Assertions.assertEquals("English",languageComboBox.getValue());

        //Action: The user enters his name and his password. Then he pushes the Log in button.
        clickOn("#nameInput");
        write("alice123\t");
        write("hello456");
        clickOn("#loginButton");

        waitForFxEvents();

        //Result: The user has entered an incorrect password or an incorrect name.
        // It is also possible that the user does not exist.
        // He is still in the Login window.
        // The user must enter his password and his name once again.
        assertEquals("The name or the password is incorrect. Please try again!",errorLabel.getText());
        verify(loginService, times(1)).login("alice123", "hello456", false);
    }

    @Test
    void loginWithRememberMe(){
        //Title: A user wants to log in and the app should save the information
        doReturn(Observable.just(new LoginResult("1", "a", null))).when(loginService).login(any(), any(), anyBoolean());
        doReturn(null).when(app).show("/lobby");

        //Start:
        //A user is in the Log in window.
        //He must enter his name and password to log in.
        //He also must select a language, otherwise it will be English.
        assertEquals("Log in", stage.getTitle());
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Assertions.assertEquals("English",languageComboBox.getValue());

        //Action:
        //The user enters his name and the password. Then he pushes the Remember me and the Log in button.
        clickOn("#nameInput");
        write("alice123\t");
        write("hello456");
        clickOn("#rememberMe");
        clickOn("#loginButton");

        waitForFxEvents(); //time delay

        //restart application
        app.autoRefresher();

        //Result: The user has successfully logged in and is now in the Lobby window.
        //The user data is saved for the next time.
        verify(loginService, times(1)).login("alice123", "hello456", true);
        verify(app, times(1)).show("/lobby");
    }

    @Test
    void loginWithRememberMeAndError(){
        //Title: A user wants to log in, but he enters an incorrect password or an incorrect name
        Label errorLabel = (Label) lookup("#errorLabel").query();
        doReturn(Observable.error(new Exception(HTTP_401))).when(loginService).login(any(), any(), anyBoolean());

        //Start: A user is in the Log in window.
        //He needs to enter his name and password to log in.
        //He also must select a language, otherwise it will be English.
        assertEquals("Log in", stage.getTitle());
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Assertions.assertEquals("English",languageComboBox.getValue());

        //Action: The user enters his name and his password.
        // Then he pushes the Log in and Remember me buttons.
        clickOn("#nameInput");
        write("alice123\t");
        write("hello456");
        clickOn("#rememberMe");
        clickOn("#loginButton");

        waitForFxEvents(); //time delay

        //Result: The user has entered an incorrect password or an incorrect name.
        //It is also possible that the user does not exist.
        //He is still in the Log in window.
        //The user must enter his password and his name once again.
        assertEquals("The name or the password is incorrect. Please try again!",errorLabel.getText());
        verify(loginService, times(1)).login("alice123", "hello456", true);
        verify(app, never()).show("/lobby");
    }

    @Test
    void loginWithLanguageChange() {
        //Title: A user wants to log in and change language
        doReturn(Observable.just(new LoginResult("1", "a", null))).when(loginService).login(any(), any(), anyBoolean()); //always returns a successful login result
        doReturn(null).when(app).show("/lobby");

        //Start:
        //A user is in the Login window.
        //He must enter his name and password.
        //He wants to select the language to German.
        assertEquals("Log in", stage.getTitle());
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Assertions.assertEquals("English",languageComboBox.getValue());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.GERMAN);
        when(newResources.get()).thenReturn(resourceBundle);

        //Action:
        //The user enters his name and the password.
        //Then he changes the Language to German.
        //Then he pushes the Log in button.
        clickOn("#nameInput");
        write("alice123\t");
        write("hello456");
        clickOn("#languageComboBox");
        clickOn("German");
        waitForFxEvents();
        clickOn("#loginButton");
        waitForFxEvents();

        //Result: The user has successfully changed the language to German and logged in and is now in the Lobby window.
        verify(prefService, times(1)).setLocale(Locale.GERMAN);
        Assertions.assertEquals("German", languageComboBox.getValue());
        verify(loginService, times(1)).login("alice123", "hello456", false); //check only whether loginService was called
        verify(app, times(1)).show("/lobby");
        prefService.setLocale(Locale.ENGLISH);
    }

    @Test
    void moveToSignup() {
        //Title: A user wants to go to the Sign up window
        //Start: A user is in the Log in window. He wants to sign up.
        assertEquals("Log in",stage.getTitle());

        //Action: He pushes the Sign up button.
        clickOn("#signupButton");

        //Result: The user is now in the Sign up window.
        assertEquals("Sign up",stage.getTitle());
    }

    @Test
    void moveToLicenses() {
        //Title: A user wants to go to the Licenses window
        //Start: A user is in the Log in window. He wants to see the licenses.
        assertEquals("Log in",stage.getTitle());

        //Action: He pushes the Licenses button.
        clickOn("#licencesButton");

        //Result: The user is now in the Licenses window.
        assertEquals("Licenses",stage.getTitle());
    }

    @Override
    public void stop() throws Exception {
        Mockito.reset(resources, loginService, subscriber, newResources, timerService);
        resources = null;
        loginController = null;
        loginService = null;
        subscriber = null;
        newResources = null;
        timerService = null;
        super.stop();
    }


}
