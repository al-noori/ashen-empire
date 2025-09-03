package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.service.GameManagerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Controller
@Title("Deletion Screen")
public class DeletionScreenController {
    @Param("id")
    String id = "";

    @FXML
    public Button deleteButton;
    @FXML
    public Button abortButton;

    @Inject
    App app;
    @Inject
    GameManagerService gameManagerService;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public DeletionScreenController() {
    }

    public void delete() {
        subscriber.subscribe(gameManagerService.deleteGame(id),
                onNext -> app.show("/lobby"),
                onError -> {
                });
    }

    public void abort() {
        app.show("/lobby");
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
