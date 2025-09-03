package de.uniks.stp24.component;

import de.uniks.stp24.service.TechnologiesServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.SubComponent;

import javax.inject.Inject;
import java.util.ResourceBundle;


@Component(view = "Technologies.fxml")
public class TechnologiesComponent extends AnchorPane {

    @FXML
    public BorderPane borderPane;
    @Inject
    @SubComponent
    TechnologyTreeComponent technologyTreeComponent;

    public StackPane stackPane;
    @FXML
    Button backButton;
    @Inject
    ResourceBundle resources;

    @Inject
    TechnologiesServices technologiesServices;

    @Inject
    public TechnologiesComponent() {
    }

    @FXML
    public void onBackButtonClicked() {
        stackPane.getChildren().remove(this);
    }
    public void rerenderTree() {
        technologyTreeComponent.render();
    }
}
