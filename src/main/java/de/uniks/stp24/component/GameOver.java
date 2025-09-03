package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnRender;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view ="GameOver.fxml")
public class GameOver extends AnchorPane {

    public StackPane stackPane;
    @FXML
    ImageView imageView;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    ImageCache imageCache;
    @Inject
    App app;
    @Inject
    GameService gameService;

    @Inject
    public GameOver() {

    }
    @OnRender
    public void onRender() {
        loadImage();
    }

    @FXML
    void onLeaveGameClicked() {
        app.show("/lobby");
    }

    @FXML
    void onGoToSpectatorModeClicked() {
        app.show("/inGame", Map.of("game", gameService.getGame()));
    }

    void loadImage() {
        imageView.setImage(imageCache.get("images/lost.png"));
    }
}
