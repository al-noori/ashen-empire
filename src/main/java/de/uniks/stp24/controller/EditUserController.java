package de.uniks.stp24.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stp24.App;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.EditUserService;
import de.uniks.stp24.service.PrefService;
import de.uniks.stp24.service.TokenStorage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;

@Controller
@Title("User administration")
public class EditUserController {
    @FXML
    ComboBox<String> languageComboBox;
    @FXML
    Button cancelButton;
    @FXML
    Text deleteAccountText;
    @FXML
    Button saveButton;
    @FXML
    PasswordField passwordInput;
    @FXML
    PasswordField confPasswordInput;
    @FXML
    ImageView potrait;
    @FXML
    Label nameLabel;
    @FXML
    TextField nameInput;
    @FXML
    Label errorLabel;
    @Inject
    App app;
    @Inject
    EditUserService editUserService;
    @Inject
    UserApiService userApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    PrefService prefService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Provider<ResourceBundle> newResources;
    private String currentNameInput;
    private String currentPasswordInput;
    private String currentConfPasswordInput;

    @Inject
    public EditUserController() {
    }

    /**
     * Displays the name and avatar of the user
     */
    @OnRender
    void displayNameAndAvatar() {
        subscriber.subscribe(
                editUserService.getUser(),
                user -> nameLabel.setText(user.name())
        );
        //TODO: implement avatar
    }

    @OnRender
    public void renderLanguageBox() {
        String languageTag = prefService.getLocale().toLanguageTag();
        languageTag = languageTag.contains("en") ? EN : DE;
        languageComboBox.setValue(resources.getString(languageTag));
        ObservableList<String> languages = FXCollections.observableArrayList(resources.getString(EN), resources.getString(DE));
        languageComboBox.setItems(languages);
    }

    /**
     * Changes the error message if the password is not valid
     */
    @OnRender
    void changeErrorMessageCorrectly() {
        nameInput.textProperty().addListener((observable, oldValue, newValue) -> ensurePasswordIsValid());
    }

    /**
     * Ensures that the password is valid while rendering
     */
    @OnRender
    void ensurePasswordIsValid() {
        final BooleanBinding notEqualPasswords = passwordInput.textProperty().isNotEqualTo(confPasswordInput.textProperty());
        final BooleanBinding passwordTooShort = passwordInput.textProperty().length().lessThan(PASSWORD_CHARACTER_LIMIT)
                .and(passwordInput.textProperty().isNotEmpty());
        final BooleanBinding allEmpty = passwordInput.textProperty().isEmpty().and(confPasswordInput.textProperty().isEmpty()).and(nameInput.textProperty().isEmpty());
        subscriber.bind(saveButton.disableProperty(),
                notEqualPasswords.or(passwordTooShort).or(allEmpty));
        subscriber.bind(errorLabel.textProperty(),
                Bindings.when(notEqualPasswords)
                        .then(resources.getString(PASSWORDS_DO_NOT_MATCH)).otherwise(
                                Bindings.when(passwordTooShort)
                                        .then(resources.getString(PASSWORD_TOO_SHORT)).otherwise(EMPTY_STRING))
        );

        // invisible error label when no error
        subscriber.bind(errorLabel.visibleProperty(),
                Bindings.when(notEqualPasswords.not().and(passwordTooShort.not()).or(allEmpty))
                        .then(false)
                        .otherwise(true));
    }

    /**
     * Goes to the account deletion confirmation page
     */
    public void onDeleteClicked() {
        app.show("/editUser/accountDeletionConfirmation");
    }

    /**
     * Saves the changes made by the user, if the name is not taken
     */
    public void onSaveClicked() {
        String password = passwordInput.getText();
        String name = nameInput.getText();
        subscriber.subscribe(
                editUserService.update(name.isEmpty() ? null : name, password.isEmpty() ? null : password),
                user -> {
                    nameLabel.setText(user.name());
                    errorLabel.textProperty().unbind();
                    errorLabel.visibleProperty().unbind();
                    errorLabel.visibleProperty().set(true);
                    errorLabel.setText(resources.getString(SUCCESSFUL_UPDATE));
                    errorLabel.setTextFill(Color.GREEN);
                }, error -> {
                    errorLabel.visibleProperty().unbind();
                    errorLabel.visibleProperty().set(true);
                    errorLabel.textProperty().unbind();
                    errorLabel.setText(errorHandler(error.getMessage()));
                    errorLabel.setTextFill(Color.RED);
                });
    }


    private String errorHandler(String message) {
        if (message.contains("409"))
            return resources.getString(NAME_ALREADY_EXISTS);
        return resources.getString(CUSTOM_ERROR);
    }

    public void chooseLanguage() {
        String selectedLanguage = languageComboBox.getValue();
        if (selectedLanguage.equals(resources.getString(EN))) {
            setLocale(Locale.ENGLISH);
        } else if (selectedLanguage.equals(resources.getString(DE))) {
            setLocale(Locale.GERMAN);
        }
    }

    private void setLocale(Locale lang) {
        prefService.setLocale(lang);
        resources = newResources.get();
        saveCurrentState();
        app.refresh();
        setCurrentState();
    }

    private void setCurrentState() {
        nameInput.setText(currentNameInput);
        passwordInput.setText(currentPasswordInput);
        confPasswordInput.setText(currentConfPasswordInput);
    }

    private void saveCurrentState() {
        currentNameInput = nameInput.getText();
        currentPasswordInput = passwordInput.getText();
        currentConfPasswordInput = confPasswordInput.getText();
    }

    public void onCancelClicked() {
        app.show("/lobby");
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        potrait.setImage(null);
    }
}
