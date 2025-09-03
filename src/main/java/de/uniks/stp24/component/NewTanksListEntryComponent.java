package de.uniks.stp24.component;

import de.uniks.stp24.dto.PresetShipDto;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.PlanNewFleetService;
import de.uniks.stp24.service.TankService;
import de.uniks.stp24.util.ui.CustomTooltip;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;
import org.fulib.fx.controller.Subscriber;
import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "NewTanksListEntry.fxml")
public class NewTanksListEntryComponent extends HBox implements ReusableItemComponent<PresetShipDto> {

    @FXML
    Button plusButton;
    @FXML
    Button minusButton;
    @FXML
    ImageView tankImg;
    @FXML
    Text tankName;
    @FXML
    Text speed;
    @FXML
    Text hp;
    @FXML
    Text atk;
    @FXML
    Text def;
    @FXML
    Text numberOfTanks;
    @Inject
    TankService tankService;
    @Inject
    ImageCache imageCache;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    PlanNewFleetService planNewFleetService;
    @Inject
    PresetsApiService presetsApiService;

    @Inject
    public NewTanksListEntryComponent() {
    }

    @Override
    public void setItem(@NotNull PresetShipDto ship) {
        this.setId(ship.id());
        tankName.setText(tankService.getTankName(ship.id()));
        tankImg.setImage(imageCache.get("ships/" + ship.id() + "_tank.png"));
        speed.setText(resources.getString("planNewFleet.speed") + " " + tankService.getTankSpeed(ship.speed()));
        hp.setText(resources.getString("planNewFleet.hp") + " " + ship.health());

        String maxATK = ship.attack().get("default") == null ? "0" : String.valueOf(ship.attack().get("default"));
        atk.setText(resources.getString("planNewFleet.maxAtk") + " " + maxATK);
        def.setText(resources.getString("planNewFleet.maxDef") + " " + ship.defense().get("default"));


        subscriber.subscribe(tankService.getBuildTime(ship.id()), buildTime -> Platform.runLater(() -> {
            int roundedBuildTime = (int) Math.ceil(buildTime);
            String buildTimeText = String.format("Build Time: %d months", roundedBuildTime);
            Text buildTimeTextNode = new Text(buildTimeText);
            buildTimeTextNode.getStyleClass().add("yellow-text-small-underlined");

            VBox tooltipContent = new VBox();
            tooltipContent.getChildren().addAll(buildTimeTextNode);

            CustomTooltip tooltip = new CustomTooltip(tooltipContent);
            tooltip.getStyleClass().clear();
            tooltip.getStyleClass().add("tooltip-round");
            Tooltip.install(plusButton, tooltip);
        }));
    }

    public void increaseNumberOfTanks() {
        int currentNumberOfTanks = Integer.parseInt(numberOfTanks.getText());
        numberOfTanks.setText(String.valueOf(currentNumberOfTanks + 1));
        planNewFleetService.incrementShipCounter(this.getId());
    }

    public void decreaseNumberOfTanks() {
        int currentNumberOfTanks = Integer.parseInt(numberOfTanks.getText());
        if (currentNumberOfTanks > 0) {
            numberOfTanks.setText(String.valueOf(currentNumberOfTanks - 1));
            planNewFleetService.decrementShipCounter(this.getId());
        }
    }

}