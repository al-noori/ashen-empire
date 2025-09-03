package de.uniks.stp24.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stp24.App;
import de.uniks.stp24.dto.ErrorResponse;
import de.uniks.stp24.service.LoginService;
import de.uniks.stp24.service.TimerService;
import de.uniks.stp24.service.UserService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;
import retrofit2.HttpException;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;


@Controller
@Title("Sign up")
public class SignupController {

    @FXML
    ImageView imageView;
    @FXML
    Button signUpButton;
    @FXML
    TextField nameInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    PasswordField confirmPasswordInput;
    @FXML
    Label errorLabel;
    @FXML
    Label serverErrorLabel;

    @Inject
    App app;
    @Inject
    UserService userService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    LoginService loginService;
    @Inject
    TimerService timerService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Param("username") //get values from login
    String username = "";
    @Param("password")
    String password = "";
    @Inject
    Subscriber subscriber;

    @Inject
    SignupController() {
    }


    @OnRender
        //access also to whole fxml
    void applyInputsFromLogin() {
        serverErrorLabel.setVisible(false);
        nameInput.setText(username);
        passwordInput.setText(password);
    }

    public void onSignUpClicked() {
        subscriber.subscribe(
                userService
                        .signup(nameInput.getText(), passwordInput.getText()),
                result -> Platform.runLater(() -> {

                    subscriber.subscribe(
                            loginService.login(nameInput.getText(), passwordInput.getText(), false),
                            loginResult -> app.show("/lobby"), throwable -> app.show("/login"));

                    timerService.setupTimer();


                }),
                error -> {
                    if (error instanceof HttpException httpError) {
                        System.out.println(httpError.code());
                        if (httpError.code() == 409) {
                            Platform.runLater(() -> {
                                serverErrorLabel.setVisible(true);
                                serverErrorLabel.setText(resources.getString(NAME_ALREADY_EXISTS));
                            });

                        } else {
                            String body = Objects.requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
                            ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);

                            Platform.runLater(() -> serverErrorLabel.setText(errorResponse.error()));
                        }
                    }
                }
        );
    }

    private void updateErrorLabel(BooleanBinding equalPasswords, BooleanBinding nameNotEmpty, BooleanBinding passwordIsGreaterThanSeven) {
        // update Error Lable
        subscriber.bind(errorLabel.textProperty(),
                Bindings.when(passwordIsGreaterThanSeven.not()).then(resources.getString(PASSWORD_TOO_SHORT))
                        .otherwise(
                                Bindings.when(equalPasswords.not())
                                        .then(resources.getString(PASSWORDS_DO_NOT_MATCH))
                                        .otherwise(Bindings.when(nameNotEmpty.not())
                                                .then(resources.getString(SET_NAME))
                                                .otherwise(EMPTY_STRING)
                                        )

                        ));
        subscriber.bind(errorLabel.visibleProperty(),
                Bindings.when(passwordIsGreaterThanSeven.not().or(equalPasswords.not()).or(nameNotEmpty.not()))
                        .then(true)
                        .otherwise(false)
        );
    }


    private void updateNameInputBorderColor(BooleanBinding nameNotEmpty) {
        subscriber.bind(nameInput.styleProperty(),
                Bindings.when(nameNotEmpty.not())
                        .then("-fx-border-color: #FF7F50;" + " -fx-border-width: 5px;")
                        .otherwise("")
        );

    }

    private void updatePasswordInputBorderColor(BooleanBinding equalPasswords, BooleanBinding passwordIsGreaterThanSeven, PasswordField passwordField) {
        subscriber.bind(passwordField.styleProperty(),
                Bindings.when(passwordIsGreaterThanSeven.not())
                        .then("-fx-border-color: #FF7F50;" + " -fx-border-width: 5px;")
                        .otherwise(
                                Bindings.when(equalPasswords.not())
                                        .then("-fx-border-color: #FF7F50;" +
                                                " -fx-border-width: 5px;")
                                        .otherwise("")
                        )
        );
    }


    @OnRender
    void updateUiElements() {
        final BooleanBinding equalPasswords = passwordInput.textProperty().isEqualTo(confirmPasswordInput.textProperty());
        final BooleanBinding nameNotEmpty = nameInput.textProperty().isNotEmpty();
        final BooleanBinding passwordIsGreaterThanSeven = passwordInput.textProperty().length().greaterThan(7);
        final BooleanBinding disableButton = ((passwordIsGreaterThanSeven.not()).or(nameNotEmpty.not()).or(equalPasswords.not()));

        updateErrorLabel(equalPasswords, nameNotEmpty, passwordIsGreaterThanSeven);

        // update nameInput border color
        updateNameInputBorderColor(nameNotEmpty);

        // update password border color
        updatePasswordInputBorderColor(equalPasswords, passwordIsGreaterThanSeven, passwordInput);

        // update confirmPassword border color
        updatePasswordInputBorderColor(equalPasswords, passwordIsGreaterThanSeven, confirmPasswordInput);

        // update signUp Button disability
        subscriber.bind(signUpButton.disableProperty(), disableButton);


    }

    public void onBackButtonClicked() {
        app.show("/login", Map.of("username", username, "password", password));
    }

    @OnDestroy
    void destroy() {
        subscriber.dispose();
        imageView.setImage(null);
    }
}
