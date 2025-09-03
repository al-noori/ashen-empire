package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.component.UserListComponent;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.GameOverviewService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TokenStorage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@Title("Game Overview")
public class GameOverviewController {
    @FXML
    public ListView<User> playerBox;
    @FXML
    public Button startGameButton;
    @FXML
    public Button leaveGameButton;
    @FXML
    public Button chooseEmpireButton;
    @FXML
    public Button achievementsButton;
    @FXML
    public Button removePlayerButton;
    @Inject
    App app;
    @Inject
    GameOverviewService gameOverviewService;
    @Inject
    Subscriber subscriber;
    @Inject
    Subscriber readySubscriber;
    @SubComponent
    @Inject
    Provider<UserListComponent> userComponentProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    GameMembersApiService gameMembersApiService;
    @Inject
    GamesApiService gamesApiService;
    @Inject
    GameService gameService;
    @FXML
    Button createEmpireButton;
    @Param("game")
    Game game;
    @Param("spectator")
    boolean spectator = false;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Inject
    UserApiService userApiService;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public GameOverviewController() {
    }


    @OnDestroy
    void destroy() {
        subscriber.dispose();
    }

    @OnRender
    void render() {
        if (game == null) {
            game = gameService.getGame();
            subscriber.subscribe(gameService.loadGame(game.getId()), game -> {
                initAll();
                addUsersListener();
            });
        } else {
            initAll();
            addUsersListener();
        }
        removePlayerButton.setDisable(true);
        removePlayerButton.setVisible(gameService.amIHost());

        if (gameService.getSpectator()) {
            startGameButton.setDisable(true);
            removePlayerButton.setDisable(true);
            createEmpireButton.setDisable(true);
            chooseEmpireButton.setDisable(true);
            achievementsButton.setDisable(true);
        }

    }

    private void addUsersListener() {
        users.addListener((ListChangeListener.Change<? extends User> change) -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (User removedUser : change.getRemoved()) {
                        if (removedUser.get_id().equals(tokenStorage.getUserId())) {
                            Platform.runLater(() -> app.show("/lobby"));
                        }
                    }
                }
            }
        });
    }

    private void initAll() {
        if (game.isStarted()) subscriber.subscribe(gameService.joinGame(), game -> app.show("/inGame", Map.of("game", game)));
        subscriber.listen(game.listeners(), Game.PROPERTY_STARTED, evt -> Platform.runLater(() -> {
            if ((Boolean) evt.getNewValue())
                subscriber.subscribe(gameService.joinGame(), game -> app.show("/inGame", Map.of("game", game)));
        }));
        if (!game.getOwner().equals(gameService.getOwnUser())) {
            startGameButton.setVisible(false);
        }
        subscriber.listen(game.listeners(), Game.PROPERTY_MEMBERS, evt -> Platform.runLater(() -> {
            users.remove((User) evt.getOldValue());
            User newUser = (User) evt.getNewValue();
            if (newUser != null) {
                users.add(newUser);
                listenToReadiness(List.of(newUser));
            }
        }));
        playerBox.setItems(users);
        users.addAll(game.getMembers());
        playerBox.setCellFactory(list -> new ComponentListCell<>(app, () -> userComponentProvider.get().setGame(game)));
        listenToReadiness(game.getMembers());
        boolean allReady = true;
        for (User us : game.getMembers()) {
            allReady = allReady && us.isReady();
        }
        boolean rd = allReady;
        Platform.runLater(() -> startGameButton.setDisable(!rd));
    }

    private void listenToReadiness(List<User> users) {
        users.forEach(user -> subscriber.listen(user.listeners(), User.PROPERTY_READY, evt -> {
            boolean allReady = true;
            for (User us : game.getMembers()) {
                allReady = allReady && us.isReady();
            }
            boolean rd = allReady;
            Platform.runLater(() -> startGameButton.setDisable(!rd));
        }));
    }

    public void startGameButton() {
        if ((game.isStarted() && users.stream().anyMatch(u -> u.get_id().equals(tokenStorage.getUserId()))) || gameService.amISpectator()) {
            subscriber.subscribe(gameService.joinGame(), game -> app.show("/inGame", Map.of("game", game)));
        } else {
            subscriber.subscribe(gameService.startGame(), game -> app.show("/inGame", Map.of("game", game)), e -> System.out.println("Error: " + e.getMessage()));
        }
    }

    public void leaveGameButton() {
        if (!tokenStorage.getUserId().equals(game.getOwner().get_id())) {
            subscriber.subscribe(gameService.leaveGame(),
                    result -> app.show("/lobby"),
                    error -> app.show("/lobby")
            );

        } else {
            app.show("/lobby");
        }
    }

    public void createEmpireButton() {
        app.show("/createEmpire", Map.of("game", game));
    }

    public void chooseEmpireButton() {
        subscriber.subscribe(gameMembersApiService.getPlayer(game.getId(), tokenStorage.getUserId()),
                result -> {
                    assert result.body() != null;
                    app.show("/chooseEmpire", Map.of("game", game, "ready", result.body().ready()));
                }
        );
    }

    public void achievementsButton() {
        app.show("/achievements", Map.of("backTo", "/gameOverview", "game", game));
    }

    @FXML
    void onPlayerBoxClicked() {
        User selectedPlayer = playerBox.getSelectionModel().selectedItemProperty().get();
        boolean disableRemoveButton = selectedPlayer == null || !tokenStorage.getUserId().equals(game.getOwner().get_id()) || selectedPlayer.get_id().equals(game.getOwner().get_id());
        removePlayerButton.setDisable(disableRemoveButton);
    }

    @FXML
    void removePlayerButton() {
        User removePlayerMember = playerBox.getSelectionModel().getSelectedItem();
        String removePlayerId = removePlayerMember.get_id();
        subscriber.subscribe(gameOverviewService.deleteMember(this.game.getId(), removePlayerId),
                result -> {
                    users.remove(removePlayerMember);
                    if (removePlayerId.equals(tokenStorage.getUserId())) {
                        Platform.runLater(() -> app.show("/lobby"));
                    }
                },
                Throwable::printStackTrace
        );
        removePlayerButton.setDisable(true);
    }

}
