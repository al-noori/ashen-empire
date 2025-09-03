package de.uniks.stp24.component;

import de.uniks.stp24.dto.PresetShipDto;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.PlanNewFleetService;
import de.uniks.stp24.service.TankService;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "PlanNewFleetListEntry.fxml")
public class PlanNewFleetListEntryComponent extends HBox implements ReusableItemComponent<PresetShipDto> {

    @FXML
    ImageView tankTypeImg;
    @FXML
    Text tankName;
    @FXML
    Text speed;
    @FXML
    Text hp;
    @FXML
    Text maxAtk;
    @FXML
    Text maxDef;
    @FXML
    Text counter;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    PlanNewFleetService planNewFleetService;
    @Inject
    ImageCache imageCache;
    @Inject
    TankService tankService;

    @Inject
    public PlanNewFleetListEntryComponent() {
    }

    @Override
    public void setItem(@NotNull PresetShipDto ship) {
        this.setId(ship.id());
        tankName.setText(tankService.getTankName(ship.id()));
        speed.setText(resources.getString("planNewFleet.speed") + " " + tankService.getTankSpeed(ship.speed()));
        hp.setText(resources.getString("planNewFleet.hp") + " " + ship.health());
        String maxATK = ship.attack().get("default") == null ? resources.getString("planNewFleet.noGun") : String.valueOf(ship.attack().get("default"));
        maxAtk.setText(resources.getString("planNewFleet.maxAtk") + " " + maxATK);
        maxDef.setText(resources.getString("planNewFleet.maxDef") + " " + ship.defense().get("default"));
        int counterValue = planNewFleetService.getShipCounter(ship.id());
        counter.setText(String.valueOf(counterValue));

        tankTypeImg.setImage(imageCache.get("ships/" + ship.id() + "_tank.png"));
    }

    public void plus() {
        // Increase the number of ships
        counter.setText(String.valueOf(Integer.parseInt(counter.getText()) + 1));
        planNewFleetService.incrementShipCounter(this.getId());
    }

    public void minus() {
        // Decrease the number of ships
        int currentCounter = Integer.parseInt(counter.getText());
        if (currentCounter > 0) {
            counter.setText(String.valueOf(currentCounter - 1));
            planNewFleetService.decrementShipCounter(this.getId());
        }
    }
}
