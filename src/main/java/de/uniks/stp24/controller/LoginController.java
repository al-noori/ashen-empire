package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.service.LoginService;
import de.uniks.stp24.service.MusicService;
import de.uniks.stp24.service.PrefService;
import de.uniks.stp24.service.TimerService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;


@Controller
@Title("Log in")
public class LoginController {

    @FXML
    ImageView imageView3;
    @FXML
    ImageView imageView2;
    @FXML
    ImageView imageView;
    @FXML
    ComboBox<String> languageComboBox;
    @FXML
    TextField nameInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    Label errorLabel;
    @FXML
    Button loginButton;
    @FXML
    Button licencesButton;
    @FXML
    CheckBox rememberMe;
    @FXML
    Button musicButton;
    @FXML
    ImageView musicButtonImage;

    @Inject
    PrefService prefService;
    @Inject
    App app;
    @Inject
    LoginService loginService;
    @Inject
    TimerService timerService;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    Provider<ResourceBundle> newResources;
    @Param("username") //get back values from signup
    String username = "";
    @Param("password")
    String password = "";
    @Inject
    Subscriber subscriber;

    private String currentNameInput;
    private String currentPasswordInput;
    private boolean currentRememberMe;
    private final BooleanProperty nameTyped = new SimpleBooleanProperty(false);
    private final BooleanProperty passwordTyped = new SimpleBooleanProperty(false);


    @Inject
    MusicService musicService;

    @Inject
    public LoginController() {
    }


    @OnRender
    public void renderLanguageBox() {
        String languageTag = prefService.getLocale().toLanguageTag();
        languageTag = languageTag.contains("en") ? EN : DE;
        languageComboBox.setValue(resources.getString(languageTag));
        ObservableList<String> languages = FXCollections.observableArrayList(resources.getString(EN), resources.getString(DE));
        languageComboBox.setItems(languages);
    }

    public void login() {
        String username = nameInput.getText();
        String password = passwordInput.getText();
        boolean rememberMe = this.rememberMe.isSelected();


        subscriber.subscribe(
                loginService.login(username, password, rememberMe),
                loginResult -> app.show("/lobby"), throwable -> {
                    errorLabel.visibleProperty().unbind();
                    errorLabel.visibleProperty().set(true);
                    errorLabel.textProperty().unbind();
                    errorLabel.setText(resources.getString(INCORRECT_PASSWORD_AND_NAME));

                    nameInput.styleProperty().unbind();
                    passwordInput.styleProperty().unbind();

                    nameInput.setStyle("-fx-border-color: #FF7F50; -fx-border-width: 5px;");
                    passwordInput.setStyle("-fx-border-color: #FF7F50; -fx-border-width: 5px;");
                });

        timerService.setupTimer();
    }

    public void signup() {
        String username = nameInput.getText();
        String password = passwordInput.getText();
        //send username and password to sign up window, pass on information
        app.show("/signup", Map.of("username", username, "password", password));
    }

    @OnRender
    void errorHandling() {
        nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                nameTyped.set(true);
            }
        });

        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                passwordTyped.set(true);
            }
        });
        final BooleanBinding nameNotEmpty = nameInput.textProperty().isNotEmpty();
        final BooleanBinding passwordNotEmpty = passwordInput.textProperty().isNotEmpty();
        subscriber.bind(errorLabel.textProperty(), Bindings.when(nameNotEmpty.not().and(nameTyped))
                .then(resources.getString(SET_NAME))
                .otherwise(Bindings.when(passwordNotEmpty.not().and(passwordTyped))
                        .then(resources.getString(SET_PASSWORD))
                        .otherwise(EMPTY_STRING)
                ));

        //lock login button until name and password entered
        subscriber.bind(loginButton.disableProperty(),
                nameNotEmpty.and(passwordNotEmpty).not());

        //border color
        subscriber.bind(nameInput.styleProperty(),
                Bindings.when(nameNotEmpty.not().and(nameTyped))
                        .then("-fx-border-color: #FF7F50;" + " -fx-border-width: 5px;")
                        .otherwise("")
        );

        subscriber.bind(passwordInput.styleProperty(),
                Bindings.when(passwordNotEmpty.not().and(passwordTyped))
                        .then("-fx-border-color: #FF7F50;" + " -fx-border-width: 5px;")
                        .otherwise("")
        );

        // invisible error label
        subscriber.bind(errorLabel.visibleProperty(),
                Bindings.when(nameNotEmpty.and(passwordNotEmpty))
                        .then(false)
                        .otherwise(true)
        );
    }

    @OnRender
    void giveBackInputsFromLogin() {
        nameInput.setText(username);
        passwordInput.setText(password);
    }

    public void licences() {
        String username = nameInput.getText();
        String password = passwordInput.getText();
        app.show("/licenses", Map.of("username", username, "password", password));
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
        rememberMe.setSelected(currentRememberMe);
    }

    private void saveCurrentState() {
        currentNameInput = nameInput.getText();
        currentPasswordInput = passwordInput.getText();
        currentRememberMe = rememberMe.isSelected();
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        imageView.setImage(null);
        imageView2.setImage(null);
        imageView3.setImage(null);
    }

    @FXML
    public void playMusic() {
        musicService.toggleMusic();
        changeMusicButton();
    }


    @OnRender
    void changeMusicButton() {
        if (musicService == null) {
            return;
        }
        Image image;
        if (musicService.isMuted()) {
            image = new Image(Objects.requireNonNull(getClass().getResource("/de/uniks/stp24/music/muteMusic.png")).toString());
            musicButtonImage.setFitHeight(25);
            musicButtonImage.setFitWidth(25);
        } else {
            image = new Image(Objects.requireNonNull(getClass().getResource("/de/uniks/stp24/music/music.png")).toString());
            musicButtonImage.setFitHeight(25);
            musicButtonImage.setFitWidth(25);

        }
        musicButtonImage.setImage(image);
    }

}




