package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Ship;
import de.uniks.stp24.rest.ShipApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import java.util.AbstractMap;
import java.util.ResourceBundle;

@Component(view = "TankDetailsListEntry.fxml")
public class TankDetailsListEntryComponent extends HBox implements ReusableItemComponent<AbstractMap.SimpleEntry<Integer, Ship>> {

    @FXML
    public Button removeButton;
    @FXML
    private Text tankNumber;
    @FXML
    public Text healthNumber;
    @FXML
    public Text experienceNumber;
    @Inject
    public TankService tankService;
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    public ShipApiService shipApiService;
    @Inject
    Subscriber subscriber;
    Ship ship;
    int index = 0;

    @Inject
    public TankDetailsListEntryComponent() {
    }

    @Override
    public void setItem(@NotNull AbstractMap.SimpleEntry<Integer, Ship> shipEntry) {
        this.index = shipEntry.getKey();
        ship = shipEntry.getValue();
        this.setId(ship.get_id());
        tankNumber.setText(String.valueOf(index));
        subscriber.subscribe(tankService.getMaxHealth(ship.getType()),
                maxHealth -> Platform.runLater(() -> {
                    int roundedMaxHealth = (int) Math.ceil(maxHealth);
                    healthNumber.setText(tankService.getTankHealth(ship.getHealth()) + roundedMaxHealth);
                }),
                throwable -> Platform.runLater(() -> healthNumber.setText("/Unknown"))
        );
        experienceNumber.setText(String.valueOf(ship.getExperience()));
    }

    public void removeTank() {
        subscriber.subscribe(shipApiService.deleteShip(gameService.getGame().getId(), ship.getFleet().get_id(), ship.get_id()));
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}