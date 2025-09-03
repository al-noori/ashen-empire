package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.MusicService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@Component(view = "GamePaused.fxml")
public class GamePausedComponent extends AnchorPane {
    public Button achievementsButton;
    @FXML
    Button resumeButton;
    @FXML
    Button backButton;
    @FXML
    Text pauseText;
    @FXML
    Button musicButton;
    @FXML
    ImageView musicButtonImage;
    @Inject
    GameService gameService;

    @Inject
    App app;

    @Param("game")
    Game game;

    @Inject
    MusicService musicService;
    public StackPane stackPane;

    @Inject
    @Resource
    ResourceBundle resource;

    @Inject
    public GamePausedComponent() {
    }

    public void initialize() {
        changeMusicButton();
        if (gameService.amISpectator()) {
            achievementsButton.setDisable(true);
        }
    }

    public void achievements() {
        app.show("/achievements", Map.of("backTo", "/inGame", "game", game));
    }

    public void resume() {
        //app.show("/inGame", Map.of("game", game));
        stackPane.getChildren().remove(this);
    }

    public void backToLobby() {
        app.show("/lobby");
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
            musicButtonImage.setFitHeight(50.0);
            musicButtonImage.setFitWidth(50.0);
        } else {
            image = new Image(Objects.requireNonNull(getClass().getResource("/de/uniks/stp24/music/music.png")).toString());
            musicButtonImage.setFitHeight(25.0);
            musicButtonImage.setFitWidth(25.0);

        }
        musicButtonImage.setImage(image);
    }

}
