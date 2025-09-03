package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.service.GameService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "FleetListEntry.fxml")
public class FleetListEntryComponent extends HBox implements ReusableItemComponent<Fleet> {

    @FXML
    Text status;
    @FXML
    Text amount;
    @FXML
    Text fleetName;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    private Fleet fleet;
    @Param("fleets")
    public ObservableList<Fleet> fleets;

    @Inject
    public FleetListEntryComponent() {
    }

    @Override
    public void setItem(@NotNull Fleet fleet) {
        this.fleet = fleet;
        fleetName.setText(fleet.getName());

        int size = 0;
        for (Integer value : fleet.getSize().values()) {
            size += value;
        }

        subscriber.listen(fleet.listeners(),
                Fleet.PROPERTY_NAME,
                evt -> Platform.runLater(() -> {
                    fleets.clear();
                    fleets.addAll(gameService.getOwnUser().getEmpire().getFleets());
                }));

        subscriber.listen(fleet.listeners(),
                Fleet.PROPERTY_SIZE,
                evt -> Platform.runLater(() -> {
                    fleets.clear();
                    fleets.addAll(gameService.getOwnUser().getEmpire().getFleets());
                }));

        amount.setText(fleet.getShips().size() + "/" + size);

        status.setText(resources.getString("fleetList.inactive"));
    }

    public void remove() {
        // remove the fleet from the game
        subscriber.subscribe(fleetsApiService.deleteFleet(gameService.getGame().getId(), fleet.get_id()));
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
