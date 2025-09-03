package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.JoinGameDto;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.service.GameService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;

@Controller
@Title("Join the game")
public class JoinTheGameController {

    @FXML
    Button joinGameButton;
    @FXML
    Button cancelButton;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    @Inject
    App app;
    @Inject
    GameMembersApiService gameMembersApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    @Param("game")
    GameDto game;

    @Inject
    public JoinTheGameController() {
    }

    public void joinTheGame() {
        String password = passwordField.getText();
        if (game != null) {
            joinGame(game, password);
        }
    }

    public void cancelJoining() {
        app.show("/lobby");
    }

    public void joinGame(GameDto gameDto, String password) {
        if (gameService.amIHost()) {
            app.show("/gameOverview");
            return;
        }

        if (password == null || password.isEmpty()) {
            errorLabel.setText("Please enter the password!");
            return;
        }

        if (!gameDto.started()) {
            subscriber.subscribe(gameService.amIMember(gameDto._id()), hasJoined -> {
                JoinGameDto joinGameDto = new JoinGameDto(false, null, password);
                subscriber.subscribe(gameMembersApiService.joinGame(gameDto._id(), joinGameDto), response -> {
                    if (!response.isSuccessful()) {
                        errorLabel.setText("The password was incorrect. Please try again.");
                    } else {
                        app.show("/gameOverview");
                    }
                });
            });
        }
    }

}