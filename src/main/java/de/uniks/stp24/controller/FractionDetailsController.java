package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.component.BuildingListComponent;
import de.uniks.stp24.component.DistrictComponent;
import de.uniks.stp24.component.JobsDetailsListComponent;
import de.uniks.stp24.component.SelectBuildingListComponent;
import de.uniks.stp24.dto.AggregateItem;
import de.uniks.stp24.dto.AggregateResult;
import de.uniks.stp24.dto.DistrictDto;
import de.uniks.stp24.dto.SystemUpgradeDto;
import de.uniks.stp24.model.game.*;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.GameSystemsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.UpgradeService;
import de.uniks.stp24.util.Tuple;
import de.uniks.stp24.ws.EventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@Title("Ingame- Fraction Details")
public class FractionDetailsController {

    @FXML
    VBox jobsDetailsListContainer;
    @FXML
    Text upgradeErrorText;
    @FXML
    Button exploreUpgradeButton;
    @FXML
    VBox selectBuildingListContainer;
    @Inject
    App app;
    @FXML
    Label StatusLabel;
    @FXML
    Label CapacityLabel;
    @FXML
    Label PopulationLabel;
    @FXML
    Label TypeLabel;
    @FXML
    Text consumer_goods;
    @FXML
    Text alloys;
    @FXML
    Text research;
    @FXML
    Text fuel;
    @FXML
    Text food;
    @FXML
    Text minerals;
    @FXML
    Text energy;
    @FXML
    Text population;
    @FXML
    Text credits;
    @FXML
    VBox buildingListContainer;
    @Inject
    Subscriber subscriber;
    @Inject
    EventListener eventListener;
    @FXML
    VBox FractionBox;
    @FXML
    VBox ressourcesBox;
    @FXML
    ListView<Tuple<DistrictDto, Fraction>> citiesList;
    @FXML
    Label citiesLabel;
    @FXML
    Label FractionLabel;
    @FXML
    Button backButton;
    @Inject
    ImageCache imageCache;

    @Param("game")
    Game game;
    @Param("fraction")
    Fraction fraction;

    @SubComponent
    @Inject
    BuildingListComponent buildingListComponent;
    @SubComponent
    @Inject
    JobsDetailsListComponent jobsDetailsListComponent;
    @SubComponent
    @Inject
    SelectBuildingListComponent selectBuildingListComponent;
    @SubComponent
    @Inject
    DistrictComponent districtComponent;
    @Inject
    Provider<DistrictComponent> districtComponentProvider;
    @Inject
    PresetsApiService presetsApiService;
    public final ObservableList<Tuple<DistrictDto, Fraction>> cities = FXCollections.observableArrayList();
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;
    @Inject
    GameSystemsApiService gameSystemsApiService;
    Map<String, Integer> deltaInit = new ConcurrentHashMap<>();
    @Inject
    JobService jobService;
    @Inject
    UpgradeService upgradeService;

    @Inject
    FractionDetailsController() {
    }

    public void initialize() {
        if (gameService.amISpectator()) {
            exploreUpgradeButton.setDisable(true);
        }
    }
    @OnInit
    void init() {
        subscriber.subscribe(presetsApiService.getDistricts(), response -> {
            if (response.isSuccessful()) {
                List<DistrictDto> districts = response.body();
                if (districts != null) {
                    districts.forEach(district -> cities.add(new Tuple<>(district, fraction)));
                    if (citiesLabel != null) {
                        citiesLabel.setText(resources.getString("fractionDetails.districts") + " (" + cities.size() + ")");
                    }
                }
            } else {
                System.out.println(response.errorBody());
            }
        });

    }

    @OnRender
    void renderBuildings() {
        citiesLabel.setText(resources.getString("fractionDetails.districts") + " (" + cities.size() + ")");
        citiesList.setItems(cities);
        citiesList.setCellFactory(list -> new ComponentListCell<>(app, districtComponentProvider));
        setTextForFraction();
        buildingListContainer.getChildren().addLast(buildingListComponent);

        if (fraction.getEmpire() == null || fraction.getEmpire().getOwner() != gameService.getOwnUser()) {
            if (fraction.getEmpire() != null || gameService.amISpectator()) exploreUpgradeButton.setVisible(false);
            else initUpgradeButton();
            ressourcesBox.getChildren().clear();
            return;
        }
        getNewChanges();
        if (gameService.getOwnUser() != null) {
            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_RESOURCES, evt -> Platform.runLater(this::getNewChanges));
        }
        initUpgradeButton();


        selectBuildingListContainer.getChildren().add(selectBuildingListComponent);
        jobsDetailsListContainer.getChildren().add(jobsDetailsListComponent);
        jobsDetailsListContainer.getStyleClass().add("details-list-view");

        addFractionsSubscribers();
        addPlayersSubscribers();
        checkWonTheGame();
        checkGameOver();
    }
    private void addFractionsSubscribers() {
            subscriber.listen(gameService.getOwnUser().listeners(), User.PROPERTY_EMPIRE, evt -> Platform.runLater(this::checkGameOver));
    }
    private void checkGameOver() {
        if (gameService.getOwnUser().getEmpire() == null) {
            back();
        }
    }
    private void addPlayersSubscribers() {
        for (User player : game.getMembers()) {
            subscriber.listen(player.listeners(), User.PROPERTY_EMPIRE, evt -> Platform.runLater(this::checkWonTheGame));
        }
    }

    private void checkWonTheGame() {
        if (game.getMembers().size() == 1) return;
        int members = 0;
        for (User player : game.getMembers()) {
            if (player.equals(gameService.getOwnUser())) continue;
            if (player.getEmpire() != null) {
                members++;
            }
        }
        if (members == 0) {
            back();
        }
    }

    private void initUpgradeButton() {
        exploreUpgradeButton.setText(upgradeService.getNextUpgradeText(fraction.getUpgrade()));

        // Disables button if upgrade in progress
        boolean disable =  fraction.getJobs().stream().anyMatch(job -> "upgrade".equals(job.getType()));
        exploreUpgradeButton.setDisable(disable);

        updateTooltip();
    }

    private void updateTooltip() {
        subscriber.subscribe(gameSystemsApiService.getFractionUpgrades(), response -> {
            if (upgradeService.getNextUpgradePreset(response, fraction) == null) return;
            SystemUpgradeDto upgrade = Objects.requireNonNull(upgradeService.getNextUpgradePreset(response, fraction));
            Platform.runLater(() -> {
                Popup popup = new Popup();

                VBox container = new VBox();

                container.getStyleClass().setAll("upgradeFractionTooltip");

                Text pop = new Text(
                        resources.getString("fractionDetails.populationGrowth") + ": " + upgrade.pop_growth()
                );

                container.getChildren().add(pop);

                getResourceAmount(upgrade.cost(), resources.getString("fractionDetails.cost") + ": ", container);
                getResourceAmount(upgrade.upkeep(), resources.getString("fractionDetails.upkeep") + ": ", container);

                Text capmul = new Text(
                        "\n\n" + resources.getString("fractionDetails.capacityMultiplier") + ": " + upgrade.capacity_multiplier() +
                                "\n\n" + resources.getString("fractionDetails.estimatedTime") + ": " + upgrade.upgrade_time() + " " + resources.getString("fractionDetails.months")
                );


                container.getChildren().add(capmul);

                pop.getStyleClass().setAll("upgradeFractionTooltip");
                capmul.getStyleClass().setAll("upgradeFractionTooltip");

                popup.getContent().add(container);


                exploreUpgradeButton.setOnMouseEntered(event -> popup.show(exploreUpgradeButton, event.getScreenX() + 10, event.getScreenY() + 10));
                exploreUpgradeButton.setOnMouseExited(event -> popup.hide());
            });
        });
    }

    private void getResourceAmount(Map<String, Integer> resources, String prefix, VBox container) {
        Text prefixText = new Text(prefix);
        prefixText.getStyleClass().setAll("upgradeFractionTextTool");
        HBox resourceContainer = new HBox();
        resourceContainer.getChildren().add(prefixText);
        if (resources == null) {
            return;
        }

        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resourceType = entry.getKey();
            int amount = entry.getValue();
            ImageView imageView = setImageFromChache("icons/resources/" + resourceType + ".png");
            Text text = new Text(amount + " " + this.resources.getString("resourcesBar." + resourceType) + " ");
            text.getStyleClass().setAll("upgradeFractionTextTool");
            resourceContainer.getChildren().addAll(imageView, text);
        }

        resourceContainer.getStyleClass().setAll("upgradeFractionTextTool");

        HBox filler = new HBox();
        filler.setMinHeight(50);

        container.getChildren().addAll(filler, resourceContainer);
    }

    private ImageView setImageFromChache(String path) {
        Image image = imageCache.get(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        return imageView;
    }

    public void back() {
        app.show("/inGame", Map.of("game", game));
    }

    private void setTextForFraction() {
        FractionLabel.setText(resources.getString("fractionDetails.fraction") + ": " + (fraction.getName() == null ? resources.getString("home.fraction") : fraction.getName()));
        StatusLabel.setText(resources.getString("fractionDetails.status") + ": " + upgradeService.getUpgradeText(fraction.getUpgrade()));
        CapacityLabel.setText(resources.getString("fractionDetails.capacity") + ": " + fraction.getCapacity());
        PopulationLabel.setText(resources.getString("fractionDetails.population") + ": " + fraction.getPopulation());
        TypeLabel.setText(resources.getString("fractionDetails.type") + ": " + resources.getString("fraction." + fraction.getType() ));
    }

    private void setTextForResources() {
        if (gameService.getOwnUser() == null) return;
        Empire empire = gameService.getOwnUser().getEmpire();
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
        if (gameService.getOwnUser() == null) return;
        setTextForResources();

        // Disables button if upgrade in progress
        exploreUpgradeButton.setDisable(fraction.getJobs().stream().anyMatch(job -> "upgrade".equals(job.getType())));

        String id = gameService.getOwnUser().getEmpire().get_id();
        subscriber.subscribe(gameEmpiresApiService.getRessourceAggregate(game.getId(), id), response -> {
            AggregateResult result = response.body();
            if (result == null) {
                return;
            }
            for (AggregateItem item : result.items()) {
                deltaInit.put(item.variable().split("\\.")[1], item.subtotal());
            }
            setTextForFraction();
            setTextForResources();
        }, error -> System.out.println("Error: " + error));
    }

    private boolean hasExplorerShipInFraction() {
        for (Fleet fleet : fraction.getFleets()) {
            for (Ship ship : fleet.getShips()) {
                if ("explorer".equals(ship.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasColonizationShipInFraction() {
        for (Fleet fleet : fraction.getFleets()) {
            for (Ship ship : fleet.getShips()) {
                if ("colonizer".equals(ship.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void upgradeExploreFraction() {
        String nextUpgrade = upgradeService.getNextUpgrade(fraction.getUpgrade());
        if (nextUpgrade == null || nextUpgrade.equals("Error")) {
            return;
        }

        if ("explored".equals(nextUpgrade) && !hasExplorerShipInFraction()) {
            upgradeErrorText.setText("No explorer ship in this fraction");
            upgradeErrorText.setVisible(true);
            return;
        }

        if ("colonized".equals(nextUpgrade) && !hasColonizationShipInFraction()) {
            upgradeErrorText.setText("No colonization ship in this fraction");
            upgradeErrorText.setVisible(true);
            return;
        }

        exploreUpgradeButton.setDisable(false);

        subscriber.subscribe(jobService.createUpgradingJob(fraction.get_id()), response -> {
            if (response.isSuccessful()) {
                upgradeErrorText.setVisible(false);
                fraction.setUpgrade(nextUpgrade);
                exploreUpgradeButton.setText(upgradeService.getNextUpgradeText(nextUpgrade));
                exploreUpgradeButton.setDisable(false);
            } else {
                System.out.println("Message: " + response.message());
                upgradeErrorText.setVisible(true);
            }
        }, error -> System.out.println("Error: " + error));
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
