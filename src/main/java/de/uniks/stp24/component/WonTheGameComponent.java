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
import java.util.ResourceBundle;

@Component(view ="WonTheGameComponent.fxml")
public class WonTheGameComponent extends AnchorPane {

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
    public StackPane stackPane;

    @Inject
    public WonTheGameComponent() {

    }

    @OnRender
    public void loadImage() {
        imageView.setImage(imageCache.get("images/applause.png"));
    }

    @FXML
    void onLeaveGameClicked() {
        gameService.leaveGame().subscribe();
        app.show("/lobby");
    }
    @FXML
    void onContinueClicked() {
        stackPane.getChildren().remove(this);
        stackPane = null;
    }

}
