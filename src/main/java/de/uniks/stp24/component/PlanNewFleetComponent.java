package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.CreateFleetDto;
import de.uniks.stp24.dto.PresetShipDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.PlanNewFleetService;
import de.uniks.stp24.service.TankService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "PlanNewFleet.fxml")
public class PlanNewFleetComponent extends AnchorPane {

    @FXML
    TextField fleetName;
    @FXML
    ChoiceBox<Fraction> locationChoices;
    @Inject
    App app;
    public StackPane stackPane;
    @FXML
    ListView<PresetShipDto> fleetDetailsList;
    @Inject
    Provider<PlanNewFleetListEntryComponent> planNewFleetListEntryComponentProvider;
    @Inject
    PresetsApiService presetsApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    GameService gameService;
    @Inject
    PlanNewFleetService planNewFleetService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    JobService jobService;
    @Inject
    TankService tankService;

    private final ObservableList<PresetShipDto> presetShips = FXCollections.observableArrayList();

    @Inject
    public PlanNewFleetComponent() {
    }

    @OnRender
    public void onRender() {
        // Render the component
        if (gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null) {
            return;
        }
        subscriber.subscribe(tankService.getShips().subscribe(presetShips::addAll, Throwable::printStackTrace));
        fleetDetailsList.setItems(presetShips);

        locationChoices.setItems(FXCollections.observableArrayList(
                gameService
                        .getOwnUser()
                        .getEmpire()
                        .getFractions()
                        .stream().filter(fraction -> fraction.getBuildings().contains("shipyard"))
                        .toList())
        );

        locationChoices.setConverter(new StringConverter<>() {
            @Override
            public String toString(Fraction fraction) {
                if (fraction != null) {
                    return fraction.getName() == null ? resources.getString("home.fraction") : fraction.getName();
                }
                return null;
            }

            @Override
            public Fraction fromString(String s) {
                return null;
            }
        });

        fleetDetailsList.setCellFactory(param -> new ComponentListCell<>(app, () -> planNewFleetListEntryComponentProvider.get()));

        planNewFleetService.resetShipCounters();
    }

    public void cancel() {
        stackPane.getChildren().remove(this);
    }

    public void save() {
        // Save changes
        if (fleetName.getText().isEmpty() || locationChoices.getValue() == null) {
            fleetName.setStyle("-fx-border-color: red");
            locationChoices.setStyle("-fx-border-color: red");
            return;
        }
        Map<String, Integer> size = planNewFleetService.getShipCounters();
        subscriber.subscribe(fleetsApiService.createFleet(gameService.getGame().getId(), new CreateFleetDto(
                        fleetName.getText(), locationChoices.getValue().get_id(), size)),
                fleetDto -> {
                    stackPane.getChildren().remove(this);

                    subscriber.subscribe(tankService.getShips()
                            .subscribe(
                                    ships -> {
                                        for (Map.Entry<String, Integer> entry : fleetDto.size().entrySet()) {
                                            for (int i = 0; i < entry.getValue(); i++) {
                                                if (tankService.resourcesCheck(entry.getKey(), ships)) {
                                                    subscriber.subscribe(jobService.createShipJob(fleetDto.location(), fleetDto.empire(), entry.getKey()));
                                                }
                                            }
                                        }
                                    },
                                    Throwable::printStackTrace
                            ));

                });

    }
}
