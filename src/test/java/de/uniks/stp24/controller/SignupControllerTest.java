package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.service.LoginService;
import de.uniks.stp24.service.TimerService;
import de.uniks.stp24.service.UserService;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.HttpException;
import retrofit2.Response;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class SignupControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    UserService userService = spy(UserService.class);
    @Spy
    LoginService loginService = spy(LoginService.class);
    @Spy
    TimerService timerService = spy(TimerService.class);
    @InjectMocks
    SignupController signupController;

    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.show(signupController);
    }

    @Test
    void signUp() {
        doReturn(Observable.just(new UserDto(LocalDateTime.now(),LocalDateTime.now(),"3","4", "5"))).when(userService).signup(any(),any());
        doReturn(Observable.just(new LoginResult("1", "a", null))).when(loginService).login(any(), any(), anyBoolean());
        doReturn(null).when(app).show("/login");

        //Title: A user wants to sign up
        //Start: A user is in the Sign up window. He must enter his name and his password. The password must be entered twice.
        assertEquals("Sign up",stage.getTitle());

        //Action: He enters his name and his password. Then he pushes the Sign up button.
        clickOn("#nameInput");
        write("myFirstname\t");
        write("myLastname123456\t");
        write("myLastname123456");
        clickOn("#signUpButton");

        waitForFxEvents();

        //Result: The user has successfully created an account and is now back in the Log in window.
        verify(userService, times(1)).signup("myFirstname","myLastname123456");
        verify(app, times(1)).show("/login");
        verify(app, times(1)).show("/lobby");
        assertEquals("Lobby",stage.getTitle());
    }

    @Test
    void signUpWithEmptyName() {
        //Title: A user wants to sign up
        //Start: A user is in the Sign up window. He must enter his name and his password. The password must be entered twice.
        assertEquals("Sign up",stage.getTitle());

        //Action: He enters his name and his password. Then he pushes the Sign up button.
        clickOn("#nameInput");
        write("\t");
        write("myLastname123456\t");
        write("myLastname123456");
        clickOn("#signUpButton");

        //Result: The user has successfully created an account and is now back in the Log in window.
        verify(userService, never()).signup(any(),any());
        assertEquals("Sign up",stage.getTitle());
        assertEquals("The name must be set!",signupController.errorLabel.getText());
    }

    @Test
    void signUpWithUnmatchingPasswords() {
        //Title: A user wants to sign up
        //Start: A user is in the Sign up window. He must enter his name and his password. The password must be entered twice.
        assertEquals("Sign up",stage.getTitle());

        //Action: He enters his name and his password. Then he pushes the Sign up button.
        clickOn("#nameInput");
        write("myFirstname\t");
        write("myLastname123456\t");
        write("myLastname1234567");
        clickOn("#signUpButton");

        //Result: The user has successfully created an account and is now back in the Log in window.
        verify(userService, never()).signup(any(),any());
        assertEquals("Sign up",stage.getTitle());
        assertEquals("Passwords do not match",signupController.errorLabel.getText());
    }
    @Test
    void signUpWithShortPassword() {
        //Title: A user wants to sign up
        //Start: A user is in the Sign up window. He must enter his name and his password. The password must be entered twice.
        assertEquals("Sign up",stage.getTitle());

        //Action: He enters his name and his password. Then he pushes the Sign up button.
        clickOn("#nameInput");
        write("myFirstname\t");
        write("my\t");
        write("my");
        clickOn("#signUpButton");

        //Result: The user has successfully created an account and is now back in the Log in window.
        verify(userService, never()).signup(any(),any());
        assertEquals("Sign up",stage.getTitle());
        assertEquals("Password must be at least 8 characters long",signupController.errorLabel.getText());
    }

    @Test
    void signUpWithExistingName() {

        doReturn(Observable.error(new HttpException(Response.error(409, new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        })))).when(userService).signup(any(),any());
        //doReturn(null).when(app).show("/login");


        //Title: A user wants to sign up
        //Start: A user is in the Sign up window. He must enter his name and his password. The password must be entered twice.
        assertEquals("Sign up",stage.getTitle());

        //Action: He enters his name and his password. Then he pushes the Sign up button.
        clickOn("#nameInput");
        write("myFirstname\t");
        write("myLastname123456\t");
        write("myLastname123456");
        clickOn("#signUpButton");


        //Result: The user has to choose a different name and still is in the Sign up window.
        verify(userService, times(1)).signup("myFirstname","myLastname123456");
        assertEquals("Sign up",stage.getTitle());
        assertEquals("The name you have entered already exists. Please choose another one.",signupController.serverErrorLabel.getText());
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(resources, userService, subscriber);
        resources = null;
        signupController = null;
        userService = null;
        subscriber = null;
        super.stop();
    }

}
