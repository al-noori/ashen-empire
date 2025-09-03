package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.BuildingService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.util.ui.CustomTooltip;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "Building.fxml")
public class BuildingComponent extends HBox implements ReusableItemComponent<BuildingDto> {

    @Inject
    App app;
    @FXML
    Label costLabel;
    @FXML
    Label productionLabel;
    @FXML
    Label upkeepLabel;
    @FXML
    Label buildingNameLabel;
    @FXML
    ImageView buildingImage;
    @Param("fraction")
    Fraction fraction;
    @Param("game")
    Game game;
    @FXML
    HBox costsBox;
    @FXML
    HBox productionBox;
    @FXML
    HBox upkeepBox;

    @Inject
    public PresetsApiService presetsApiService;
    @Inject
    public Subscriber subscriber;
    @Inject
    public BuildingService buildingService;
    @Inject
    public ImageCache imageCache;
    @Inject
    JobService jobService;
    @Inject
    Provider<ResourceVariableComponent> resourceVariableComponentProvider;
    @Inject
    ResourceBundle resourceBundle;

    @Inject
    @Resource
    ResourceBundle bundle;

    @Inject
    GameService gameService;

    @Inject
    public BuildingComponent() {
    }

    @Inject
    BuildingInformationComponent buildingInformationComponent;
    BuildingDto currentBuilding;

    @Override
    public void setItem(@NotNull BuildingDto building) {
        buildingNameLabel.setText(bundle.getString(building.id()));
        buildingImage.setImage(imageCache.get(pathHandler(building.id())));
        currentBuilding = building;
        List<String> types = List.of("production", "upkeep", "cost");
        for(String type: types){
            List<String> variables = (switch (type) {
                case "production" -> building.production();
                case "upkeep" -> building.upkeep();
                case "cost" -> building.cost();
                default -> Map.of();
            }).keySet().stream().map(s -> "buildings." + building.id() + "." + type + "." + s).toList();
            HBox box = switch (type) {
                case "production" -> productionBox;
                case "upkeep" -> upkeepBox;
                case "cost" -> costsBox;
                default -> null;
            };
            if (variables.isEmpty()) {
                if(box == null) continue;
                Label label = new Label(
                        resourceBundle.getString("no.regular")
                );
                label.setPrefHeight(20);
                label.setAlignment(Pos.TOP_CENTER);
                label.getStyleClass().add("yellow-text-small");
                box.getChildren().add(label);
                continue;
            }
            subscriber.subscribe(gameService.getVariables(variables), costs -> {
                if(box == null) return;
                box.getChildren().clear();
                costs.forEach((key, value) -> {
                    key = key.substring(key.lastIndexOf(".") + 1);
                    HBox hBox = new HBox();
                    try {
                        Image image = imageCache.get("icons/resources/" + key + ".png");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        hBox.getChildren().add(imageView);
                    } catch (IllegalArgumentException e) {
                        Label label = new Label(key + ": ");
                        label.getStyleClass().add("yellow-text-small");
                        hBox.getChildren().add(label);
                    }
                    Label label = new Label(Math.round(value.finalValue()) + "");
                    label.getStyleClass().add("yellow-text-small");
                    hBox.getChildren().add(label);
                    box.getChildren().add(hBox);
                    ResourceVariableComponent resourceVariableComponent = app.initAndRender(resourceVariableComponentProvider.get());
                    resourceVariableComponent.setItem(type, value);
                    CustomTooltip tooltip = new CustomTooltip(resourceVariableComponent);
                    Tooltip.install(hBox, tooltip);
                });
            });
        }
    }

    public String pathHandler(String id) {
        return "buildings/" + id + ".png";
    }



    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }


}
