package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.PresetShipDto;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.PlanNewFleetService;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(view = "PlanNewTanks.fxml")
public class PlanNewTanksComponent extends VBox {

    public VBox planNewTanksHolder;

    @FXML
    Button backButton;
    @FXML
    Button saveButton;
    @FXML
    Text planNewTanks;
    @FXML
    ListView<PresetShipDto> newTankList;
    @Inject
    TankService tankService;
    @Inject
    PresetsApiService presetsApiService;
    @Inject
    App app;
    @Inject
    Provider<NewTanksListEntryComponent> newTanksListEntryComponentProvider;
    @Inject
    PlanNewFleetService planNewFleetService;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    GameService gameService;
    @Inject
    Subscriber subscriber;
    @Inject
    JobService jobService;
    Fleet fleet;

    @Inject
    public PlanNewTanksComponent() {
    }

    public void setData(Fleet fleet) {
        this.fleet = fleet;
        List<String> existingShipType = new ArrayList<>();
        fleet.getShips().forEach(
                ship -> existingShipType.add(ship.getType())
        );

        subscriber.subscribe(tankService.getShips().subscribe(presetShipDtos -> {
            List<PresetShipDto> shipsToAdd = new ArrayList<>();

            for (PresetShipDto presetShipDto : presetShipDtos) {
                if (!existingShipType.contains(presetShipDto.id())) {
                    shipsToAdd.add(presetShipDto);
                }
            }

            Platform.runLater(() -> {
                ObservableList<PresetShipDto> observableShipsToAdd = FXCollections.observableArrayList(shipsToAdd);
                newTankList.setItems(observableShipsToAdd);
                planNewFleetService.resetShipCounters();
            });
        }, throwable -> System.out.println("Error fetching ship: " + throwable.getMessage())));
    }

    @OnRender
    public void onRender(){
        newTankList.setCellFactory(param -> new ComponentListCell<>(app, () -> newTanksListEntryComponentProvider.get()));
    }

    public void back() {
        planNewTanksHolder.getChildren().clear();
        newTankList.getItems().clear();
    }

    public void save() {
        Map<String, Integer> size = planNewFleetService.getShipCounters();
        Map<String, Integer> currentSize = fleet.getSize();
        Map<String, Integer> newSize = new HashMap<>(size);
        currentSize.forEach((key, value) -> newSize.merge(key, value, Integer::sum));
        fleet.setSize(newSize);
        planNewTanksHolder.getChildren().clear();
        newTankList.getItems().clear();
    }

}
