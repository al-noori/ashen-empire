package de.uniks.stp24.component;

import de.uniks.stp24.dto.UpdateFleetDto;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.rest.FleetsApiService;
import de.uniks.stp24.service.GameService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "EditDivisionName.fxml")
public class EditDivisionNameComponent extends VBox {

    public VBox editNameHolder;
    @FXML
    Text nameExistsLabel;
    @FXML
    TextField editedName;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    @Inject
    FleetsApiService fleetsApiService;
    @Inject
    @Resource
    ResourceBundle resources;
    public Fleet fleet;

    @Inject
    public EditDivisionNameComponent() {
    }

    public void cancel() {
        nameExistsLabel.setVisible(false);
        editNameHolder.getChildren().clear();
    }

    public void save() {
        nameExistsLabel.setVisible(false);
        if (editedName.getText().isEmpty()) {
            editedName.setStyle("-fx-border-color: red");
            return;
        } else if (gameService.getOwnUser().getEmpire().getFleets().stream().anyMatch(fleet -> fleet.getName().equals(editedName.getText()))) {
            editedName.setStyle("-fx-border-color: red");
            nameExistsLabel.setVisible(true);
            return;
        }
        // Save changes
        subscriber.subscribe(fleetsApiService.updateFleet(gameService.getGame().getId(), fleet.get_id(), new UpdateFleetDto(editedName.getText(), null)));
        editNameHolder.getChildren().clear();
    }
}
