package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.CreateWarDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.VariablesApiService;
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
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "EmpireInformation.fxml")
public class EmpireInformationComponent extends VBox {

    @FXML
    public Text statusText;
    @FXML
    public ImageView flagImage;
    @FXML
    public ImageView populationImage;
    @FXML
    public Text populationText;
    @FXML
    public ImageView coinImage;
    @FXML
    public Text coinText;
    @FXML
    public ImageView militaryImage;
    @FXML
    public Text militaryText;
    @FXML
    public Button declareButton;
    @FXML
    public Button endButton;
    @FXML
    public Button backButton;
    @FXML
    public Text empireName;
    @FXML
    public VBox confirmBox;

    /*@SubComponent
    @Inject
    ContactsComponent contactsComponent;
*/

    @SubComponent
    @Inject
    ConfirmWarComponent confirmWarComponent;

    @Inject
    WarsApiService warsApiService;

    @Inject
    GameService gameService;

    @Inject
    App app;

    @Inject
    ImageCache imageCache;

    @Inject
    EmpireApiService empireService;

    @Inject
    VariablesApiService variablesApiService;

    @Param("game")
    Game game;

    @Param("empire")
    Empire empire;

    @Inject
    Subscriber subscriber;

    public StackPane stackPane;


    @Inject
    @Resource
    ResourceBundle resource;


    @Inject
    public EmpireInformationComponent() {
    }

    @OnInit
    public void onInit() {
    }

    @OnRender
    public void setText() {
        if (empire != null) {

            populationImage.setImage(imageCache.get("icons/resources/population.png"));
            coinImage.setImage(imageCache.get("icons/resources/credits.png"));
            militaryImage.setImage(imageCache.get("icons/resources/minerals.png"));
            empireName.setText(empire.getName());
            statusText.setText("PEACEFUL");
            endButton.setDisable(true);
            declareButton.setDisable(false);

            gameService.getOwnUser().getEmpire().getAttackingWars().forEach(war -> {
                if (war.getDefender().get_id().equals(empire.get_id())) {
                    statusText.setText("DEFENDER");
                    declareButton.setDisable(true);
                    endButton.setDisable(false);
                }
            });

            gameService.getOwnUser().getEmpire().getDefendingWars().forEach(war -> {
                if (war.getAttacker().get_id().equals(empire.get_id())) {
                    statusText.setText("ATTACKER");
                    declareButton.setDisable(true);
                    endButton.setDisable(true);
                }
            });


            if (empire != null) {
                flagImage.setImage(imageCache.get("flags/Flag" + (empire.getFlag() + 1) + ".png"));

                populationText.setText("");
                coinText.setText("");
                militaryText.setText("");
            }
        }


    }

    public void backButton() {
        stackPane.getChildren().remove(this);
        /*contactsComponent.stackPane = stackPane;
        stackPane.getChildren().add(app.initAndRender(contactsComponent, Map.of("game", game)));
         */
    }


    public void declareButton() {
        confirmWarComponent.clear();
        confirmWarComponent.declareOrEnd = true;
        confirmWarComponent.confirmHolder = confirmBox;
        confirmWarComponent.game = game;
        confirmWarComponent.empire = empire;
        confirmWarComponent.empireInformationComponent = this;
        confirmWarComponent.setText();
        confirmBox.getChildren().clear();
        confirmBox.getChildren().add(confirmWarComponent);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                0.0, 0.0,
                100.0, 0.0,
                140.0, 10.0,
                100.0, 20.0,
                0.0, 20.0
        );
        confirmBox.setShape(polygon);
    }

    public void setButtons(boolean war) {
        if (war) {
            statusText.setText("DEFENDER");
            endButton.setDisable(false);
            declareButton.setDisable(true);
        } else {
            statusText.setText("PEACEFUL");
            endButton.setDisable(true);
            declareButton.setDisable(false);
        }
        confirmBox.getChildren().clear();
        stackPane.getChildren().remove(this);
    }


    public void endButton() {
        confirmWarComponent.clear();
        confirmWarComponent.confirmHolder = confirmBox;
        confirmWarComponent.game = game;
        confirmWarComponent.empire = empire;
        confirmWarComponent.empireInformationComponent = this;
        confirmWarComponent.declareOrEnd = false;
        confirmWarComponent.setText();
        confirmBox.getChildren().clear();
        confirmBox.getChildren().add(confirmWarComponent);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                0.0, 0.0,
                100.0, 0.0,
                140.0, 10.0,
                100.0, 20.0,
                0.0, 20.0
        );
        confirmBox.setShape(polygon);
    }

    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }


}
