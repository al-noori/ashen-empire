package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.service.EditUserService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Provider;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.HTTP_409;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


@ExtendWith(MockitoExtension.class)
public class EditUserControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    EditUserService editUserService = spy(EditUserService.class);
    Provider<ResourceBundle> newResources = mock(Provider.class);
    @InjectMocks
    EditUserController editUserController;
    @Spy
    @Resource
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        doReturn(Observable.just(new UserDto(LocalDateTime.now(),LocalDateTime.now(),"3","Nicole","5"))).when(editUserService).getUser();
        app.show(editUserController);
    }

    @Test
    void editUserNameSuccess() {
        Label errorLabel = (Label) lookup("#errorLabel").query();
        Label nameLabel = (Label) lookup("#nameLabel").query();
        doReturn(Observable.just(new UserDto(LocalDateTime.now(),LocalDateTime.now(),"3","Mila",null))).when(editUserService).update(any(),any());

        //Start: A user is in the User administration window. He wants to edit his name.
        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals("Nicole", nameLabel.getText());


        //Action: He changes his name to Mila.
        clickOn("#nameInput");
        write("Mila");
        clickOn("#saveButton");
        waitForFxEvents();

        //Result: The user has successfully changed his name to Mila.
        Assertions.assertEquals("Mila",nameLabel.getText());
        Assertions.assertEquals("Your name, password and language are successfully updated",
              errorLabel.getText());
        verify(editUserService, times(1)).update("Mila",null);
    }

    @Test
    void editUserNameAndPassword(){
        Label errorLabel = (Label) lookup("#errorLabel").query();
        Label nameLabel = (Label) lookup("#nameLabel").query();
        doReturn(Observable.just(new UserDto(LocalDateTime.now(),LocalDateTime.now(),"3","Mila",null))).when(editUserService).update(any(),any());

        //Title: A user wants to edit his name, his password and the language.
        //Start: A user is in the User administration window.
        //He wants to edit his name, to change his password and the language.

        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals("Nicole", nameLabel.getText());

        //Action: He changes his name to Mila, his password to 12345678 and the language to English.
        clickOn("#nameInput");
        write("Mila\t");

        write("12345678\t");
        Assertions.assertTrue(lookup("#saveButton").query().isDisabled());
        Assertions.assertEquals("Passwords do not match", errorLabel.getText());
        write("12345678\t");
        Assertions.assertFalse(lookup("#saveButton").query().isDisabled());

        clickOn("#saveButton");
        waitForFxEvents();
        //TODO: implement language change test

        //Result: The user has successfully changed his name to Mila, his password to 12345678 and the language to English.
        Assertions.assertEquals("Mila",nameLabel.getText());
        Assertions.assertEquals("Your name, password and language are successfully updated",
              errorLabel.getText());
        verify(editUserService, times(1)).update("Mila","12345678");
    }

    @Test
    void editUserNameError() {

        Label errorLabel = (Label) lookup("#errorLabel").query();
        Label nameLabel = (Label) lookup("#nameLabel").query();
        doReturn(Observable.error(new Throwable(HTTP_409))).when(editUserService).update(any(),any());

        //Title: A user wants to edit his name but he fails.
        //Start: A user is in the User administration window. He wants to edit his name.
        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals("Nicole", nameLabel.getText());

        //Action: He changes his name to Lea.
        clickOn("#nameInput");
        write("Lea\t");
        clickOn("#saveButton");
        waitForFxEvents();

        //Result: There is already one user with this name, so Nicole has to choose another name.
        Assertions.assertEquals("Nicole",nameLabel.getText());
        Assertions.assertEquals("The name you have entered already exists. Please choose another one.", errorLabel.getText());
        verify(editUserService, times(1)).update("Lea",null);
    }

    @Test
    void editUserPasswordError() {
        Label errorLabel = (Label) lookup("#errorLabel").query();
        Label nameLabel = (Label) lookup("#nameLabel").query();

        //Title: A user wants to edit his password but he fails.
        //Start: A user is in the User administration window. He wants to edit his password.
        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals("Nicole", nameLabel.getText());

        //Action: He changes his password to 1234567.
        clickOn("#passwordInput");
        write("12345678\t");
        write("1234567\t");
        clickOn("#saveButton");
        waitForFxEvents();

        //Result: The confirmed password does match, so Nicole has to enter the password again.
        Assertions.assertTrue(lookup("#saveButton").query().isDisabled());
        Assertions.assertEquals("Nicole",nameLabel.getText());
        Assertions.assertEquals("Passwords do not match", errorLabel.getText());
        verify(editUserService, times(0)).update(any(),any());
    }
    @Test
    void moveToAccountDeletion(){
        Text deleteAccountText = (Text) lookup("#deleteAccountText").query();

        //Title: A user wants to delete his account.
        //Start: A user is in the User administration window. He wants to delete his account.
        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals(deleteAccountText.getText(),"Do you want to delete your account?");
        //Action: He clicks on the Do you want to delete your account? text.
        clickOn(deleteAccountText);
        waitForFxEvents();

        //Result: The user is in the Account deletion confirmation window.
        Assertions.assertEquals("Delete your account",stage.getTitle());
        verify(app, times(1)).show("/editUser/accountDeletionConfirmation");
    }

    @Test
    void chooseAnotherLanguage(){
        //Title: A user wants to change the language.
        //Start: A user is in the User administration window. He wants to change the language.
        ComboBox languageComboBox = (ComboBox) lookup("#languageComboBox").query();
        Label errorLabel = (Label) lookup("#errorLabel").query();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.GERMAN);
        when(newResources.get()).thenReturn(resourceBundle);
        Assertions.assertEquals("User administration",stage.getTitle());
        Assertions.assertEquals("English",languageComboBox.getValue());
        //Action: He clicks on the language he wants to choose.
        clickOn("#languageComboBox");
        clickOn("German");
        waitForFxEvents();

        //Result: The user has successfully changed the language to German
        verify(prefService, times(1)).setLocale(Locale.GERMAN);
        Assertions.assertEquals("German", languageComboBox.getValue());

        //***This is for changing the language back to english for the other tests***
        prefService.setLocale(Locale.ENGLISH);
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, editUserService, resources, newResources);
        subscriber = null;
        editUserService = null;
        resources = null;
        newResources = null;
        editUserController = null;
        super.stop();
    }
}
