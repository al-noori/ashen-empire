package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.component.*;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.*;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

@Controller
@Title("Ingame")
public class InGameController {
    @FXML
    Button contactsButton;
    @FXML
    HBox fleetListBox;
    @FXML
    Button marketButton;
    @FXML
    Button technologiesButton;
    @FXML
    HBox jobsListBox;
    @FXML
    StackPane stackPane;
    @SubComponent
    @FXML
    @Inject
    MapComponent mapComponent;
    @FXML
    AnchorPane anchorPane;
    @FXML
    BorderPane overlay;
    @FXML
    HBox fractionsListBox;
    @FXML
    HBox gameControlMenuBox;
    @Inject
    App app;
    @Param("game")
    Game game;
    @Inject
    Subscriber subscriber;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    GameControlMenuComponent gameControlMenuComponent;
    @SubComponent
    @Inject
    FractionListComponent fractionListComponent;
    @SubComponent
    @Inject
    JobsListComponent jobsListComponent;
    @SubComponent
    @Inject
    FleetListComponent fleetListComponent;
    @SubComponent
    @Inject
    GamePausedComponent gamePausedComponent;
    @SubComponent
    @Inject
    TechnologiesComponent technologiesComponent;
    @FXML
    HBox resourcesBox;
    @SubComponent
    @Inject
    ResourcesBarComponent resourcesBarComponent;
    @SubComponent
    @Inject
    ContactsComponent contactsComponent;
    @Inject
    UserApiService userApiService;
    @Inject
    GameService gameService;

    @SubComponent
    @Inject
    @FXML
    TechnologyTreeComponent technologyTreeComponent;

    @SubComponent
    @Inject
    Provider<TechnologieCardComponent> technologieCardComponentProvider;


    @SubComponent
    @Inject
    @FXML
    NotificationsComponent notificationsComponent;

    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    EventListener eventListener;
    @Inject
    StoryEventComponent storyEventComponent;

    @Inject
    GameControlMenuService gameControlMenuService;

    @SubComponent
    @Inject
    GameOver gameOver;

    @SubComponent
    @Inject
    WonTheGameComponent wonTheGameComponent;

    @Inject
    AchievementsService achievementsService;

    @Inject
    TechnologiesServices technologiesServices;


    private static final Random random = new Random();
    private int interval; // interval for story events
    private int counter = 0; // counter for interval

    @SubComponent
    @Inject
    MarketComponent marketComponent;


    @OnInit
    void init() {
        if (gameService.amISpectator()) {
            return;
        }
        interval = random.nextInt(10) + 6;
        subscriber.listen(game.listeners(), Game.PROPERTY_PERIOD, evt -> {
            if (stackPane.getChildren().size() == 1) {
                counter++;
                if (counter >= interval) {
                    Platform.runLater(this::showStoryEvent);
                    interval = random.nextInt(10) + 6;
                    counter = 0;
                }
            }
        });
        //addPlayerSubscriber();
    }

    private void showStoryEvent() {
        storyEventComponent.stackPane = stackPane;
        stackPane.getChildren().add(app.initAndRender(storyEventComponent,
                Map.of("eventNumber", 1 + game.getPeriod() % 16, "game", game))
        );
    }

    @OnRender
    void render() {
        //Add countryList to Ingame
        User ownUser = gameService.getOwnUser();
        if (ownUser != null && ownUser.getEmpire() != null) {
            fractionsListBox.getChildren().add(fractionListComponent);
            jobsListBox.getChildren().add(jobsListComponent);
            fleetListBox.getChildren().add(fleetListComponent);
            fleetListComponent.stackPane = stackPane;
            //Add resourcesBar to Ingame
            resourcesBox.getChildren().add(resourcesBarComponent);
            gameControlMenuBox.getChildren().add(app.initAndRender(gameControlMenuComponent, Map.of("game", game)));
        }

        anchorPane.setOnScroll(mapComponent.getOnScroll());
        technologiesComponent.borderPane.minHeightProperty().bind(stackPane.heightProperty());
        technologiesComponent.borderPane.minWidthProperty().bind(stackPane.widthProperty());

        if (gameService.amISpectator()) {
            marketButton.setDisable(true);
            technologiesButton.setDisable(true);
            contactsButton.setDisable(true);

        } else  {
            addFractionsSubscribers();
            addPlayersSubscribers();
            checkWonTheGame();
            checkGameOver();
            addJobsSubscribers();
            addResourcesBarSubscribers();
            checkForAchievements();
        }
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
                0.0, 0.0,
                100.0, 0.0,
                140.0, 10.0,
                100.0, 20.0,
                0.0, 20.0
        );
        marketButton.setShape(polygon);
        technologiesButton.setShape(polygon);
        contactsButton.setShape(polygon);
    }
    private void addFractionsSubscribers() {
        subscriber.listen(gameService.getOwnUser().listeners(), User.PROPERTY_EMPIRE, evt -> Platform.runLater(this::checkGameOver));
    }
    private void checkGameOver() {
        if(gameService.getOwnUser().getEmpire() == null) {
            stackPane.getChildren().removeAll();
            gameOver.stackPane = stackPane;
            stackPane.getChildren().add(gameOver);
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
            wonTheGameComponent.stackPane = stackPane;
            stackPane.getChildren().removeAll();
            stackPane.getChildren().add(wonTheGameComponent);
            achievementsService.checkAndUnlockAchievements(gameService.amIHost());
        }
    }
    private void addResourcesBarSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_RESOURCES, evt -> checkForAchievements());
    }
    private void checkForAchievements() {
        Map<String, Integer> resources = gameService.getOwnUser().getEmpire().getResources();
        if (resources.get("energy") >= 10000) {
            achievementsService.unlockEnergyHoarder();
        }
        if (resources.get("credits") >= 20000) {
            achievementsService.unlockTreasureTrove();
        }
        if(technologiesServices.areAllTechnologiesUnlocked()) {
            achievementsService.unlockTechTitan();
        }
    }
    private void addJobsSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_JOBS, evt -> checkForAchievements());
    }


    @Inject
    public InGameController() {
    }

    public void pauseButton() {
        gamePausedComponent.stackPane = stackPane;
        stackPane.getChildren().add(gamePausedComponent);
    }

    public void onTechnologiesButtonClicked() {
        technologiesComponent.stackPane = stackPane;
        stackPane.getChildren().add(technologiesComponent);
        technologiesComponent.rerenderTree();
    }


    public void marketButton() {
        marketComponent.stackPane = stackPane;
        stackPane.getChildren().add(marketComponent);
    }

    void addPlayerSubscriber() {
        subscriber.listen(game.listeners(), Game.PROPERTY_MEMBERS, evt -> {
            if(gameService.amIPlayer()) {
                gameOver.stackPane = stackPane;
                stackPane.getChildren().add(gameOver);
            }
        });
    }


    public void onContactsButtonClicked(MouseEvent mouseEvent) {
        contactsComponent.stackPane = stackPane;
        stackPane.getChildren().add(app.initAndRender(contactsComponent, Map.of("game", game)));
    }
    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }
}
