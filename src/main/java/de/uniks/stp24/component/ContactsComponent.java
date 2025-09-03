package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "Contacts.fxml")
public class ContactsComponent extends AnchorPane {

    @FXML
    public ImageView CharacterImage;
    @FXML
    public ImageView EmpireImage;
    @FXML
    public ImageView EconomyImage;
    @FXML
    public Text EconomyText;
    @FXML
    public ImageView MiliaryImage;
    @FXML
    public Text MilitaryText;
    @FXML
    public ImageView TechImage;
    @FXML
    public Text TechText;
    @FXML
    public Button BackButton;
    @FXML
    public Button DiplomacyButton;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Text EmpireText;

    @FXML
    public HBox empiresBox;
    @FXML
    public TableView<User> playersBox;
    @FXML
    public TableColumn<User, ImageView> picTableColumn;
    @FXML
    public TableColumn<User, VBox> nameTableColumn;

    @SubComponent
    @Inject
    EmpireListsComponent empireListComponent;

    @SubComponent
    @Inject
    EmpireInformationComponent empireInformationComponent;

    @Inject
    GameEmpiresApiService empireApiService;

    @Inject
    GameService gameService;

    @Inject
    App app;

    @Inject
    ImageCache imageCache;

    @Inject
    EmpireApiService empireService;

    @Param("game")
    Game game;

    @Inject
    Subscriber subscriber;

    public StackPane stackPane;

    private int empire;
    private int portrait;

    private final ObservableList<Empire> empireList = FXCollections.observableArrayList();
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Inject
    @Resource
    ResourceBundle resource;


    @Inject
    public ContactsComponent() {
    }

    public void onBackButton() {
        stackPane.getChildren().remove(this);
    }

    public void onDiplomacyButton() {
        if (empireListComponent.empire != null) {
            stackPane.getChildren().remove(this);
            empireInformationComponent.stackPane = stackPane;
            stackPane.getChildren().add(app.initAndRender(empireInformationComponent, Map.of("game", game, "empire", empireListComponent.empire)));
        }
    }

    @OnInit
    public void onInit() {
        if (gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null){
            return;
        }
        subscriber.subscribe(empireService.getAll(gameService.getGame().getId()),
                response -> {
                    List<GameEmpireDto> empires = response.body();
                    assert empires != null;
                    for (GameEmpireDto empire1 : empires) {
                        if (empire1._id().equals(gameService.getOwnUser().getEmpire().get_id())) {
                            EmpireText.setText(empire1.name());
                            empire = empire1.flag() + 1;
                            portrait = empire1.portrait() + 1;
                        } else {
                            if (!empireList.contains(new Empire().setName(empire1.name()).set_id(empire1._id())
                                    .setColor(empire1.color()).setFlag(empire1.flag() + 1).setPortrait(empire1.portrait() + 1))) {
                                empireList.add(new Empire().setName(empire1.name()).set_id(empire1._id())
                                        .setColor(empire1.color()).setFlag(empire1.flag() + 1).setPortrait(empire1.portrait() + 1));
                            }
                            }
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

        game.getMembers().forEach(user -> {
            if (!users.contains(user)) {
                users.add(user);
            }
        });

        subscriber.subscribe(empireApiService.getMilitaryAggregate(game.getId(), gameService.getOwnUser().getEmpire().get_id()),
                response -> {
                    MilitaryText.setText("Military: " + response.body().total());
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

        subscriber.subscribe(empireApiService.getTechnologyAggregate(game.getId(), gameService.getOwnUser().getEmpire().get_id()),
                response -> {
                    TechText.setText("Technology: " + response.body().total());
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

        subscriber.subscribe(empireApiService.getEconomyAggregate(game.getId(), gameService.getOwnUser().getEmpire().get_id()),
                response -> {
                    EconomyText.setText("Economy: " + response.body().total());
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );
    }

    @OnRender
    public void setText() {
        EmpireImage.setImage(imageCache.get("flags/Flag" + empire + ".png"));
        CharacterImage.setImage(imageCache.get("portraits/" + portrait + ".png"));

        EconomyImage.setImage(imageCache.get("icons/economy_icon.png"));
        MiliaryImage.setImage(imageCache.get("icons/tank_icon_yellow.png"));
        TechImage.setImage(imageCache.get("icons/technology_icon.png"));

        empiresBox.getChildren().add(empireListComponent);

        playersBox.setItems(users);
        picTableColumn.setCellValueFactory(cellData -> {
                ImageView image = new ImageView();
                image.setFitHeight(50);
                image.setFitWidth(50);
                image.setImage(imageCache.get("portraits/" + (cellData.getValue().getEmpire().getPortrait() + 1) + ".png"));
                return new SimpleObjectProperty<>(image);
        });

        nameTableColumn.setCellValueFactory(cellData -> {
                VBox vbox = new VBox();
                Text name = new Text(cellData.getValue().getName());
                name.setStyle("-fx-text-fill: #FFD52C;\n" +
                        "    -fx-fill: #FFD52C;\n" +
                        "    -fx-font-family: 'VT323';\n" +
                        "    -fx-font-weight: 900;\n" +
                        "    -fx-font-size: 25;");
                Text empireListNo = new Text();
                empireListNo.setStyle("-fx-text-fill: #FFD52C;\n" +
                            "    -fx-fill: #FFD52C;\n" +
                            "    -fx-font-family: 'VT323';\n" +
                            "    -fx-font-weight: 900;\n" +
                            "    -fx-font-size: 25;");
                subscriber.subscribe(empireService.getAll(gameService.getGame().getId()),
                        response -> {List<GameEmpireDto> empires = response.body();
                    assert empires != null;
                    for (GameEmpireDto empire1 : empires) {
                        if (empire1._id().equals(cellData.getValue().getEmpire().get_id())) {
                            empireListNo.setText(empire1.name());
                        }
                    }
                    },
                        error -> {
                    System.out.println("Error: " + error);
                }
                );

                vbox.getChildren().addAll(name, empireListNo);
                return new SimpleObjectProperty<>(vbox);
}
        );
    }

    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }

}
