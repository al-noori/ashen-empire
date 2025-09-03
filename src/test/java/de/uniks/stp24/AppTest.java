package de.uniks.stp24;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.stp24.dto.*;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.rest.*;
import de.uniks.stp24.service.*;
import de.uniks.stp24.util.response.ResponseEnum;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TextMatchers;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static de.uniks.stp24.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;


public class AppTest extends ApplicationTest {
    @Spy
    public App app = new App(false);


    protected TestComponent testComponent = (TestComponent) DaggerTestComponent.builder().mainApp(app).build();

    final Subject<Event<GameDto>> gameSubject = BehaviorSubject.create();
    final Subject<Event<GameMembersDto>> gameMembersSubject = BehaviorSubject.create();
    final Subject<Event<GameEmpireDto>> empireSubject = BehaviorSubject.create();
    final Subject<Event<FractionDto>> fractionSubject = BehaviorSubject.create();
    final Subject<Event<JobDto>> jobSubject = BehaviorSubject.create();


    @Override
    public void init() throws TimeoutException {
        FxToolkit.registerStage(Stage::new);
    }


    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setX(0);
        stage.setY(0);
        app.setComponent(testComponent);
        app.start(stage);
        stage.requestFocus();
        stage.getScene().getStylesheets().clear();
    }

    void setup() {
        final JobApiService jobApiService = testComponent.jobApiService();
        doReturn(Observable.just(Response.success(List.of()))).when(jobApiService).getJobList(any(), any());
        final AuthApiService authApiService = testComponent.authApiService();
        doReturn(Observable.just(new LoginResult("3", "a", "r")))
                .when(authApiService).refresh(new RefreshDto("r"));
        doReturn(Observable.just(new LoginResult("3", "a", "r")))
                .when(authApiService).login(new LoginDto("testuser", "test1234"));


        final LoginService loginService = testComponent.loginService();
        doReturn(Observable.just(new LoginResult("3", "a", "r")))
                .when(loginService).login("testuser", "test1234", false);

        final UserService userService = testComponent.userService();
        doReturn(Observable.just(new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "testuser", "1")))
                .when(userService).signup("testuser", "test1234");

        final AchievementsService achievementsService = testComponent.achievementsService();
        when(achievementsService.isAchievementUnlocked(anyString())).thenReturn(Observable.just(true));
        doReturn(Observable.just(new ArrayList<>())).when(achievementsService).getAchievementsFromUser();
        when(achievementsService.addAllAchievementsToUserIfNotHappenedYet()).thenReturn(Observable.just(false));

        final GamesApiService gamesApiService = testComponent.gamesApiService();
        AtomicInteger callCount = new AtomicInteger(0);

        List<CreateGameResponseDto> firstResponse = List.of(
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "1",
                        "Game1",
                        "1",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(50)
                ),
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "2",
                        "Game2",
                        "2",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                )
        );

        List<CreateGameResponseDto> secondResponse = List.of(
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "1",
                        "Game1",
                        "1",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(50)
                ),
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "2",
                        "Game2",
                        "2",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                ),
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "3",
                        "Game3",
                        "3",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                )
        );

        List<CreateGameResponseDto> thirdResponse = List.of(
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "1",
                        "Game1",
                        "1",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(50)
                ),
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "2",
                        "Game2",
                        "2",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                ),
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "3",
                        "Game3edit",
                        "3",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                )
        );

        doReturn(Observable.just(Map.of("1", "first", "2", "second", "3", "testuser"))).when(userService).decodeUsers(any());

        Mockito.when(gamesApiService.getAllGames()).thenAnswer((Answer<Observable<Response<List<CreateGameResponseDto>>>>) invocation -> {
            int currentCount = callCount.getAndIncrement(); // Increment once and use this value for conditions
            if (currentCount == 0) {
                return Observable.just(Response.success(firstResponse));
            } else if (currentCount == 1) {
                return Observable.just(Response.success(secondResponse));
            } else {
                return Observable.just(Response.success(thirdResponse));
            }
        });

        when(gamesApiService.getGameById(eq("3")))
                .thenAnswer(invocation -> {
                    Response<CreateGameResponseDto> response = Response.success(
                            new CreateGameResponseDto(
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    "3",
                                    "Game3",
                                    "3",
                                    1,
                                    1,
                                    false,
                                    1,
                                    1,
                                    new GameSettingsDto(100)
                            )
                    );
                    return Observable.just(response);
                });

        doReturn(Observable.just(new GameDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "3",
                "Game3edit",
                "3",
                true,
                1,
                1,
                new GameSettingsDto(100),
                1,
                "theone"
        )))
                .when(gamesApiService).updateCurrentGame(any(), anyBoolean(), any());

        final GameManagerService gameManagerService = testComponent.gameManagerService();
        doReturn(Observable.just(new CreateGameResponseDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "3",
                "Game3",
                "3",
                1,
                1,
                false,
                1,
                1,
                new GameSettingsDto(100)))).when(gameManagerService).createGame(any(), any(), any());
        doReturn(Observable.just("testuser")).when(userService).getUserNameById(any());
        final EventListener eventListener = testComponent.eventListener();

        Mockito.doReturn(BehaviorSubject.create()).when(eventListener).listen("games.3.empires.3.jobs.*.*", JobDto.class);

        Mockito.doReturn(gameSubject).when(eventListener).listen("games.*.*", CreateGameResponseDto.class);

        doReturn(Observable.just(new CreateGameResponseDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "3",
                "Game3",
                "3",
                1,
                1,
                false,
                1,
                1,
                new GameSettingsDto(100)
        ))).when(gameManagerService).getGame("3");

        doReturn(Observable.just(ResponseEnum.OK)).when(gameManagerService).editGame("3", "Game3edit", "password3edit", 100);


        doReturn(gameMembersSubject).when(eventListener).listen("games." + "3" + ".members.*.*", GameMembersDto.class);
        doReturn(gameSubject).when(eventListener).listen("games." + "3" + ".*", GameDto.class);


        TechnologyDto technologyDto = new TechnologyDto("12",null,null,0,null,null);


        final GameMembersApiService gameMembersApiService = testComponent.gameMembersApiService();
        Mockito.when(gameMembersApiService.getPlayer("3")).thenAnswer(invocation -> {
            Response<List<GameMembersDto>> response = Response.success(
                    List.of(
                            new GameMembersDto(
                                    "3",
                                    "3",
                                    false,
                                    LocalDateTime.of(1, 1, 1, 1, 1),
                                    LocalDateTime.of(1, 1, 1, 1, 1),
                                    new EmpireDto("ashen empire", "Test", "blue", "FFFFFF", 16, 16, "2", null, Arrays.asList(technologyDto) , LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1)))
                    )
            );
            return Observable.just(response);
        });

        final UserApiService userApiService = testComponent.userApiService();
        doReturn(Observable.just(new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "testuser", null))).when(userApiService).findOne(any());
        doReturn(Observable.just(List.of(new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "1", "first", null),
                new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "2", "second", null),
                new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "testuser", null)))
        ).when(userApiService).findAll();


        EmpireDto edto = new EmpireDto("TestEmpire", "test", "Blue", "FFFFFF", 0, 0, "null", null,Arrays.asList(technologyDto), LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1));
        doReturn(Observable.just(Response.success(new GameMembersDto("3", "3", true, LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), edto))))
                .when(gameMembersApiService).updateGameMembership(any(), any(), any());

        final GameEmpiresApiService gameEmpiresApiService = testComponent.gameEmpiresApiService();
        doReturn(Observable.just(List.of(
                new GameEmpireDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "3",
                        "3", "TestEmpire", "description", "blue", 16, 16, null, null,
                        Map.of(), new ArrayList<>())
        ))).when(gameEmpiresApiService).getAll(any());
        doReturn(Observable.just(Response.success(new GameEmpireDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "3",
                "3", "TestEmpire", "description", "blue", 16, 16, null, null,
                Map.of(), new ArrayList<>())))
        ).when(gameEmpiresApiService).getOne(any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getRessourceAggregate(any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getMilitaryAggregate(any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getEconomyAggregate(any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getTechnologyAggregate(any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getMilitaryCompareAggregate(any(), any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getEconomyCompareAggregate(any(), any(), any());
        doReturn(Observable.just(Response.success(new AggregateResult(0, new AggregateItem[0])))).when(gameEmpiresApiService).getTechnologyCompareAggregate(any(), any(), any());
        doReturn(empireSubject).when(eventListener).listen("games." + "3" + ".empires.*.*", GameEmpireDto.class);

        Mockito.when(gameMembersApiService.getPlayer("3", "3")).thenAnswer(invocation -> {
            Response<GameMembersDto> response = Response.success(
                    new GameMembersDto(
                            "3",
                            "3",
                            false,
                            LocalDateTime.of(1, 1, 1, 1, 1),
                            LocalDateTime.of(1, 1, 1, 1, 1),
                            edto)
            );
            return Observable.just(response);
        });

        final EditUserService editUserService = testComponent.editUserService();
        doReturn(Observable.just(new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "testuser", null))).when(editUserService).getUser();
        doReturn(Observable.just(new LoginResult("3", "a", "r"))).when(editUserService).checkPassword("test1234");
        doReturn(Observable.just(new UserDto(LocalDateTime.of(1, 1, 1, 1, 1), LocalDateTime.of(1, 1, 1, 1, 1), "3", "testuser", null))).when(editUserService).delete();

        when(gameMembersApiService.joinGame("3", new JoinGameDto(false, edto, "password3edit")))
                .thenAnswer(invocation -> {
                    Response<GameMembersDto> response = Response.success(
                            new GameMembersDto(
                                    "3",
                                    "3",
                                    false,
                                    LocalDateTime.of(1, 1, 1, 1, 1),
                                    LocalDateTime.of(1, 1, 1, 1, 1),
                                    edto
                            )
                    );
                    return Observable.just(response);
                });


        final GameSystemsApiService gameSystemsApiService = testComponent.gameSystemsApiService();
        doReturn(Observable.just(new FractionDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "3",
                "Game3",
                "Deutsche Land",
                Map.of(),
                Map.of(),
                1,
                null,
                "unexplored",
                1,
                Map.of(),
                1,
                1,
                "",
                "n",
                "",
                100
        ))).when(gameSystemsApiService).getFractionsFromUser(any(), any());

        doReturn(Observable.just(List.of())).when(gameSystemsApiService).getFractionMaxHealth(any(), any(), any());

        doReturn(Observable.just(List.of())).when(gameSystemsApiService).getFractionDefense(any(), any(), any());

        doReturn(Observable.just(new FractionDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "3",
                "Game3",
                "Deutsche Land",
                Map.of(),
                Map.of(),
                1,
                null,
                "unexplored",
                1,
                Map.of(),
                1,
                1,
                "",
                "n",
                "",
                100
        ))).when(gameSystemsApiService).getAllFractions(any());


        doReturn(fractionSubject).when(eventListener).listen("games." + "3" + ".systems.*.*", FractionDto.class);
        doReturn(empireSubject).when(eventListener).listen("games." + "3" + ".empires.3.updated", GameEmpireDto.class);
        final EmpireApiService empireApiService = testComponent.empireApiService();

        doReturn(Observable.just(new ArrayList<>())).when(empireApiService).getAll("3");

        doReturn(AsyncSubject.create()).when(eventListener).listen(any(), any());
        final PresetsApiService presetsApiService = testComponent.presetsApiService();
        doReturn(Observable.just(Response.success(new ArrayList<>()))).when(presetsApiService).getTraits();
        doReturn(Observable.just(new ArrayList<>())).when(presetsApiService).getShips();


      
        final TechnologiesServices technologiesServices = testComponent.technologiesServices();
        when(technologiesServices.areAllTechnologiesUnlocked()).thenReturn(false);
        doReturn(Observable.just(new ArrayList<>())).when(technologiesServices).loadTree();
        final VariablesApiService variablesApiService = testComponent.variablesApiService();
        doReturn(Observable.just(List.of(
                new VariableDto(
                        "empire.market.fee",
                        0.15,
                        null,
                        0.15
                )
        ))).when(variablesApiService).getVariables(any(), any(), any());
        final FleetsApiService fleetsApiService = testComponent.fleetApiService();
        doReturn(Observable.just(List.of())).when(fleetsApiService).getFleets(any());
        final ShipApiService shipApiService = testComponent.shipApiService();
        doReturn(Observable.just(List.of())).when(shipApiService).getShips(any(), any());
        final WarsApiService warsApiService = testComponent.warsApiService();
        doReturn(Observable.just(List.of())).when(warsApiService).getWars(any());

    }

    @Test
    public void v1() throws JsonProcessingException {
        // TODO login, main-menu, ...
        setup();

        waitForFxEvents();

        // test login screen title
        assertEquals("Log in", app.stage().getTitle());

        // test signup
        clickOn("#signupButton");
        waitForFxEvents();
        assertEquals("Sign up", app.stage().getTitle());
        clickOn("#nameInput");
        write("testuser\ttest1234\ttest1234");
        clickOn("#signUpButton");
        waitForFxEvents();

        // test lobby
        assertEquals("Lobby", app.stage().getTitle());
        TableView<GameDto> tableView = lookup("#tableView").query();

        // test create game
        clickOn("#createGame");
        waitForFxEvents();
        assertEquals("Create a new game", app.stage().getTitle());
        clickOn("#nameField");
        write("Game3\t");
        write("password3\t100");
        setupOverview();
        clickOn("#saveButton");

        waitForFxEvents();

        clickOn("#leaveGameButton");


        assertEquals("Lobby", app.stage().getTitle());
        clickOn("Game3");
        clickOn("#gameManagement");
        waitForFxEvents();

        // test edit game
        assertEquals("Edit the Game", app.stage().getTitle());
        clickOn("#nameInput");
        TextField nameInput = lookup("#nameInput").query();
        eraseText(nameInput.getText().length());
        write("Game3edit\t");
        write("password3edit\t100");
        clickOn("#saveChangesButton");
        waitForFxEvents();
        clickOn("#cancelButton");

        // test join game (Game Overview)
        clickOn("Game3edit");
        setupOverview();
        clickOn("#joinTheGame");
        waitForFxEvents();

        assertEquals("Game Overview", app.stage().getTitle());


        // test create empire
        clickOn("#createEmpireButton");
        waitForFxEvents();
        assertEquals("Create an empire", app.stage().getTitle());
        clickOn("#nameInput");
        write("TestEmpire\t");
        clickOn("#selectColour");
        waitForFxEvents();
        clickOn("Blue");
        clickOn("#descriptionInput");
        write("test");
        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectFlag").query()).getSelectionModel().select(0));

        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectPortrait").query()).getSelectionModel().select(0));
        waitForFxEvents();
        clickOn("#saveButton");

        waitForFxEvents();
        // test choose empire
        clickOn("#chooseEmpireButton");
        waitForFxEvents();
        assertEquals("Choose Empire", app.stage().getTitle());
        clickOn("TestEmpire");
        clickOn("#saveButton");
        interact(() -> {
            ComboBox<String> lookup = lookup("#comboBox").query();
            lookup.getSelectionModel().select(0);
        });
        waitForFxEvents();
        // test ingame
        initGame();
        clickOn("#startGameButton");
        waitForFxEvents();
        assertEquals("Ingame", app.stage().getTitle());
        clickOn("#pauseButton");
        waitForFxEvents();
        assertEquals("Ingame", app.stage().getTitle());
        clickOn("#backButton");
        waitForFxEvents();

        // test accountdeletionconfirmation
        clickOn("#userAdministration");
        clickOn("#deleteAccountText");
        waitForFxEvents();
        clickOn("#confPasswordInput");
        write("test1234");

        // test pop up window
        clickOn("#deleteButton");
        waitForFxEvents();
        clickOn("OK");
        waitForFxEvents();

        // after deletion back to login
        assertEquals("Log in", app.stage().getTitle());

        // go to licenses
        clickOn("#licencesButton");
        waitForFxEvents();
        assertEquals("Licenses", app.stage().getTitle());
        clickOn("#backButton");
        waitForFxEvents();
        assertEquals("Log in", app.stage().getTitle());

        //test login
        assertEquals("Log in", app.stage().getTitle());
        clickOn("#nameInput");
        write("testuser\ttest1234");
        clickOn("#loginButton");
        waitForFxEvents();
    }

    @AfterAll
    public static void clearInlineMocks() {
        Mockito.framework().clearInlineMocks();
    }

    @AfterEach
    public void garbargeCollection() {
        java.lang.System.gc();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Stage stage = app.stage();
        stage.getScene().getStylesheets().clear();
        stage.close();
        app = null;
        Mockito.framework().clearInlineMocks();
    }

    @Test
    public void v2() throws JsonProcessingException {

        setupv2();
        waitForFxEvents();

        // test login screen title
        assertEquals("Log in", app.stage().getTitle());

        // test signup
        clickOn("#signupButton");
        waitForFxEvents();
        assertEquals("Sign up", app.stage().getTitle());
        clickOn("#nameInput");
        write("testuser\ttest1234\ttest1234");
        clickOn("#signUpButton");
        waitForFxEvents();

        // test lobby
        assertEquals("Lobby", app.stage().getTitle());
        TableView<GameDto> tableView = lookup("#tableView").query();

        // test create game
        clickOn("#createGame");
        waitForFxEvents();
        assertEquals("Create a new game", app.stage().getTitle());
        clickOn("#nameField");
        write("Game3\t");
        write("password3\t100");
        setupOverview();
        clickOn("#saveButton");

        waitForFxEvents();

        clickOn("#createEmpireButton");
        waitForFxEvents();
        assertEquals("Create an empire", app.stage().getTitle());
        clickOn("#nameInput");
        write("TestEmpire\t");
        clickOn("#selectColour");
        waitForFxEvents();
        clickOn("Blue");
        clickOn("#descriptionInput");
        write("test");
        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectFlag").query()).getSelectionModel().select(0));

        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectPortrait").query()).getSelectionModel().select(0));
        waitForFxEvents();
        initGame();
        clickOn("#saveButton");

        waitForFxEvents();

        // test choose empire
        clickOn("#chooseEmpireButton");
        waitForFxEvents();
        assertEquals("Choose Empire", app.stage().getTitle());
        clickOn("TestEmpire");

        clickOn("#saveButton");
        interact(() -> {
            ComboBox<String> lookup = lookup("#comboBox").query();
            lookup.getSelectionModel().select(0);
        });
        // test ingame
        clickOn("#startGameButton");
        waitForFxEvents();
        verifyThat("#credits", TextMatchers.hasText("10000 (0)"));
        verifyThat("#minerals", TextMatchers.hasText("10000 (0)"));
        verifyThat("#fuel", TextMatchers.hasText("10000 (0)"));
        verifyThat("#alloys", TextMatchers.hasText("10000 (0)"));
        verifyThat("#consumer_goods", TextMatchers.hasText("10000 (0)"));
        verifyThat("#research", TextMatchers.hasText("10000 (0)"));
        verifyThat("#food", TextMatchers.hasText("10000 (0)"));
        verifyThat("#energy", TextMatchers.hasText("10000 (0)"));
        verifyThat("#population", TextMatchers.hasText("2500 (0)"));
        assertEquals("Ingame", app.stage().getTitle());
        clickOn("homesystem");
        waitForFxEvents();
        clickOn("-");
        waitForFxEvents();
        clickOn("+");
        waitForFxEvents();
        clickOn("X");
        waitForFxEvents();
        clickOn("Back");
        waitForFxEvents();
    }

    @Test
    public void v3() throws JsonProcessingException {
        setupv3();
        waitForFxEvents();

        // test login screen title
        assertEquals("Log in", app.stage().getTitle());

        // test signup
        clickOn("#signupButton");
        waitForFxEvents();
        assertEquals("Sign up", app.stage().getTitle());
        clickOn("#nameInput");
        write("testuser\ttest1234\ttest1234");
        clickOn("#signUpButton");
        waitForFxEvents();

        // test lobby
        assertEquals("Lobby", app.stage().getTitle());
        TableView<GameDto> tableView = lookup("#tableView").query();

        // test create game
        clickOn("#createGame");
        waitForFxEvents();
        assertEquals("Create a new game", app.stage().getTitle());
        clickOn("#nameField");
        write("Game3\t");
        write("password3\t100");
        setupOverview();
        clickOn("#saveButton");

        waitForFxEvents();

        clickOn("#createEmpireButton");
        waitForFxEvents();
        assertEquals("Create an empire", app.stage().getTitle());
        clickOn("#nameInput");
        write("TestEmpire\t");
        clickOn("#selectColour");
        waitForFxEvents();
        clickOn("Blue");
        clickOn("#descriptionInput");
        write("test");
        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectFlag").query()).getSelectionModel().select(0));

        waitForFxEvents();
        interact(() -> ((ComboBox<ImageView>) lookup("#selectPortrait").query()).getSelectionModel().select(0));
        waitForFxEvents();
        clickOn("#traitsButton");
        clickOn("#traitNameLabel");
        clickOn("#selectButton");
        waitForFxEvents();
        clickOn("#saveTraitsButton");
        waitForFxEvents();
        initGame();
        clickOn("#saveButton");

        waitForFxEvents();

        // test choose empire
        clickOn("#chooseEmpireButton");
        waitForFxEvents();
        assertEquals("Choose Empire", app.stage().getTitle());
        clickOn("TestEmpire");

        clickOn("#saveButton");
        interact(() -> {
            ComboBox<String> lookup = lookup("#comboBox").query();
            lookup.getSelectionModel().select(0);
        });
        // test ingame
        clickOn("#startGameButton");
        waitForFxEvents();

        clickOn("#zeroSpeedButton");
        clickOn("#doubleSpeedButton");
        clickOn("#tripleSpeedButton");
        clickOn("#regularSpeedButton");

        waitForFxEvents();

        // Technologies
        clickOn("#technologiesButton");
        sleep(5000);
        clickOn("#backButton");

        // Market
        clickOn("#marketButton");
        // Toggle Button
        clickOn("#toggleButton");
        clickOn("#toggleButton");
        clickOn("#toggleButton");
        // Radio Buttons
        waitForFxEvents();
        clickOn("#energyRadio");
        clickOn("#mineralsRadio");
        clickOn("#foodRadio");
        clickOn("#fuelRadio");
        clickOn("#alloysRadio");
        clickOn("#consumer_goodsRadio");
        // Change Button
        clickOn("#changeButton");
        // Exit Market
        clickOn("#backButton");

        // Pause Menu
        clickOn("#pauseButton");
        clickOn("#achievementsButton");
        clickOn("#backButton");
        clickOn("#pauseButton");
        clickOn("#musicButton");
        clickOn("#musicButton");
        clickOn("#resumeButton");
        waitForFxEvents();

        clickOn("homesystem");
        waitForFxEvents();

        clickOn("#exploreUpgradeButton");
        waitForFxEvents();

        clickOn("Cancel");
        clickOn("Back");


    }

    private void setupv3() throws JsonProcessingException {
        setupv2();


        final PresetsApiService presetsApiService = testComponent.presetsApiService();
        when(presetsApiService.getResources()).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super Map<String, ResourcePresetDto>> observer) {

            }
        });

        TraitsDto trait1 = new TraitsDto("prepared", new EffectDto[0], 0, new String[] {"unprepared"});
        TraitsDto trait2 = new TraitsDto("unprepared", new EffectDto[0], 0, new String[] {"prepared"});
        TraitsDto trait3 = new TraitsDto("strong", new EffectDto[0], 0, new String[0]);

        List<TraitsDto> traits = new ArrayList<>(List.of(trait1, trait2, trait3));

        when(presetsApiService.getTraits()).thenReturn(Observable.just(Response.success(traits)));

        TechnologyDto technology1 = new TechnologyDto("society", List.of(), List.of(), 1, List.of(), List.of());
        TechnologyDto technology2 = new TechnologyDto("demographic", List.of(), List.of(), 2, List.of(), List.of());

        List<TechnologyDto> technologies = new ArrayList<>(List.of(technology1, technology2));

        when(presetsApiService.getTechnologies()).thenReturn(Observable.just(Response.success(technologies)));

        final EmpireApiService empireApiService = testComponent.empireApiService();
        when(empireApiService.update(any(), any(), any())).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super GameEmpireDto> observer) {

            }
        });

        final AchievementsApiService achievementsApiService = testComponent.achievementsApiService();
        when(achievementsApiService.getAchievementsByUserId(any())).thenReturn(new Observable<>() {
            @Override
            protected void subscribeActual(@NonNull Observer<? super Response<List<AchievementDto>>> observer) {

            }
        });

        final JobDto job = new JobDto(
                null,
                null,
                "111",
                0,
                5,
                "3",
                "3",
                "101",
                0,
                "upgrade",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        final JobApiService jobApiService = testComponent.jobApiService();
        when(jobApiService.createJob(any(), any(), any())).thenAnswer(evt -> {
            jobSubject.onNext(new Event<>("games.3.empires.3.jobs.111.created", job));
            return Observable.just(Response.success(job));
        });

        final EventListener eventListener = testComponent.eventListener();
        doReturn(jobSubject).when(eventListener).listen("games.3.empires.3.jobs.*.*", JobDto.class);

    }

    private void setupOverview() {
        final GamesApiService gamesApiService = testComponent.gamesApiService();
        when(gamesApiService.getGameById(eq("3"))).thenReturn(Observable.just(Response.success(
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "3",
                        "Game3",
                        "3",
                        1,
                        1,
                        false,
                        1,
                        1,
                        new GameSettingsDto(100)
                )
        )));
        when(gamesApiService.updateGame(eq("3"), anyBoolean(), any())).thenAnswer(inv -> Observable.just(Response.success(
                inv.getArgument(1, CreateGameResponseDto.class)
        )));
        final GameMembersApiService gameMembersApiService = testComponent.gameMembersApiService();
        when(gameMembersApiService.getPlayer(eq("3"))).thenReturn(
                Observable.just(Response.success(
                        List.of(
                                new GameMembersDto(
                                        "3",
                                        "3",
                                        false,
                                        LocalDateTime.of(1, 1, 1, 1, 1),
                                        LocalDateTime.of(1, 1, 1, 1, 1),
                                        null)
                        )
                ))
        );
        final EmpireApiService empireApiService = testComponent.empireApiService();
        when(empireApiService.getAll(eq("3"))).thenReturn(
                Observable.just(Response.success(
                        List.of()
                ))
        );
        when(gameMembersApiService.updateGameMembership(eq("3"), eq("3"), any())).thenAnswer(inv -> {
            ChangeMemberDto changeMemberDto = inv.getArgument(2, ChangeMemberDto.class);
            boolean ready = false;
            if (changeMemberDto.ready() != null) ready = changeMemberDto.ready();
            GameMembersDto dto = new GameMembersDto(
                    "3",
                    "3",
                    ready,
                    null,
                    null,
                    changeMemberDto.empire());
            gameMembersSubject.onNext(new Event<>("game.3.members.3.updated", dto));
            return Observable.just(Response.success(
                    dto
            ));
        });
        final EventListener eventListener = testComponent.eventListener();
        when(eventListener.listen(eq("games.3.members.*.*"), eq(GameMembersDto.class))).thenReturn(gameMembersSubject);
        when(eventListener.listen(eq("games.3.*"), eq(GameDto.class))).thenReturn(gameSubject);
        when(eventListener.listen(eq("games.3.empires.*.*"), eq(GameEmpireDto.class))).thenReturn(empireSubject);
        when(eventListener.listen(eq("games.3.systems.*.*"), eq(FractionDto.class))).thenReturn(fractionSubject);
    }

    private void setupv2() {
        setup();
        final JobApiService jobApiService = testComponent.jobApiService();
        doReturn(Observable.just(Response.success(List.of()))).when(jobApiService).getJobList(any(), any());
        when(jobApiService.deleteJob(any(), any(), any())).thenReturn(Observable.just(Response.success(null)));
        when(jobApiService.createJob(any(), any(), any())).thenReturn(Observable.just(Response.success(null)));
        when(jobApiService.updateJob(any(), any(), any(), any())).thenReturn(Observable.just(Response.success(null)));
    }

    private void initGame() throws JsonProcessingException {
        final GamesApiService gamesApiService = testComponent.gamesApiService();
        when(gamesApiService.getGameById(eq("3")))
                .thenAnswer(invocation -> {
                    Response<CreateGameResponseDto> response = Response.success(
                            new CreateGameResponseDto(
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    "3",
                                    "Game3",
                                    "3",
                                    1,
                                    5,
                                    false,
                                    1,
                                    1,
                                    new GameSettingsDto(100)
                            )
                    );
                    return Observable.just(response);
                });

        Mockito.doReturn(Observable.just(Response.success(
                new CreateGameResponseDto(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "testGameID",
                        "testGame",
                        "testUserID",
                        1,
                        1,
                        true,
                        1,
                        1,
                        null
                )
        ))).when(gamesApiService).getGameById("testGameID");
        when(gamesApiService.updateGame(eq("3"), anyBoolean(), any())).thenAnswer(invocation -> {
            CreateGameDto gdto = invocation.getArgument(2, CreateGameDto.class);
            int speed = 0;
            if (gdto.speed() != null) speed = gdto.speed();
            CreateGameResponseDto createGameResponseDto = new CreateGameResponseDto(
                    null,
                    null,
                    invocation.getArgument(0, String.class),
                    gdto.name(),
                    null,
                    1,
                    5,
                    gdto.started(),
                    speed,
                    1,
                    gdto.settings()
            );
            Response<CreateGameResponseDto> response = Response.success(
                    createGameResponseDto
            );
            return Observable.just(response);
        });
        final EmpireApiService empireApiService = testComponent.empireApiService();
        GameEmpireDto gameEmpireDto = new GameEmpireDto(
                null,
                null,
                "3",
                "3",
                "3",
                "3",
                "3",
                "#FFFFFF",
                5,
                5,
                "101",
                new String[0],
                Map.of("credits", 10000,
                        "population", 2500,
                        "energy", 10000,
                        "minerals", 10000,
                        "food", 10000,
                        "fuel", 10000,
                        "research", 10000,
                        "alloys", 10000,
                        "consumer_goods", 10000),
                null
        );

        when(empireApiService.getAll(eq("3"))).thenReturn(Observable.just(Response.success(List.of(
                gameEmpireDto
        ))));
        when(empireApiService.getById(eq("3"), eq("3"))).thenReturn(Observable.just(
                gameEmpireDto
        ));
        when(empireApiService.getAll(eq("3"))).thenReturn(Observable.just(Response.success(List.of(
                gameEmpireDto
        ))));
        when(empireApiService.getById(eq("3"), eq("3"))).thenReturn(Observable.just(
                gameEmpireDto));
        final GameSystemsApiService gameSystemsApiService = testComponent.gameSystemsApiService();
        List<FractionDto> fractionDtos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            fractionDtos.add(new FractionDto(
                    null,
                    null,
                    "" + i,
                    "3",
                    "inhabitable_0",
                    Map.of("city", 1, "energy", 1, "mining", 1, "agriculture", 1, "industry", 1, "research_site", 1, "ancient_foundry", 1, "ancient_factory", 1, "ancient_refinery", 1),
                    Map.of("city", 1, "energy", 1, "mining", 1, "agriculture", 1, "industry", 1, "research_site", 1, "ancient_foundry", 1, "ancient_factory", 1, "ancient_refinery", 1),
                    0,
                    new String[0],
                    "unexplored",
                    0,
                    Map.of((i + 1) + "", 5),
                    i, i,
                    null,
                    null,
                    "uninhabitable_0",
                    100
            ));
        }

        when(gameSystemsApiService.getAllFractions(eq("3"))).thenReturn(
                Observable.just(Response.success(fractionDtos))
        );
        fractionDtos.add(new FractionDto(
                null,
                null,
                "" + 101,
                "3",
                "homesystem",
                Map.of("city", 5, "energy", 5, "mining", 5, "agriculture", 5, "industry", 5, "research_site", 5, "ancient_foundry", 5, "ancient_factory", 5, "ancient_refinery", 5),
                Map.of("city", 1, "energy", 1, "mining", 1, "agriculture", 1, "industry", 1, "research_site", 1, "ancient_foundry", 1, "ancient_factory", 1, "ancient_refinery", 1),
                0,
                new String[]{
                        "exchange",
                        "power_plant"
                },
                "upgraded",
                0,
                Map.of(),
                -50, -50,
                "3",
                "3",
                "regular",
                100
        ));

        when(gameSystemsApiService.getAllFractions(eq("3"))).thenReturn(
                Observable.just(Response.success(fractionDtos))
        );
        PresetsApiService presetsApiService = testComponent.presetsApiService();
        final ObjectMapper objectMapper = new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        List<DistrictDto> districtDtos = objectMapper.readValue(DISTRICTS_PRESETS, new TypeReference<>() {
        });
        when(presetsApiService.getDistricts()).thenReturn(Observable.just(Response.success(
                districtDtos
        )));
        List<BuildingDto> buildingDtos = objectMapper.readValue(Buildings_Preset, new TypeReference<>() {
        });
        Map<String, BuildingDto> buildingsMap = new HashMap<>();
        buildingDtos.forEach(dto -> buildingsMap.put(dto.id(), dto));
        when(presetsApiService.getBuildingInfo(anyString())).thenAnswer(inv -> buildingsMap.get(inv.getArgument(0, String.class)));
        when(presetsApiService.getBuildings()).thenReturn(Observable.just(buildingDtos));
        SystemUpgradeResponseDto systemUpgradeResponseDto = objectMapper.readValue(SYSTEM_UPGRADES, new TypeReference<>() {
        });
        when(gameSystemsApiService.getFractionUpgrades()).thenReturn(Observable.just(systemUpgradeResponseDto));
        final GameService gameService = testComponent.gameService();
        when(gameSystemsApiService.updateFraction(eq("3"), eq("101"), any())).thenAnswer(
                inv -> {
                    Fraction fraction = gameService.getFraction("101");
                    UpdateFractionDto dto = inv.getArgument(2, UpdateFractionDto.class);
                    Map<String, Integer> map = dto.districts();
                    if (map != null) {
                        Map<String, Integer> currentDistricts = new HashMap<>(fraction.getDistricts());
                        map.forEach((entry, value) -> {
                            currentDistricts.merge(entry, value, Integer::sum);

                            fraction.setDistricts(currentDistricts);

                            fractionSubject.onNext(new Event<>("game.3.systems.101.updated", new FractionDto(
                                    null,
                                    null,
                                    "101",
                                    "3",
                                    "homesystem",
                                    fraction.getDistrictSlots(),
                                    currentDistricts,
                                    5,
                                    dto.buildings(),
                                    "developed",
                                    25,
                                    Map.of("100", 5),
                                    -50,
                                    -50,
                                    "3",
                                    "3",
                                    "regular",
                                    100
                            )));
                        });
                    }
                    return Observable.just(new FractionDto(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            null,
                            "developed",
                            0,
                            null,
                            0,
                            0,
                            null,
                            null,
                            null,
                            100
                    ));
                }
        );
        final GameEmpiresApiService gameEmpiresApiService = testComponent.gameEmpiresApiService();
        when(gameEmpiresApiService.getRessourceAggregate(eq("3"), eq("3"))).thenReturn(
                Observable.just(Response.success(
                        new AggregateResult(
                                5,
                                new AggregateItem[]{
                                        new AggregateItem("energy", 5, 5),
                                        new AggregateItem("credits", 5, 5),
                                        new AggregateItem("population", 5, 5),
                                        new AggregateItem("minerals", 5, 5),
                                        new AggregateItem("food", 5, 5),
                                        new AggregateItem("fuel", 5, 5),
                                        new AggregateItem("research", 5, 5),
                                        new AggregateItem("alloys", 5, 5),
                                        new AggregateItem("consumer_goods", 5, 5)
                                }
                        )
                ))
        );
    }
}
