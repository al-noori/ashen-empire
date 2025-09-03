package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Ship;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
import java.util.Set;

@Component(view = "TankTransferListEntry.fxml")
public class TankTransferListEntryComponent extends HBox implements ReusableItemComponent<AbstractMap.SimpleEntry<Integer, Ship>> {

    @FXML
    CheckBox selectedTank;
    @FXML
    Text tankNumber;
    @FXML
    Text healthNumber;
    @FXML
    Text experienceNumber;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    TankService tankService;
    @Inject
    Subscriber subscriber;
    int index = 0;
    Set<Ship> selectionModel;

    @Inject
    public TankTransferListEntryComponent() {
    }

    public void setSelectionModel(Set<Ship> selectionModel) {
        this.selectionModel = selectionModel;
    }

    @Override
    public void setItem(@NotNull AbstractMap.SimpleEntry<Integer, Ship> shipEntry) {
        this.index = shipEntry.getKey();
        Ship ship = shipEntry.getValue();
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
        selectedTank.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                selectionModel.add(ship);
            } else {
                selectionModel.remove(ship);
            }
        });
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}