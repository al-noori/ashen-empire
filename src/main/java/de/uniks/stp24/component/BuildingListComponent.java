package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.dto.FractionDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.ws.EventListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "BuildingList.fxml")
public class BuildingListComponent extends VBox {

    @FXML
    Label buildingsLabel;
    @FXML
    ListView<BuildingDto> buildingList;
    @Inject
    App app;
    @Inject
    EventListener eventListener;
    @Inject
    Subscriber subscriber;
    @Inject
    Provider<OwnBuildingComponent> buildingComponentProvider;
    @Inject
    PresetsApiService presetsApiService;
    @Inject
    public GameEmpiresApiService gameEmpiresApiService;
    @Param("fraction")
    Fraction fraction;
    @Param("game")
    Game game;

    @Inject
    @Resource
    ResourceBundle resource;

    public ObservableList<BuildingDto> currentBuildings = FXCollections.observableArrayList();
    public ObservableList<BuildingDto> allBuildings = FXCollections.observableArrayList();

    @Inject
    public BuildingListComponent() {
    }

    @OnInit
    void init() {
        subscriber.subscribe(presetsApiService.getBuildings(), b -> allBuildings.addAll(b));
        currentBuildings = allBuildings.filtered(
                building -> fraction.getBuildings().contains(building.id()));

        subscriber.subscribe(eventListener.listen("games." + game.getId() + ".systems." + fraction.get_id() + ".*",
               FractionDto.class), event -> {
            if (event.suffix().equals("updated")) {
                ObservableList<BuildingDto> newCurrentBuildings = FXCollections.observableArrayList();
                buildingList.setItems(newCurrentBuildings);
                FractionDto updatedFraction = event.data();
                newCurrentBuildings.addAll(
                        allBuildings.filtered(
                                building -> Arrays.stream(updatedFraction.buildings()).toList().contains(building.id())
                        )
                );
            }
        });
    }

    @OnRender
    void renderBuildings() {
        buildingsLabel.setText("Buildings (" + fraction.getBuildings().size()+ ")");
        subscriber.subscribe(eventListener.listen("games." + game.getId() + ".systems." + fraction.get_id() + ".*",
                FractionDto.class), event -> {
            if (event.suffix().equals("updated")) {
                buildingsLabel.setText("Buildings (" + event.data().buildings().length+ "):");
            }
        });
        buildingList.setItems(currentBuildings);
        buildingList.setCellFactory(list -> new ComponentListCell<>(app,  () -> app.initAndRender(buildingComponentProvider.get(), Map.of("fraction", fraction, "game", game))));
    }
    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }

}