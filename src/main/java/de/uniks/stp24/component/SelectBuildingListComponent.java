package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.dto.FractionDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.BuildingService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
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

@Component(view = "SelectBuildingList.fxml")
public class SelectBuildingListComponent extends VBox {
    public ObservableList<BuildingDto> buildings = FXCollections.observableArrayList();
    @Param("fraction")
    Fraction fraction;
    @Param("game")
    Game game;
    @FXML
    public ListView<BuildingDto> selectBuildingList;

    @Inject
    App app;
    @Inject
    Subscriber subscriber;
    @Inject
    BuildingService buildingService;
    @Inject
    EventListener eventListener;
    @Inject
    GameService gameService;

    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Inject
    Provider<SelectBuildingComponent> selectBuildingComponentProvider;
    @Inject
    PresetsApiService presetsApiService;
    public ObservableList<BuildingDto> allBuildings = FXCollections.observableArrayList();

    @Inject
    @Resource
    ResourceBundle resource;

    @Inject
    public SelectBuildingListComponent() {
    }

    @OnInit
    void init() {

        if(this.fraction.getEmpire() == null || this.fraction.getEmpire().getOwner() != gameService.getOwnUser()) return;

        subscriber.subscribe(presetsApiService.getBuildings(), b -> allBuildings.addAll(b));

        subscriber.subscribe(buildingService.getNotOwnedBuildings(game, fraction),
                notOwnedBuildings -> buildings.setAll(notOwnedBuildings));

        subscriber.subscribe(eventListener.listen("games." + game.getId() + ".systems." + fraction.get_id() + ".*",
                FractionDto.class), event -> {
            if (event.suffix().equals("updated")) {
                ObservableList<BuildingDto> newNotOwnedBuildings = FXCollections.observableArrayList();
                selectBuildingList.setItems(newNotOwnedBuildings);
                subscriber.subscribe(buildingService.getNotOwnedBuildings(game, fraction),
                        newNotOwnedBuildings::addAll
                );
            }
        });

    }

    @OnRender
    void renderBuildings() {
        if(this.fraction.getEmpire() == null || this.fraction.getEmpire().getOwner() != gameService.getOwnUser()) return;

        selectBuildingList.setItems(buildings);
        selectBuildingList.setCellFactory(
                list -> new ComponentListCell<>(app,
                        () -> app.initAndRender(selectBuildingComponentProvider.get(),
                                Map.of("fraction", fraction, "game", game))));
    }

    @OnDestroy
    void destroy() {
        subscriber.dispose();
    }
}
