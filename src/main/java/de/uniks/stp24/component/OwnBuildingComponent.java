package de.uniks.stp24.component;

import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.service.BuildingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@Component(view = "OwnBuilding.fxml")
public class OwnBuildingComponent extends HBox implements ReusableItemComponent<BuildingDto> {
    @FXML
    public Button deleteButton;
    @Inject
    Subscriber subscriber;
    @Inject
    BuildingService buildingService;
    @Inject
    @SubComponent
    @FXML
    BuildingComponent buildingComponent;
    @Param("fraction")
    Fraction fraction;
    @Param("game")
    Game game;
    private BuildingDto currentBuilding;

    @Inject
    public OwnBuildingComponent() {
    }

    public void delete() {
        subscriber.subscribe(buildingService.removeBuilding(currentBuilding, game, fraction));
    }

    @Override
    public void setItem(@NotNull BuildingDto buildingDto) {
        currentBuilding = buildingDto;
        buildingComponent.setItem(buildingDto);
    }
}
