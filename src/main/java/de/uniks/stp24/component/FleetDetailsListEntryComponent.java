package de.uniks.stp24.component;

import de.uniks.stp24.dto.ShipGroup;
import de.uniks.stp24.model.game.*;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.rest.ShipApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.TankService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "FleetDetailsListEntry.fxml")
public class FleetDetailsListEntryComponent extends HBox implements ReusableItemComponent<ShipGroup> {

    public VBox tankTransferHolder;

    @FXML
    Button openTransferButton;
    @FXML
    Button plusButton;
    @FXML
    Button minusButton;
    @FXML
    ImageView tankImg;
    @FXML
    Text speed;
    @FXML
    Text atk;
    @FXML
    Text def;
    @FXML
    Text shipName;
    private ShipGroup currentShip;

    @Inject
    ImageCache imageCache;
    @Inject
    PresetsApiService presetsApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    TankService tankService;
    @SubComponent
    @Inject
    TankTransferComponent tankTransferComponent;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    GameService gameService;
    @Inject
    JobService jobService;
    @Inject
    ShipApiService shipApiService;

    @SubComponent
    @Inject
    TankDetailsComponent tankDetailsComponent;

    @Inject
    public FleetDetailsListEntryComponent() {
    }

    @Override
    public void setItem(@NotNull ShipGroup ship) {
        currentShip = ship;
        int shipsInConstruction = (int) ship.fleet().getLocation().getJobs().stream().filter(j -> j.getType().equals("ship") && j.getShip().equals(ship.type())).count();
        shipName.setText(tankService.getTankName(ship.type()) + " (" + ship.currentAmount() + ")" + "(" + shipsInConstruction + ")");
        tankImg.setImage(imageCache.get("ships/" + ship.type() + "_tank.png"));
        subscriber.subscribe(tankService.getShips()
                .flatMapIterable(shipDtos -> shipDtos)
                .filter(shipDto -> shipDto.id().equals(ship.type()))
                .firstElement()
                .toObservable(), shipDto -> {
            speed.setText(resources.getString("planNewFleet.speed") + " " + tankService.getTankSpeed(shipDto.speed()));
            String maxATK = shipDto.attack().get("default") == null ? resources.getString("planNewFleet.noGun") : String.valueOf(shipDto.attack().get("default"));
            atk.setText(resources.getString("planNewFleet.maxAtk") + " " + maxATK);
            def.setText(resources.getString("planNewFleet.maxDef") + " " + shipDto.defense().get("default"));


            Map<String, Integer> cost = shipDto.cost();
            plusButton.setDisable(!tankService.enoughResources(cost));

            subscriber.listen(ship.fleet().getEmpire().listeners(),
                    Empire.PROPERTY_RESOURCES,
                    evt -> Platform.runLater(() -> plusButton.setDisable(!tankService.enoughResources(cost))));
        });

        minusButton.setDisable(hasNoShipJobsOfType());
        subscriber.listen(ship.fleet().getLocation().listeners(),
                Fraction.PROPERTY_JOBS,
                evt -> Platform.runLater(() -> minusButton.setDisable(hasNoShipJobsOfType())));

        subscriber.listen(ship.fleet().listeners(),
                Fleet.PROPERTY_SHIPS,
                evt -> Platform.runLater(() -> {
                    int currentSize = (int) ship.fleet().getShips().stream().filter(s -> s.getType().equals(ship.type())).count();
                    int shipsInConstruction2 = (int) ship.fleet().getLocation().getJobs().stream().filter(j -> j.getType().equals("ship") && j.getShip().equals(ship.type())).count();
                    shipName.setText(tankService.getTankName(ship.type()) + " (" + currentSize + ")" + "(" + shipsInConstruction2 + ")");
                }));

        updateTransferButtonState();

        subscriber.listen(ship.fleet().getLocation().listeners(),
                Fraction.PROPERTY_JOBS,
                evt -> Platform.runLater(() -> {
                    int shipsInConstruction2 = (int) ship.fleet().getLocation().getJobs().stream().filter(j -> j.getType().equals("ship") && j.getShip().equals(ship.type())).count();
                    shipName.setText(tankService.getTankName(ship.type()) + " (" + ship.currentAmount() + ")" + "(" + shipsInConstruction2 + ")");
                }));

        if (currentShip.fleet().getLocation().getFleets().size() < 2) {
            openTransferButton.setDisable(true);
        } else {
            openTransferButton.setDisable(false);
        }
    }

    public void transfer() {
        // Open Tank transfer
        tankTransferComponent.tankTransferName.setText(resources.getString("tank.transfer") + ": " + tankService.getTankName(currentShip.type()));
        tankTransferComponent.tankTransferHolder = tankTransferHolder;
        tankTransferComponent.setData(currentShip.fleet(), currentShip.type());
        tankTransferHolder.getChildren().clear();
        tankTransferHolder.getChildren().add(tankTransferComponent);
    }

    public void showTankDetails() {
        tankDetailsComponent.tankDetailsName.setText(resources.getString("tank.details") + ": " + tankService.getTankName(currentShip.type()));
        tankDetailsComponent.tankDetailsHolder = tankTransferHolder;
        tankDetailsComponent.setData(currentShip.fleet(), currentShip.type());
        tankTransferHolder.getChildren().clear();
        tankTransferHolder.getChildren().add(tankDetailsComponent);
    }

    public void plus() {
        Fleet fleet = currentShip.fleet();
        subscriber.subscribe(jobService.createShipJob(fleet.get_id(), fleet.getLocation().get_id(), currentShip.type()));
    }

    public void minus() {
        currentShip.fleet().getLocation().getJobs().stream()
                .filter(job -> job.getType().equals("ship") && job.getShip().equals(currentShip.type()))
                .map(Jobs::get_id)
                .findFirst()
                .ifPresent(jobToBeDeleted -> subscriber.subscribe(jobService.deleteJob(jobToBeDeleted)));

    }

    private boolean hasNoShipJobsOfType() {
        return currentShip.fleet().getLocation().getJobs().stream()
                .noneMatch(job -> job.getType().equals("ship") && job.getShip().equals(currentShip.type()));
    }

    private void updateTransferButtonState() {
        if (currentShip != null && currentShip.fleet() != null) {
            boolean canTransfer = currentShip.fleet().getEmpire().getFleets().stream()
                    .anyMatch(f -> !f.equals(currentShip.fleet()) && f.getLocation().equals(currentShip.fleet().getLocation()));
            openTransferButton.setDisable(!canTransfer);
        } else {
            openTransferButton.setDisable(true);
        }
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
