package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.rest.AuthApiService;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.service.*;
import de.uniks.stp24.ws.EventListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


@Controller
@Title("Lobby")
public class LobbyController {
    @Inject
    GamesApiService gamesApiService;
    @Inject
    UserService userService;
    @Inject
    AuthApiService authApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    LobbyService lobbyService;
    @Inject
    PrefService prefService;
    @Inject
    TimerService timerService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Subscriber subscriber;
    @Inject
    App app;
    @Inject
    EventListener eventListener;
    @Inject
    GameService gameService;
    @FXML
    Button joinTheGame;
    @FXML
    Button createGame;
    @FXML
    Button gameManagement;
    @FXML
    Button deleteGame;
    @Inject
    GameMembersApiService gameMembersApiService;
    @FXML
    Button userAdministration;
    @FXML
    Button achievementsButton;
    @FXML
    TableView<GameDto> tableView;
    @FXML
    TableColumn<GameDto, String> gameNameColumn;
    @FXML
    TableColumn<GameDto, String> playersNumberColumn;
    @FXML
    TableColumn<GameDto, String> hostColumn;

    public final ObservableList<GameDto> games = FXCollections.observableArrayList();
    private final ObjectProperty<UserDto> currentUserProperty = new SimpleObjectProperty<>();
    private Map<String, String> usersMap = new HashMap<>();

    @Inject
    MusicService musicService;

    @FXML
    Button joinAsSpectator;

    @Inject
    public LobbyController() {
    }

    @OnInit
    void init() {
        subscriber.subscribe(gamesApiService.getAllGames(), response -> {
            if (response.isSuccessful()) {
                List<CreateGameResponseDto> createGameResponses = response.body();
                assert createGameResponses != null;
                subscriber.subscribe(userService.decodeUsers(createGameResponses.stream().map(CreateGameResponseDto::owner).collect(Collectors.toSet())), users -> {
                    usersMap = users;
                    for (CreateGameResponseDto createGameResponseDto : createGameResponses) {
                        games.add(new GameDto(null, null, createGameResponseDto._id(), createGameResponseDto.owner(), createGameResponseDto.name(),
                                createGameResponseDto.started(), createGameResponseDto.speed(), createGameResponseDto.period(),
                                createGameResponseDto.settings(),
                                createGameResponseDto.members(),
                                users.get(createGameResponseDto.owner())));
                    }
                    tableView.setItems(games);
                });
            } else {
                System.out.println(response.errorBody());
            }
        });

        subscriber.subscribe(eventListener.listen("games.*.*", CreateGameResponseDto.class), event -> {
            final CreateGameResponseDto gameDto = event.data();
            if (gameDto == null) {
                return;
            }
            final GameDto newGame;
            if (usersMap.containsKey(gameDto.owner())) {
                newGame = new GameDto(gameDto.createdAt(), gameDto.updatedAt(), gameDto._id(), gameDto.owner(), gameDto.name(),
                        gameDto.started(), gameDto.speed(), gameDto.period(),
                        gameDto.settings(),
                        gameDto.members(),
                        usersMap.get(gameDto.owner()));
            } else {
                String hostname = userService.getUserNameById(gameDto.owner()).blockingFirst();
                newGame = new GameDto(gameDto.createdAt(), gameDto.updatedAt(), gameDto._id(), gameDto.owner(), gameDto.name(),
                        gameDto.started(), gameDto.speed(), gameDto.period(),
                        gameDto.settings(),
                        gameDto.members(),
                        hostname);
                usersMap.put(gameDto.owner(), hostname);
            }
            switch (event.suffix()) {
                case "created" -> games.add(newGame);
                case "updated" -> games.replaceAll(game -> game._id().equals(newGame._id()) ? newGame : game);
                case "deleted" -> games.removeIf(game -> game._id().equals(newGame._id()));
            }
            tableView.setItems(games);
        });
    }


    @OnDestroy
    void destroy() {
        subscriber.dispose();
    }

    @OnRender
    void render() {

        // Disable the joinTheGame button until a game is selected
        //joinTheGame.disableProperty().bind(gameList.getSelectionModel().selectedItemProperty().isNull());
        joinTheGame.disableProperty().bind(
                Bindings.createBooleanBinding(() -> {
                    GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
                    return selectedGame == null;
                }, tableView.getSelectionModel().selectedItemProperty().isNull()));

        // Disable the gameManagement button until a game is selected and its owner is the current user
        gameManagement.disableProperty().bind(
                Bindings.createBooleanBinding(() -> {
                    GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
                    return selectedGame == null || !selectedGame.owner().equals(tokenStorage.getUserId());
                }, tableView.getSelectionModel().selectedItemProperty(), currentUserProperty));

        // Disable the gameManagement button until a game is selected and its owner is the current user
        deleteGame.disableProperty().bind(
                Bindings.createBooleanBinding(() -> {
                    GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
                    return selectedGame == null || !selectedGame.owner().equals(tokenStorage.getUserId());
                }, tableView.getSelectionModel().selectedItemProperty(), currentUserProperty));

        gameNameColumn.setCellValueFactory(gameStringCellDataFeatures -> {
            final GameDto game = gameStringCellDataFeatures.getValue();
            return new SimpleStringProperty(game.name());
        });
        playersNumberColumn.setCellValueFactory(cellDataFeatures -> {
            final GameDto game = cellDataFeatures.getValue();
            return new SimpleStringProperty(game.playersCount() + "");
        });
        hostColumn.setCellValueFactory(cellDataFeatures -> {
            final GameDto game = cellDataFeatures.getValue();
            return new SimpleStringProperty(game.hostname());
        });


        // Disable the joinAsSpectator button until a game is selected
        joinAsSpectator.disableProperty().bind(
                Bindings.createBooleanBinding(() -> {
                    GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
                    return selectedGame == null || selectedGame.owner().equals(tokenStorage.getUserId());
                }, tableView.getSelectionModel().selectedItemProperty()));
    }


    public void gameManagement() {
        app.show("/gameManager", Map.of("id", tableView.getSelectionModel().getSelectedItem()._id()));
    }

    public void createGame() {
        app.show("/createGame");
    }

    public void logOut() {
        subscriber.subscribe(lobbyService.logout(), response -> {
            timerService.stopTimer();
            app.show("/login");
        }, error -> System.out.println(error.getMessage()));
    }

    public void deleteGame() {
        app.show("/gameManager/deletionScreen", Map.of("id", tableView.getSelectionModel().getSelectedItem()._id()));
    }

    public void joinTheGame() {
        GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            return;
        }
        subscriber.subscribe(gameService.amIMember(selectedGame._id()), hasJoined -> {
            if (hasJoined) {
                gameService.getGame().setId(selectedGame._id());
                app.show("/gameOverview");
            } else {
                if (selectedGame.started()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The game has already started. You cannot join.");
                    alert.showAndWait();
                } else {
                    app.show("/joinTheGame", Map.of("game", selectedGame));
                }
            }
        });
    }

    public void openAchievements() {
        app.show("/achievements", Map.of("backTo", "/lobby"));
    }

    public void openUserAdministration() {
        app.show("/editUser");
    }

    public void joinAsSpectator() {
        GameDto selectedGame = tableView.getSelectionModel().getSelectedItem();
        subscriber.subscribe(gameService.setUserAsSpectator(selectedGame._id()),
                spectatorGame -> {
                    gameService.getGame().setId(selectedGame._id());
                    if (selectedGame.started() && gameService.amISpectator()) {
                        subscriber.subscribe(gameService.joinGame(),
                                game -> app.show("/inGame", Map.of("game", game)));
                    } else {
                        System.out.println("The game has not started yet. Please wait until the game starts.");
                        app.show("/gameOverview");
                    }
                });
    }


}
