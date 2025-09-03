package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.service.GameManagerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.CSSConstants.BACKGROUND_COLOR_ON_ERROR;
import static de.uniks.stp24.constants.CSSConstants.BACKGROUND_COLOR_ON_SUCCESS;
import static de.uniks.stp24.constants.Constants.*;

@Controller
@Title("Edit the Game")
public class GameManagerController {
    public String id = "";
    @FXML
    public TextField nameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public TextField mapSizeInput;
    @FXML
    public Label statusLabel;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveChangesButton;


    @Inject
    App app;
    @Inject
    GameManagerService gameManagerService;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;

    @OnRender
    public void setup(@Param("id") String id) {
        this.id = id;
        subscriber.subscribe(gameManagerService.getGame(id), dto -> {
            nameInput.setText(dto.name());
            passwordInput.setText("");
            mapSizeInput.setText(String.valueOf(dto.settings().size()));
        }, error -> statusLabel.setText("Error: " + error.getMessage()));
    }


    @Inject
    public GameManagerController() {
    }

    public void cancel() {
        app.show("/lobby");
    }

    public void saveChanges() {
        statusLabel.getStyleClass().remove(BACKGROUND_COLOR_ON_ERROR);
        statusLabel.getStyleClass().remove(BACKGROUND_COLOR_ON_SUCCESS);
        statusLabel.setVisible(false);
        if (passwordInput.getText().isEmpty()) {
            statusLabel.setText(resources.getString(SET_PASSWORD));
            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
            statusLabel.setVisible(true);
            return;
        }
        if (nameInput.getText().isEmpty()) {
            statusLabel.setText(resources.getString(SET_NAME));
            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
            statusLabel.setVisible(true);
            return;
        }
        try {
            if (Integer.parseInt(mapSizeInput.getText()) < 50) {
                statusLabel.setText(resources.getString(MAP_SIZE_TOO_SMALL));
                statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
                statusLabel.setVisible(true);
                return;
            }
            if (Integer.parseInt(mapSizeInput.getText()) > 200) {
                statusLabel.setText(resources.getString(MAP_SIZE_TOO_BIG));
                statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
                statusLabel.setVisible(true);
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText(resources.getString(MAP_SIZE_TYPE_ERROR));
            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
            statusLabel.setVisible(true);
            return;
        }
        subscriber.subscribe(
                gameManagerService.editGame(
                        id,
                        nameInput.getText(),
                        passwordInput.getText(),
                        Integer.parseInt(mapSizeInput.getText())
                ),
                res -> {
                    switch (res) {
                        case OK:
                            statusLabel.setText(resources.getString(CHANGES_SAVED));
                            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_SUCCESS);
                            break;
                        case NOT_YOUR_GAME:
                            statusLabel.setText(resources.getString(NOT_OWNER));
                            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
                            break;
                        default:
                            statusLabel.setText(resources.getString(CUSTOM_ERROR) + res);
                            statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
                    }
                    statusLabel.setVisible(true);
                }, error -> {
                    statusLabel.getStyleClass().remove(BACKGROUND_COLOR_ON_SUCCESS);
                    statusLabel.getStyleClass().add(BACKGROUND_COLOR_ON_ERROR);
                    statusLabel.setText("Error: " + error.getMessage());
                    statusLabel.setVisible(true);
                }
        );
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        subscriber = null;
        app = null;
        gameManagerService = null;
        resources = null;
    }
}
