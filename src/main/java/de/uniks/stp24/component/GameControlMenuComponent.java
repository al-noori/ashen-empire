package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.service.GameControlMenuService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.ws.EventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static de.uniks.stp24.constants.Constants.*;

@Component(view = "GameControlMenu.fxml")
public class GameControlMenuComponent extends VBox {

    @FXML
    ImageView pauseImage;
    @FXML
    ImageView playImage;
    @FXML
    ImageView doubleSpeedImage;
    @FXML
    ImageView tripleSpeedImage;
    @FXML
    Button zeroSpeedButton;
    @FXML
    Button regularSpeedButton;
    @FXML
    Button doubleSpeedButton;
    @FXML
    Button tripleSpeedButton;
    @FXML
    Label gameStatusLabel;
    @FXML
    Label timeLabel;
    @Inject
    public GameControlMenuService gameControlMenuService;
    @Inject
    public GamesApiService gamesApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    ImageCache imageCache;
    @Inject
    GameService gameService;

    @Inject
    EventListener eventListener;
    @Inject
    @Resource
    ResourceBundle resource;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Param("game")
    Game game;
    int currentSpeed = 1;
    Button[] buttons;

    @Inject
    public GameControlMenuComponent() {
    }

    @OnInit
    public void onInit() {
        currentSpeed = game.getSpeed();
        gameControlMenuService.startPeriodicUpdate(game, currentSpeed);
    }

    @OnRender
    public void onRender() {
        buttons = new Button[]{zeroSpeedButton, regularSpeedButton, doubleSpeedButton, tripleSpeedButton};
        disableAllButtonsIfNotHost();
        gameStatusLabel.setText(gameStatusHandler(currentSpeed));
        buttonColorHandler(currentSpeed);

        updateClock(formatter, game.getPeriod());

        subscriber.listen(game.listeners(), Game.PROPERTY_SPEED, evt -> {
            currentSpeed = game.getSpeed();
            Platform.runLater(() -> {
                gameStatusLabel.setText(gameStatusHandler(currentSpeed));
                buttonColorHandler(currentSpeed);
            });
        });
        subscriber.listen(game.listeners(), Game.PROPERTY_PERIOD, evt ->
                Platform.runLater(() -> updateClock(formatter, (Integer) evt.getNewValue())));
        System.out.println(currentSpeed);
        pauseImage.setImage(imageCache.get("icons/pause_icon.png"));
        playImage.setImage(imageCache.get("icons/play_icon.png"));
        doubleSpeedImage.setImage(imageCache.get("icons/doublespeed_icon.png"));
        tripleSpeedImage.setImage(imageCache.get("icons/triplespeed_icon.png"));
    }

    private void disableAllButtonsIfNotHost() {
        List<Button> buttons = List.of(zeroSpeedButton, regularSpeedButton, doubleSpeedButton, tripleSpeedButton);
        boolean isHost = game.getOwner() == gameService.getOwnUser();
        buttons.forEach(button -> button.disableProperty().bind(Bindings.createBooleanBinding(() -> !isHost)));
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        gameControlMenuService.stopPeriodicUpdate();
    }

    public void tripleSpeedUp() {
        accelerateSpeedTo(GAME_SPEED_3);
    }

    public void doubleSpeedUp() {
        accelerateSpeedTo(GAME_SPEED_2);
    }

    public void regularSpeedUp() {
        accelerateSpeedTo(GAME_SPEED_1);
    }

    public void zeroSpeedUp() {
        accelerateSpeedTo(GAME_SPEED_0);
    }

    private void accelerateSpeedTo(int clickedSpeed) {
        currentSpeed = clickedSpeed;
        gameControlMenuService.stopPeriodicUpdate();
        subscriber.subscribe(gameControlMenuService.updateSpeed(game, clickedSpeed),
                game -> {
                    gameStatusLabel.setText(gameStatusHandler(clickedSpeed));
                    buttonColorHandler(clickedSpeed);
                });
        gameControlMenuService.startPeriodicUpdate(game, clickedSpeed);
    }

    private void buttonColorHandler(int clickedSpeed) {
        buttons[clickedSpeed].setStyle("-fx-background-color: #00FFBF");
        for (int i = 0; i < buttons.length; i++) {
            if (i != clickedSpeed) {
                buttons[i].setStyle("");
            }
        }
    }


    private String gameStatusHandler(int speed) {
        return switch (speed) {
            case 0 -> "Game Status: Paused";
            // Default case gets triggered when the speed is 1
            case 2 -> "Game Status: Fast";
            case 3 -> "Game Status: Very fast";
            default -> "Game Status: Play";
        };
    }

    public void updateClock(DateTimeFormatter formatter, int period) {
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 1, 0, 0).plusMonths(period);
        timeLabel.setText(currentTime.format(formatter.withLocale(resource.getLocale())));
    }
}
