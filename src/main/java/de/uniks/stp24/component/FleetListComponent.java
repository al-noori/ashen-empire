package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.service.GameService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "FleetList.fxml")
public class FleetListComponent extends VBox {

    @Inject
    App app;
    @FXML
    ListView<Fleet> fleetList;
    @Inject
    Provider<FleetListEntryComponent> fleetListEntryComponentProvider;
    public StackPane stackPane;
    @SubComponent
    @Inject
    FleetDetailsComponent fleetDetailsComponent;
    @SubComponent
    @Inject
    PlanNewFleetComponent planNewFleetComponent;
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Subscriber subscriber;

    private final ObservableList<Fleet> fleets = FXCollections.observableArrayList();

    @Inject
    public FleetListComponent() {
    }

    @OnRender
    public void onRender() {
        if (gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null) {
            fleets.addAll(gameService.getOwnUser().getEmpire().getFleets());
            fleetList.setItems(fleets);

            fleetList.setCellFactory(list -> new ComponentListCell<>(app, () -> app.initAndRender(fleetListEntryComponentProvider.get(), Map.of("fleets", fleets))));

            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                    Empire.PROPERTY_FLEETS,
                    evt -> Platform.runLater(() -> {
                        fleets.clear();
                        fleets.addAll(gameService.getOwnUser().getEmpire().getFleets());
                    }));

        }
    }

    public void onItemClicked() {
        Fleet selectedFleet = fleetList.getSelectionModel().getSelectedItem();
        // Clear selection
        fleetList.getSelectionModel().select(null);

        // Return if no fleet is selected
        if (selectedFleet == null) return;

        fleetDetailsComponent.setData(selectedFleet);
        fleetDetailsComponent.stackPane = stackPane;
        stackPane.getChildren().add(fleetDetailsComponent);
    }

    public void planNewFleet() {
        planNewFleetComponent.stackPane = stackPane;
        stackPane.getChildren().add(planNewFleetComponent);
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
