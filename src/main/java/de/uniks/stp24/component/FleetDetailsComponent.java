package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.ShipGroup;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.service.GameService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "FleetDetails.fxml")
public class FleetDetailsComponent extends AnchorPane {

    @FXML
    VBox editNameHolder;
    @Inject
    App app;
    public StackPane stackPane;
    @FXML
    ListView<ShipGroup> fleetDetailsList;
    @FXML
    Text tankfactoryAvailable;
    @FXML
    Text currentSize;
    @FXML
    Text plannedSize;
    @FXML
    Text currentLocation;
    @FXML
    Text fleetName;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    @SubComponent
    @Inject
    EditDivisionNameComponent editDivisionNameComponent;
    @SubComponent
    @Inject
    PlanNewTanksComponent planNewTanksComponent;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    Provider<FleetDetailsListEntryComponent> fleetDetailsListEntryComponentProvider;
    private final ObservableList<ShipGroup> ships = FXCollections.observableArrayList();
    private Fleet fleet;

    @Inject
    public FleetDetailsComponent() {
    }

    public void setData(Fleet fleet) {
        this.fleet = fleet;
        fleetName.setText(fleet.getName());

        System.out.println(fleet.getSize());

        int size = 0;
        for (Integer value : fleet.getSize().values()) {
            size += value;
        }
        plannedSize.setText(resources.getString("fleetDetails.plannedSize") + " " + size);

        currentSize.setText(resources.getString("fleetDetails.currentSize") + " " + fleet.getShips().size());

        currentLocation.setText(resources.getString("fleetDetails.location") + " " + (fleet.getLocation().getName() == null ? resources.getString("home.fraction") : fleet.getLocation().getName()));

        boolean tankFactoryAvailableCheck = fleet.getLocation().getBuildings().contains("shipyard") && fleet.getEmpire() == gameService.getOwnUser().getEmpire();

        tankfactoryAvailable.setText(resources.getString("fleetDetails.tankFactory") + " " + (tankFactoryAvailableCheck ? resources.getString("fleetDetails.tankFactory.true") : resources.getString("fleetDetails.tankFactory.false")));

        if (tankFactoryAvailableCheck) {
            tankfactoryAvailable.setStyle("-fx-fill: #00FFBF");
        } else {
            tankfactoryAvailable.setStyle("-fx-fill: #c15652");
        }

        ships.clear();

        for (Map.Entry<String, Integer> ship : fleet.getSize().entrySet()) {
            int currentSize = (int) fleet.getShips().stream().filter(s -> s.getType().equals(ship.getKey())).count();
            ships.add(new ShipGroup(fleet, ship.getKey(), currentSize));
        }

        fleetDetailsList.setItems(ships);

        subscriber.listen(fleet.listeners(),
                Fleet.PROPERTY_SIZE,
                evt -> Platform.runLater(() -> {
                    int size2 = 0;
                    for (Integer value : fleet.getSize().values()) {
                        size2 += value;
                    }
                    plannedSize.setText(resources.getString("fleetDetails.plannedSize") + " " + size2);
                }));

        subscriber.listen(fleet.listeners(),
                Fleet.PROPERTY_SHIPS,
                evt -> Platform.runLater(() -> {
                    currentSize.setText(resources.getString("fleetDetails.currentSize") + " " + fleet.getShips().size());
                    ships.clear();
                    for (Map.Entry<String, Integer> ship : fleet.getSize().entrySet()) {
                        int currentSize = (int) fleet.getShips().stream().filter(s -> s.getType().equals(ship.getKey())).count();
                        ships.add(new ShipGroup(fleet, ship.getKey(), currentSize));
                        System.out.println("refresh");
                    }
                }));

        fleetDetailsList.setCellFactory(param -> new ComponentListCell<>(app, this::createFleetDetailsListEntryComponent));
    }

    private FleetDetailsListEntryComponent createFleetDetailsListEntryComponent() {
        FleetDetailsListEntryComponent component = fleetDetailsListEntryComponentProvider.get();
        editNameHolder.getChildren().clear();
        component.tankTransferHolder = editNameHolder;
        return component;
    }

    public void cancel() {
        stackPane.getChildren().remove(this);
    }

    public void edit() {
        // Open edit name dialog
        editDivisionNameComponent.fleet = fleet;
        editDivisionNameComponent.editNameHolder = editNameHolder;
        editNameHolder.getChildren().clear();
        editNameHolder.getChildren().add(editDivisionNameComponent);
    }

    public void save() {
        // Save changes
        stackPane.getChildren().remove(this);
    }

    public void planNewTanks() {
        // Plan new tanks
        planNewTanksComponent.planNewTanksHolder = editNameHolder;
        planNewTanksComponent.setData(fleet);
        editNameHolder.getChildren().clear();
        editNameHolder.getChildren().add(planNewTanksComponent);
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
