package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.*;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.service.BuildingService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.ws.EventListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "SelectBuilding.fxml")
public class SelectBuildingComponent extends BuildingComponent implements ReusableItemComponent<BuildingDto> {
    @FXML
    @SubComponent
    @Inject
    BuildingComponent buildingComponent;
    @FXML
    Button buildButton;
    @Inject
    App app;
    @Inject
    EventListener eventListener;
    @Inject
    JobService jobService;
    @Inject
    BuildingService buildingService;
    @Inject
    ImageCache imageCache;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Inject
    BuildingInformationComponent buildingInformationComponent;
    @Inject
    GameService gameService;
    SimpleObjectProperty<Map<String, Integer>> currentResources = new SimpleObjectProperty<>();
    SimpleObjectProperty<Integer> currentCapacity = new SimpleObjectProperty<>();

    @Inject
    @Resource
    ResourceBundle resource;

    @Inject
    public SelectBuildingComponent() {
    }


    @Override
    public void setItem(@NotNull BuildingDto building) {
        if (gameService.amISpectator()) {
            buildButton.setDisable(true);
        }
        currentBuilding = building;
        buildingComponent.setItem(building);
        if (fraction.getEmpire() != null) {
            currentResources.set(fraction.getEmpire().getResources());
        } else {
            currentResources.set(Map.of());
        }
        int districtSize = fraction.getDistricts().values().stream().reduce(0, Integer::sum);
        int buildingSize = fraction.getBuildings().size();
        currentCapacity.setValue(fraction.getCapacity() - districtSize - buildingSize);

        buildButton.setDisable(!canMakeBuildingWith(currentResources.get(), currentBuilding) || currentCapacity.get() <= 0);
        currentCapacity.addListener((observable, oldValue, newValue)
                -> buildButton.setDisable(!canMakeBuildingWith(currentResources.get(), currentBuilding) || newValue <= 0));
        currentResources.addListener((observable, oldValue, newValue) -> buildButton.setDisable(!canMakeBuildingWith(newValue, currentBuilding) || currentCapacity.get() <= 0));
    }

    @OnInit
    public void init() {
        if (fraction.getEmpire() != null) {
            subscriber.subscribe(eventListener.listen("games." + game.getId() + ".empires." +
                                    fraction.getEmpire().get_id() + ".*",
                            GameEmpireDto.class),
                    event -> currentResources.set(event.data().resources()));
        }
        subscriber.subscribe(eventListener.listen("games." + game.getId() + ".systems." +
                                fraction.get_id() + ".*",
                        FractionDto.class),
                event -> {
                    int districtSize = event.data().districts().values().stream().reduce(0, Integer::sum);
                    int buildingSize = event.data().buildings().length;
                    currentCapacity.set(event.data().capacity() - districtSize - buildingSize);

                });
    }

    public void build() {
        subscriber.subscribe(jobService.createBuildingJob(fraction.get_id(), currentBuilding.id()));
    }


    private boolean canMakeBuildingWith(Map<String, Integer> resources, BuildingDto selectedBuilding) {
        for (Map.Entry<String, Integer> entry : selectedBuilding.cost().entrySet()) {
            String resourceType = entry.getKey();
            int requiredAmount = entry.getValue();

            if (resources.containsKey(resourceType)) {
                int availableAmount = resources.get(resourceType);
                if (requiredAmount > availableAmount) {
                    return false;
                }
            }
        }
        return true;
    }

}
