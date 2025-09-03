package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.GameSystemsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.ws.EventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Component(view = "FractionList.fxml")
public class FractionListComponent extends VBox {

    @FXML
    ListView<Fraction> fractionList;
    @Inject
    Subscriber subscriber;
    @Inject
    GameSystemsApiService gameSystemsApiService;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Param("game")
    Game game;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    EventListener eventListener;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    App app;
    @Inject
    GameService gameService;
    @Inject
    ImageCache imageCache;

    private final ObservableList<Fraction> fractions = FXCollections.observableArrayList();

    @Inject
    public FractionListComponent() {
    }

    @OnInit
    public void onInit() {
        if (gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null) {
            fractions.addAll(gameService.getOwnUser().getEmpire().getFractions());
            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                    Empire.PROPERTY_FRACTIONS,
                    evt -> Platform.runLater(() -> fractions.add((Fraction) evt.getNewValue())));
        }
    }

    @OnRender
    public void onRender() {
        // Set Fractions to list view
        fractionList.setItems(fractions);

        setCustomCellFactory();

        // Adjust font size based on window height
        if (app.stage() != null) {
            subscriber.listen(app.stage().heightProperty(), (observable, oldValue, newValue) -> adjustFontSize(newValue.doubleValue()));
            // Initial font size adjustment
            adjustFontSize(app.stage().getHeight());
        }

    }

    private void setCustomCellFactory() {
        fractionList.setCellFactory(param -> {
            ListCell<Fraction> cell = new ListCell<>() {

                @Override
                protected void updateItem(Fraction item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Set Fraction name for every cell
                        HBox container = new HBox();
                        container.setSpacing(5);

                        ImageView fractionImage = new ImageView(imageCache.get("images/fractions/" + item.getType() + ".png"));
                        fractionImage.setFitHeight(50);
                        fractionImage.setFitWidth(60);

                        container.getChildren().add(fractionImage);

                        VBox infos = new VBox();

                        Text name = new Text(item.getName() == null ? resources.getString("home.fraction") : item.getName());
                        name.getStyleClass().setAll("smallText");

                        ImageView hpImage = new ImageView(imageCache.get("icons/hp.png"));

                        hpImage.setFitHeight(15);
                        hpImage.setFitWidth(15);

                        Text hp = new Text(resources.getString("planNewFleet.hp") + " " + item.getHealth() + "/NaN");
                        hp.getStyleClass().setAll("smallText");

                        HBox hpContainer = new HBox();

                        hpContainer.setSpacing(5);

                        hpContainer.getChildren().addAll(hpImage, hp);

                        Text defense = new Text(resources.getString("planNewFleet.defense") + " NaN");
                        defense.getStyleClass().setAll("smallText");

                        ImageView defenseImage = new ImageView(imageCache.get("icons/shield_icon.png"));

                        defenseImage.setFitHeight(12);
                        defenseImage.setFitWidth(12);

                        HBox defenseContainer = new HBox();

                        defenseContainer.setSpacing(5);

                        defenseContainer.getChildren().addAll(defenseImage, defense);

                        infos.getChildren().addAll(name, hpContainer, defenseContainer);

                        container.getChildren().add(infos);

                        setGraphic(container);

                        Popup popup = createPopup(item);

                        setOnMouseEntered(event -> popup.show(this, event.getScreenX() + 10, event.getScreenY() + 10));

                        setOnMouseExited(event -> popup.hide());

                        initTextFields(item, hp, defense);
                    }
                }
            };

            // Bind cell height to 10% of the list view height
            subscriber.bind(cell.prefHeightProperty(), fractionList.heightProperty().divide(10));

            return cell;
        });
    }

    private void initTextFields(Fraction item, Text hp, Text defense) {
        AtomicInteger maxHealth = new AtomicInteger();
        subscriber.subscribe(gameSystemsApiService.getFractionMaxHealth(game.getId(), gameService.getOwnUser().getEmpire().get_id(), item.get_id()), response -> {
            if (response.isSuccessful() && response.body() != null) {
                hp.setText(resources.getString("planNewFleet.hp") + " " + item.getHealth() + "/" + response.body().total());
                maxHealth.set(response.body().total());
            }
        });

        subscriber.subscribe(gameSystemsApiService.getFractionDefense(game.getId(), gameService.getOwnUser().getEmpire().get_id(), item.get_id()), response -> {
            if (response.isSuccessful() && response.body() != null){
                defense.setText(resources.getString("planNewFleet.defense") + " " + response.body().total());
            }
        });

        subscriber.listen(item.listeners(), Fraction.PROPERTY_HEALTH, evt -> hp.setText(resources.getString("planNewFleet.hp") + " " + evt.getNewValue() + "/" + maxHealth));
    }

    private Popup createPopup(Fraction item) {
        Map<String, Integer> d = item.getDistricts();

        Popup popup = new Popup();

        VBox container = new VBox();

        container.setPadding(new Insets(10));

        container.setStyle("-fx-border-color: #CCB526; -fx-border-width: 2px; -fx-border-style: solid;");

        Text title = new Text(item.getName() == null ? resources.getString("home.fraction") : item.getName());

        Text info = new Text(
                "\n" + resources.getString("fractionList.position") + ": " + item.getX() + "/" + item.getY() +
                        "\n" + resources.getString("fractionList.population") + ": " + item.getPopulation() +
                        "\n" + resources.getString("fractionList.capacity") + ": " + item.getCapacity() +
                        "\n" + resources.getString("fractionList.districts") + ": " + d.values().stream().reduce(0, Integer::sum) +
                        "\n" + resources.getString("fractionList.buildings") + ": " + item.getBuildings().size());
        title.getStyleClass().setAll("systemListTooltipTitle");
        container.getStyleClass().setAll("systemListTooltip");
        info.getStyleClass().setAll("systemListTooltip");

        container.getChildren().addAll(title, info);

        popup.getContent().addAll(container);

        return popup;
    }

    public void onItemClicked() {
        Fraction selectedItem = fractionList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            app.show("/inGame/FractionDetails", Map.of("game", game, "fraction", selectedItem));
        }
    }

    private void adjustFontSize(double height) {
        double fontSize = Math.min(height / 30, 30);
        fractionList.setStyle("-fx-font-size: " + fontSize + "px;");
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
