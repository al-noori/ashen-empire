package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.ShipDto;
import de.uniks.stp24.dto.UpdateFleetDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Ship;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.rest.ShipApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Component(view = "TankTransfer.fxml")
public class TankTransferComponent extends VBox {

    public VBox tankTransferHolder;
    @FXML
    Text tankTransferName;
    @FXML
    ListView<AbstractMap.SimpleEntry<Integer, Ship>> tankList;
    @FXML
    ComboBox<Fleet> selectDivisionComboBox;
    @FXML
    Button cancelButton;
    @FXML
    Button transferButton;

    @Inject
    TankService tankService;
    @Inject
    PresetsApiService presetsApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    App app;
    @Inject
    GameService gameService;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    ShipApiService shipApiService;
    @Inject
    Provider<TankTransferListEntryComponent> tankTransferListEntryComponentProvider;

    public ObservableList<Fleet> fleets = FXCollections.observableArrayList();
    Fleet selectedFleet;
    Fleet originalFleet;
    private final Set<Ship> selectionModel = new CopyOnWriteArraySet<>();

    @Inject
    public TankTransferComponent() {
    }

    public void initFleet() {
        fleets.clear();
        fleets.addAll(gameService.getOwnUser().getEmpire().getFleets().stream().filter(fleet -> !fleet.equals(originalFleet)).filter(fleet -> fleet.getLocation().equals(originalFleet.getLocation())).toList());
        updateTransferButtonState();
    }

    @OnRender
    public void renderFleet() {
        selectDivisionComboBox.setItems(fleets);
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                Empire.PROPERTY_FLEETS,
                evt -> Platform.runLater(() -> {
                    fleets.clear();
                    fleets.addAll(gameService.getOwnUser().getEmpire().getFleets().stream().filter(fleet -> !fleet.equals(originalFleet)).toList());
                    updateTransferButtonState();
                }));
        selectDivisionComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Fleet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        selectDivisionComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Fleet fleet, boolean empty) {
                super.updateItem(fleet, empty);
                if (empty || fleet == null) {
                    setText(null);
                } else {
                    setText(fleet.getName());
                }
            }
        });
    }

    public void setData(Fleet fleet, String shipType) {
        originalFleet = fleet;
        List<AbstractMap.SimpleEntry<Integer, Ship>> ships = new ArrayList<>();
        int counter = 1;
        for (Ship ship : fleet.getShips()) {
            if (ship.getType().equals(shipType)) {
                ships.add(new AbstractMap.SimpleEntry<>(counter, ship));
                counter += 1;
            }
        }
        // Update the ObservableList for the ListView
        ObservableList<AbstractMap.SimpleEntry<Integer, Ship>> observableShips = FXCollections.observableArrayList(ships);
        tankList.setItems(observableShips);
        initFleet();
    }

    private TankTransferListEntryComponent createTankTransferListEntryComponent() {
        TankTransferListEntryComponent tankTransferListEntryComponent = tankTransferListEntryComponentProvider.get();
        tankTransferListEntryComponent.setSelectionModel(selectionModel);
        return tankTransferListEntryComponent;
    }

    @OnRender
    public void onRender(){
        tankList.setCellFactory(param -> new ComponentListCell<>(app, this::createTankTransferListEntryComponent));
    }

    public void selectDivision() {
        selectedFleet = selectDivisionComboBox.getValue();
    }

    public void cancel() {
        tankTransferHolder.getChildren().clear();
    }

    public void transfer() {
        Fleet fleet = selectedFleet;
        Map<String, Integer> fleetToTransferSize = fleet.getSize();
        Map<String, Integer> originalFleetSize = originalFleet.getSize();

        for (Ship ship: selectionModel) {
            fleetToTransferSize.merge(ship.getType(), 1, Integer::sum);
            if (originalFleetSize.get(ship.getType()) != null && originalFleetSize.get(ship.getType()) > 0) {
                originalFleetSize.merge(ship.getType(), -1, Integer::sum);
            }
            subscriber.subscribe(shipApiService.updateShip(
                    gameService.getGame().getId(),
                    ship.getFleet().get_id(),
                    ship.get_id(),
                    new ShipDto(
                            null,
                            null,
                            null,
                            null,
                            null,
                            fleet.get_id(),
                            null,
                            null,
                            null
                    )));
        }
        subscriber.subscribe(fleetsApiService.updateFleet(gameService.getGame().getId(), fleet.get_id(), new UpdateFleetDto(null, fleetToTransferSize)));
        subscriber.subscribe(fleetsApiService.updateFleet(gameService.getGame().getId(), originalFleet.get_id(), new UpdateFleetDto(null, originalFleetSize)));

        tankTransferHolder.getChildren().clear();
    }

    private void updateTransferButtonState() {
        transferButton.setDisable(fleets.isEmpty());
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
