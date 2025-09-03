package de.uniks.stp24.component;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.service.GameControlMenuService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Response;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class GameControlMenuComponentTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    GameControlMenuService gameControlMenuService = spy(GameControlMenuService.class);
    @InjectMocks
    GameControlMenuComponent gameControlMenuComponent;
    @Spy
    GameService gameService = spy(GameService.class);
    @Spy
    ImageCache imageCache = spy(ImageCache.class);
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    GamesApiService gamesApiService = mock(GamesApiService.class);

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        gamesApiService = gameControlMenuComponent.gamesApiService;
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
        ))).when(this.gamesApiService).getGameById("testGameID");
        User user = new User().set_id("testUserID");
        Game game = new Game().setSpeed(1).withMembers(user).setOwner(user).setId("testGameID");
        doReturn(user).when(gameService).getOwnUser();
        doAnswer(invocation -> Observable.just(
                new GameDto(
                        null,
                        null,
                        "testGameID",
                        "testUserID",
                        null,
                        null,
                        (int) invocation.getArguments()[1],
                        null,
                        null,
                        1,
                        null)
        )).when(gameControlMenuService).updateSpeed(any(Game.class), anyInt());

        app.show(gameControlMenuComponent, Map.of("game", game));


    }

    @Test
    void test() {
        Assertions.assertNotNull(gameControlMenuComponent);
        Label gameStatusLabel = lookup("#gameStatusLabel").query();
        Assertions.assertEquals("Game Status: Play", gameStatusLabel.getText());
        clickOn("#doubleSpeedButton");
        waitForFxEvents();
        verify(gameControlMenuService, times(1)).updateSpeed(any(Game.class), eq(2));
        Assertions.assertEquals("Game Status: Fast", gameStatusLabel.getText());
        clickOn("#tripleSpeedButton");
        waitForFxEvents();
        verify(gameControlMenuService, times(1)).updateSpeed(any(Game.class), eq(3));
        Assertions.assertEquals("Game Status: Very fast", gameStatusLabel.getText());
        clickOn("#zeroSpeedButton");
        waitForFxEvents();
        verify(gameControlMenuService, times(1)).updateSpeed(any(Game.class), eq(0));
        Assertions.assertEquals("Game Status: Paused", gameStatusLabel.getText());
    }
}
