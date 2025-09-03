package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.component.EmpireComponent;
import de.uniks.stp24.dto.EmpireDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.service.EmpireService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@Title("Choose Empire")
public class ChooseEmpireController {


    @FXML
    public ListView<EmpireDto> empiresBox;
    @FXML
    Button saveButton;

    @Inject
    App app;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Subscriber subscriber;
    @Inject
    Provider<EmpireComponent> empireComponentProvider;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Inject
    GameMembersApiService gameMembersApiService;
    @SubComponent
    @Inject
    EmpireComponent empireComponent;
    @Inject
    EventListener eventListener;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    EmpireService empireService;
    @Inject
    GameService gameService;
    @Param("game")
    Game game;
    @Param("ready")
    boolean ready = true;

    private ObservableList<EmpireDto> empires;

    @Inject
    public ChooseEmpireController() {
    }


    @OnInit
    void init() {
        this.empires = FXCollections.observableArrayList(empireService.getEmpires(game.getId()));
    }

    @OnDestroy
    void destroy() {
        subscriber.dispose();
    }

    @OnRender
    void render() {
        subscriber.bind(saveButton.disableProperty(),
                empiresBox.getSelectionModel().selectedItemProperty().isNull());
        empiresBox.setItems(empires);
        empiresBox.setCellFactory(list -> new ComponentListCell<>(app, empireComponentProvider));
    }

    public void saveButton() {
        EmpireDto gameEmpireDto = empiresBox.getSelectionModel().getSelectedItem();
        subscriber.subscribe(gameService.updateUserEmpire(gameEmpireDto),
                result -> app.show("/gameOverview", Map.of("game", game)),
                Throwable::printStackTrace);
    }


    public void backButton() {
        app.show("/gameOverview", Map.of("game", game));
    }
}
