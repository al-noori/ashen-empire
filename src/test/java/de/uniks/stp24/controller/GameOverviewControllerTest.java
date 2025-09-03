package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.component.UserListComponent;
import de.uniks.stp24.dto.EmpireDto;
import de.uniks.stp24.dto.GameMembersDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.util.WaitForAsyncUtils;
import retrofit2.Response;

import javax.inject.Provider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class GameOverviewControllerTest extends ControllerTest {
    EventListener eventListener = mock(EventListener.class);
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    GameMembersApiService gameMembersApiService = mock(GameMembersApiService.class);
    @Spy
    UserApiService userApiService = spy(UserApiService.class);
    @InjectMocks
    GameOverviewController gameOverviewController;
    @Spy
    TokenStorage tokenStorage = spy(TokenStorage.class);
    @Spy
    GameService gameService = spy(GameService.class);
    Game game = new Game().setOwner(new User());
    Provider<UserListComponent> userListComponentProvider = mock(Provider.class);
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));



    final Subject<Event<GameMembersDto>> subject= BehaviorSubject.create();
    final Subject<Event<Game>> subject2= BehaviorSubject.create();

    @Override
    public void start(Stage stage) throws Exception{
        super.start(stage);
        doReturn(new UserListComponent().inject(userApiService, gameMembersApiService, tokenStorage, subscriber)).when(userListComponentProvider).get();
        doReturn(subject).when(eventListener).listen("games." + "507f191e810c19729de860ea" + ".members.*.*", GameMembersDto.class);
        doReturn(subject2).when(eventListener).listen("games." + "507f191e810c19729de860ea" + ".*", Game.class);
        doReturn("62fc9b33773277d12d28929b").when(tokenStorage).getUserId();
        Mockito.when(gameMembersApiService.getPlayer("507f191e810c19729de860ea")).thenAnswer(invocation -> {
            Response<List<GameMembersDto>> response = Response.success(
                    List.of(
                            new GameMembersDto (
                                    "507f191e810c19729de860ea",
                                    "62fc9b33773277d12d28929b",
                                    false,
                                    LocalDateTime.now(),
                                    LocalDateTime.now(),
                                    new EmpireDto("ashen empire", "Test", "blue", "FFFFFF" ,16, 16, "Tests", null, null, LocalDateTime.of(1,1,1,1,1), LocalDateTime.of(1,1,1,1,1)))
                    )
            );
            return Observable.just(response);
        });
        app.show(gameOverviewController, Map.of("game", game)); //build start situation
    }

    @Test
    void testMemberList(){
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Game Overview", stage.getTitle());
        waitForFxEvents();
        assertEquals(0, gameOverviewController.playerBox.getItems().size());
    }

    @Test
    void testAchievements(){
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Game Overview", stage.getTitle());
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#achievementsButton");
        verify(app, times(1)).show("/achievements", Map.of("backTo", "/gameOverview",
                "game", game));

    }

    @Test
    void testCreateAnEmpire(){
    }

    @Test
    void testChooseAnEmpire(){

    }

    @Test
    void testStartTheGame(){

    }

    @Test
    void testLeaveGame(){

    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, eventListener, gameMembersApiService, userApiService, tokenStorage, userListComponentProvider);
        subscriber = null;
        eventListener = null;
        gameMembersApiService = null;
        userApiService = null;
        tokenStorage = null;
        userListComponentProvider = null;
        gameOverviewController = null;
        super.stop();
    }
}
