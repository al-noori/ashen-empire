package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.service.GameManagerService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;

@Controller
@Title("Create a new game")
public class CreateGameController {


    //----------------------FXML-ELEMENTS----------------------------------//
    @FXML
    TextField nameField;
    @FXML
    PasswordField passwordField;
    @FXML
    TextField mapSizeField;
    @FXML
    Button saveButton;
    @FXML
    Button cancelButton;
    @FXML
    Label statusLabel;
    //----------------------DEPENDENCIES----------------------------------//
    @Inject
    App app;
    @Inject
    GameManagerService gameManagerService;
    @Inject
    UserService userService;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    GameService gameService;

    @Inject
    CreateGameController() {

    }

    public void onCancelClicked() {
        app.show("/lobby");
    }

    public void onSaveClicked() {
        resetStyles();
        if (allInputsValid(nameField.getText(), passwordField.getText(), mapSizeField.getText())) {
            createGame();
        }
    }

    private void createGame() {
        subscriber.subscribe(
                gameManagerService.createGame(nameField.getText(), passwordField.getText(), Integer.parseInt(mapSizeField.getText()))
                        .flatMap(r -> gameService.loadGame(r._id()))
                        .subscribe(game -> Platform.runLater(() -> {
                            gameService.disableSpectator();
                            statusLabel.setText(resources.getString(GAME_CREATED_SUCCESSFULLY));
                            app.show("/gameOverview", Map.of("game", game));
                        }), error -> Platform.runLater(() -> {
                            statusLabel.setText(resources.getString(CREATE_GAME_ERROR) + error.getMessage());
                            statusLabel.setVisible(true);
                            System.out.println(error.getMessage());
                        }))
        );
    }

    private boolean isANumber(String mapSize) {
        try {
            Integer.parseInt(mapSize);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if (!password.isEmpty()) {
            return true;
        } else {
            passwordField.getStyleClass().add("error-border");
            statusLabel.setText(resources.getString(SET_PASSWORD));
            return false;
        }
    }

    private boolean isNameValid(String name) {
        if (!name.isEmpty()) {
            return true;
        } else {
            nameField.getStyleClass().add("error-border");
            statusLabel.setText(resources.getString(SET_NAME));
            return false;
        }
    }

    private boolean isMapSizeValid(String mapSize) {
        if (isANumber(mapSize)) {
            int size = Integer.parseInt(mapSize);
            if (size >= 50 && size <= 200) {
                return true;
            } else {
                mapSizeField.getStyleClass().add("error-border");
                statusLabel.setText(resources.getString(MAP_SIZE_ERROR));
                return false;
            }
        } else {
            mapSizeField.getStyleClass().add("error-border");
            statusLabel.setText(resources.getString(MAP_SIZE_TYPE_ERROR));
            return false;
        }
    }

    private boolean allInputsValid(String name, String password, String mapSize) {
        return isNameValid(name) && isPasswordValid(password) && isMapSizeValid(mapSize);
    }

    private void resetStyles() {
        nameField.getStyleClass().remove("error-border");
        passwordField.getStyleClass().remove("error-border");
        mapSizeField.getStyleClass().remove("error-border");
        statusLabel.getStyleClass().remove("error-border");
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
