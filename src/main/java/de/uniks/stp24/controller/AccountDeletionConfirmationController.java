package de.uniks.stp24.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stp24.App;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.EditUserService;
import de.uniks.stp24.service.TokenStorage;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.Optional;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;

@Controller
@Title("Delete your account")
public class AccountDeletionConfirmationController {
    @FXML
    Label errorLabel;
    @FXML
    Button cancelButton;
    @Inject
    App app;
    @FXML
    PasswordField confPasswordInput;
    @FXML
    Button deleteButton;
    @Inject
    UserApiService userApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    EditUserService editUserService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    Subscriber subscriber;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public AccountDeletionConfirmationController() {
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    /**
     * This method is called when the delete button is clicked
     * It checks if the password is correct and then deletes the account
     */
    public void onDeleteClicked() {
        String password = confPasswordInput.getText();
        subscriber.subscribe(
                editUserService.checkPassword(password),
                loginResult -> deleteAccount(),
                error -> {
                    errorLabel.textProperty().unbind();
                    errorLabel.setText(errorHandler(error.getMessage()));
                    errorLabel.setVisible(true);
                }
        );
    }

    private String errorHandler(String message) {
        if (message.contains("401")) {
            return resources.getString(INCORRECT_PASSWORD);
        }
        return resources.getString(CUSTOM_ERROR);
    }

    /**
     * This method is called to reset the error message when the password is changed in the text field
     */

    @OnRender
    void resetErrorMessageCorrectly() {
        confPasswordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText(EMPTY_STRING);
            errorLabel.setVisible(false);
        });
    }

    /**
     * This method is called to ensure that the password is not too short
     */
    @OnRender
    void ensurePasswordIsValid() {
        final BooleanBinding passwordTooShort = confPasswordInput.textProperty().length().lessThan(PASSWORD_CHARACTER_LIMIT);
        subscriber.bind(deleteButton.disableProperty(), passwordTooShort);
    }

    /**
     * This method is called to delete the account and show a success pop up
     */
    public void deleteAccount() {
        subscriber.subscribe(
                editUserService.delete(),
                user -> successPopUp()
        );
    }

    /**
     * This method is called to show a success pop up when the account is deleted
     * does only have OK button and redirects to login page after clicking OK
     */
    private void successPopUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString(ACCOUNT_DELETION));
        alert.setHeaderText(null); // No header
        alert.setContentText(resources.getString(SUCCESSFUL_DELETION));
        alert.getButtonTypes().setAll(ButtonType.OK);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            app.show("/login");
        }
    }

    public void onCancelClicked() {
        app.show("/editUser");
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        subscriber = null;
        app = null;
        userApiService = null;
        tokenStorage = null;
        editUserService = null;
        objectMapper = null;
        resources = null;
    }
}
