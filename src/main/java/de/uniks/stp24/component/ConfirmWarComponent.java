package de.uniks.stp24.component;

import de.uniks.stp24.dto.CreateWarDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.WarsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;

@Component(view = "ConfirmWar.fxml")
public class ConfirmWarComponent extends VBox {

    @FXML
    public VBox confirmVBox;
    @FXML
    public ImageView warImage;
    @FXML
    public Button declareWarButton;
    @FXML
    public Text infoText;

    @Inject
    ImageCache imageCache;

    @Inject
    WarsApiService warsApiService;

    @Inject
    Subscriber subscriber;

    @Inject
    GameService gameService;

    public VBox confirmHolder;

    public StackPane stackPane;

    public Game game;

    public Empire empire;

    public EmpireInformationComponent empireInformationComponent;

    public boolean declareOrEnd;

    @Inject
    public ConfirmWarComponent() {
    }

    @OnRender
    public void setText() {
        if (declareOrEnd) {
            infoText.setText("Confirm War and let your Enemy know.");
            warImage.setImage(imageCache.get("images/war_icon.png"));
        } else {
            infoText.setText("End the war and let your Enemy know.");
            warImage.setImage(imageCache.get("images/peace_icon.png"));
        }

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                0.0, 0.0,
                140.0, 0.0,
                140.0, 20.0,
                70.0, 30.0,
                0.0, 20.0
        );
        confirmVBox.setShape(polygon);
    }

    public void declareWarButton(){
        if (declareOrEnd) {
            subscriber.subscribe(warsApiService.createWar(game.getId(), new CreateWarDto(gameService.getOwnUser().getEmpire().get_id(),
                            empire.get_id(), "Test")), response -> {
                    }
            );

            empireInformationComponent.setButtons(true);
        } else {
            gameService.getOwnUser().getEmpire().getAttackingWars().forEach(war -> {
                if (war.getDefender().get_id().equals(empire.get_id())) {
                    subscriber.subscribe(warsApiService.deleteWar(game.getId(), war.get_id()), response -> {
                            },
                            error -> {
                                System.out.println("Error: " + error);
                            }
                    );
                }
            });
            empireInformationComponent.setButtons(false);
        }
    }

    public void clear() {
        this.game = null;
        this.empire = null;
        this.empireInformationComponent = null;
        this.declareOrEnd = false;
        this.confirmHolder = null;
    }
}
