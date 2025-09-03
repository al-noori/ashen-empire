package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Ship;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component(view = "TankDetails.fxml")
public class TankDetailsComponent extends VBox {

    @FXML
    public Text tankDetailsName;
    @Inject
    TankService tankService;

    @FXML
    ListView<AbstractMap.SimpleEntry<Integer, Ship>> tankDetailsList;

    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;

    public VBox tankDetailsHolder;

    @Inject
    public ResourceBundle resources;

    @Inject
    PresetsApiService presetsApiService;

    @Inject
    App app;

    @Inject
    public Provider<TankDetailsListEntryComponent> tankDetailsListEntryComponentProvider;

    @Inject
    public TankDetailsComponent() {
    }

    public void setData(Fleet fleet, String type) {
        subscriber.listen(fleet.listeners(), Fleet.PROPERTY_SHIPS, e -> Platform.runLater(() -> updateList(fleet, type)));
        updateList(fleet, type);
    }

    private void updateList(Fleet fleet, String type) {
        List<AbstractMap.SimpleEntry<Integer, Ship>> ships = new ArrayList<>();
        int counter = 1;
        for (Ship ship : fleet.getShips()) {
            if (ship.getType().equals(type)) {
                ships.add(new AbstractMap.SimpleEntry<>(counter, ship));
                counter += 1;
            }
        }
        ObservableList<AbstractMap.SimpleEntry<Integer, Ship>> observableShips = FXCollections.observableArrayList(ships);
        tankDetailsList.setItems(observableShips);
    }

    @OnRender
    public void onRender() {
        tankDetailsList.setCellFactory(param -> new ComponentListCell<>(app, this::createTankDetailsListEntryComponent));
    }

    public TankDetailsListEntryComponent createTankDetailsListEntryComponent() {
        return tankDetailsListEntryComponentProvider.get();
    }

    @FXML
    public void cancel() {
        VBox parent = (VBox) this.getParent();
        parent.getChildren().remove(this);
    }

}