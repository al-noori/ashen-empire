package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.service.EditUserService;
import io.reactivex.rxjava3.core.Observable;
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
import org.testfx.matcher.base.NodeMatchers;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.HTTP_401;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class AccountDeletionConfirmationControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    EditUserService editUserService = mock(EditUserService.class);
    @InjectMocks
    AccountDeletionConfirmationController accountDeletionConfirmationController;
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.show(accountDeletionConfirmationController);
    }

    @Test
    void deleteAccountPasswordError(){
        Label errorLabel = (Label) lookup("#errorLabel").query();
        doReturn(Observable.error(new Exception(HTTP_401))).when(editUserService).checkPassword(any());

        //Title: A user wants to delete his account
        //Start: A user is in the Delete account window. He must enter his password.
        Assertions.assertEquals("Delete your account",stage.getTitle());

        //Action: He enters his password and pushes the Delete account button.
        clickOn("#confPasswordInput");
        write("wrongPassword");
        clickOn("#deleteButton");

        //Result: The user receives an error message that the password is incorrect.
        Assertions.assertEquals("The password is incorrect. Please try again!",errorLabel.getText());
        verify(editUserService, times(1)).checkPassword("wrongPassword");
    }

    @Test
    void deleteAccountSuccess(){
        doReturn(Observable.just(new LoginResult("id","a","r"))).when(editUserService).checkPassword(any());
        doReturn(Observable.just(new UserDto(LocalDateTime.now(),LocalDateTime.now(),"3","4", "5"))).when(editUserService).delete();
        doReturn(null).when(app).show("/login");

        //Title: A user wants to delete his account
        //Start: A user is in the Delete account window. He must enter his password.
        Assertions.assertEquals("Delete your account",stage.getTitle());

        //Action: He enters his password and pushes the Delete account button.
        clickOn("#confPasswordInput");
        write("correctPassword");
        clickOn("#deleteButton");
        waitForFxEvents();
        //Result: The user has successfully deleted his account and is now back in the Log in window.

        verifyThat(".alert", NodeMatchers.isNotNull());
        verifyThat(".alert", NodeMatchers.isVisible());
        clickOn("OK");
        verify(editUserService, times(1)).checkPassword("correctPassword");
        verify(editUserService, times(1)).delete();
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, editUserService, resources);
        subscriber = null;
        editUserService = null;
        resources = null;
        accountDeletionConfirmationController = null;
        super.stop();
    }
}
