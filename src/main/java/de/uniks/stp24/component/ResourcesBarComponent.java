package de.uniks.stp24.component;

import de.uniks.stp24.dto.AggregateItem;
import de.uniks.stp24.dto.AggregateResult;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.GameSystemsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.ws.EventListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

@Component(view = "ResourcesBar.fxml")
public class ResourcesBarComponent extends VBox {

    @FXML
    ImageView populationImg;
    @FXML
    ImageView energyImg;
    @FXML
    ImageView mineralsImg;
    @FXML
    ImageView foodImg;
    @FXML
    ImageView fuelImg;
    @FXML
    ImageView researchImg;
    @FXML
    ImageView alloysImg;
    @FXML
    ImageView consumer_goodsImg;
    @FXML
    ImageView creditsImg;
    @FXML
    Text credits;
    @FXML
    Text minerals;
    @FXML
    Text fuel;
    @FXML
    Text alloys;
    @FXML
    Text consumer_goods;
    @FXML
    Text research;
    @FXML
    Text food;
    @FXML
    Text energy;
    @FXML
    Text population;
    @Param("game")
    Game game;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    EventListener eventListener;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Inject
    GameSystemsApiService gameSystemsApiService;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    Map<String, Integer> deltaInit = new ConcurrentHashMap<>();

    @Inject
    @Resource
    ResourceBundle resource;

    @Inject
    public ResourcesBarComponent() {
    }

    @OnRender
    public void onRender() {
        setTextForResources();
        getNewChanges();
        if (gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null) {
            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                    Empire.PROPERTY_RESOURCES,
                    evt -> Platform.runLater(this::getNewChanges));
        }
    }

    private void createAllTooltips() {
        Platform.runLater(() -> {
            Map<String, Integer> resources = gameService.getOwnUser().getEmpire().getResources();
            createOneTooltip(creditsImg, credits, resource.getString("resourcesBar.credits"), resources.get("credits"), deltaInit.getOrDefault("credits", 0));
            createOneTooltip(populationImg, population, resource.getString("resourcesBar.population"), resources.get("population"), deltaInit.getOrDefault("population", 0));
            createOneTooltip(energyImg, energy, resource.getString("resourcesBar.energy"), resources.get("energy"), deltaInit.getOrDefault("energy", 0));
            createOneTooltip(mineralsImg, minerals, resource.getString("resourcesBar.minerals"), resources.get("minerals"), deltaInit.getOrDefault("minerals", 0));
            createOneTooltip(foodImg, food, resource.getString("resourcesBar.food"), resources.get("food"), deltaInit.getOrDefault("food", 0));
            createOneTooltip(fuelImg, fuel, resource.getString("resourcesBar.fuel"), resources.get("fuel"), deltaInit.getOrDefault("fuel", 0));
            createOneTooltip(researchImg, research, resource.getString("resourcesBar.research"), resources.get("research"), deltaInit.getOrDefault("research", 0));
            createOneTooltip(alloysImg, alloys, resource.getString("resourcesBar.alloys"), resources.get("alloys"), deltaInit.getOrDefault("alloys", 0));
            createOneTooltip(consumer_goodsImg, consumer_goods, resource.getString("resourcesBar.consumer_goods"), resources.get("consumer_goods"), deltaInit.getOrDefault("consumer_goods", 0));
        });
    }

    private void createOneTooltip(ImageView img, Text text, String name, Integer stored, Integer delta) {
        Popup popup = new Popup();

        VBox container = new VBox();

        container.setPadding(new Insets(10));

        container.setStyle("-fx-border-color: #CCB526; -fx-border-width: 2px; -fx-border-style: solid;");

        Text title = new Text(name);

        Text info = new Text(
                "\n" + resource.getString("resourcesBar.stored") + ": " + stored +
                        "\n" +
                        resource.getString("resourcesBar.monthly") + ": " + delta);

        title.getStyleClass().setAll("systemListTooltipTitle");
        container.getStyleClass().setAll("systemListTooltip");
        info.getStyleClass().setAll("systemListTooltip");

        container.getChildren().addAll(title, info);

        popup.getContent().addAll(container);

        setToolTipEvent(img, popup);
        setToolTipEvent(text, popup);

    }

    private void setToolTipEvent(Node node, Popup popup) {
        node.setOnMouseEntered(event -> popup.show(this, event.getScreenX() + 10, event.getScreenY() + 10));

        node.setOnMouseExited(event -> popup.hide());

        if (gameService.getOwnUser() != null) {
            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                    Empire.PROPERTY_RESOURCES,
                    evt -> Platform.runLater(popup::hide));
        }

    }

    private void setTextForResources() {
        Empire empire = gameService.getOwnUser().getEmpire();
        createAllTooltips();
        credits.setText(empire.getResources().get("credits") + " (" + deltaInit.getOrDefault("credits", 0) + ")");
        population.setText(empire.getResources().get("population") + " (" + deltaInit.getOrDefault("population", 0) + ")");
        energy.setText(empire.getResources().get("energy") + " (" + deltaInit.getOrDefault("energy", 0) + ")");
        minerals.setText(empire.getResources().get("minerals") + " (" + deltaInit.getOrDefault("minerals", 0) + ")");
        food.setText(empire.getResources().get("food") + " (" + deltaInit.getOrDefault("food", 0) + ")");
        fuel.setText(empire.getResources().get("fuel") + " (" + deltaInit.getOrDefault("fuel", 0) + ")");
        research.setText(empire.getResources().get("research") + " (" + deltaInit.getOrDefault("research", 0) + ")");
        alloys.setText(empire.getResources().get("alloys") + " (" + deltaInit.getOrDefault("alloys", 0) + ")");
        consumer_goods.setText(empire.getResources().get("consumer_goods") + " (" + deltaInit.getOrDefault("consumer_goods", 0) + ")");
    }

    public void getNewChanges() {
        if (gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null) {
            String id = gameService.getOwnUser().getEmpire().get_id();
            subscriber.subscribe(gameEmpiresApiService.getRessourceAggregate(game.getId(), id), response -> {
                AggregateResult result = response.body();
                if (result == null) {
                    return;
                }
                for (AggregateItem item : result.items()) {
                    deltaInit.put(item.variable().split("\\.")[1], item.subtotal());
                }
                setTextForResources();
            }, error -> System.out.println("Error: " + error));
        }
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
