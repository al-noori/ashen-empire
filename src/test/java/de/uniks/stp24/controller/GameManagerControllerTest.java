package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameSettingsDto;
import de.uniks.stp24.service.GameManagerService;
import de.uniks.stp24.util.response.ResponseEnum;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class GameManagerControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    GameManagerService gameManagerService = spy(GameManagerService.class);
    @InjectMocks
    GameManagerController gameManagerController;
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        CreateGameResponseDto dto = new CreateGameResponseDto (
                LocalDateTime.of(2024, 1, 2, 3, 4),
                LocalDateTime.of(2024, 5, 2, 3, 4),
                "507f191e810c19729de860ea",
                "okayes Spiel",
                "62fc9b33773277d12d28929b",
                0,
                0,
                false,
                1,
                0,
                new GameSettingsDto(50));
        doReturn(Observable.just(dto)).when(gameManagerService).getGame("507f191e810c19729de860ea");
        app.show(gameManagerController, Map.of("id", "507f191e810c19729de860ea")); //build start situation
    }

    @Test
    void editGame() {
        doReturn(Observable.just(ResponseEnum.OK)).when(gameManagerService).editGame(
                eq("507f191e810c19729de860ea"),
                eq("superSpiel"),
                eq("12345678"),
                eq(100));
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("100");
        clickOn("#saveChangesButton");
        verify(gameManagerService, times(1)).editGame(
                "507f191e810c19729de860ea",
                "superSpiel",
                "12345678",
                100);
        verifyThat("#statusLabel", hasText("Changes saved"));
    }
    @Test
    void editGameWithError() {
        doReturn(Observable.just(ResponseEnum.NOT_YOUR_GAME)).when(gameManagerService).editGame(
                eq("507f191e810c19729de860ea"),
                eq("superSpiel"),
                eq("12345678"),
                eq(100));
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("100");
        clickOn("#saveChangesButton");
        verify(gameManagerService, times(1)).editGame(
                "507f191e810c19729de860ea",
                "superSpiel",
                "12345678",
                100);
        verifyThat("#statusLabel", hasText("You are not the owner of this game"));
    }
    @Test
    void editGameWithInvalidPassword() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("");
        clickOn("#mapSizeInput").eraseText(3).write("100");
        clickOn("#saveChangesButton");
        verifyThat("#statusLabel", hasText("The password must be set!"));
    }
    @Test
    void editGameWithSmallMapSize() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("10");
        clickOn("#saveChangesButton");
        verifyThat("#statusLabel", hasText("Map size must be bigger than 50"));
    }
    @Test
    void editGameWithBigMapSize() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("1000");
        clickOn("#saveChangesButton");
        verifyThat("#statusLabel", hasText("Map size must be smaller than 200"));
    }
    @Test
    void editGameWithInvalidMapSize() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("10a0");
        clickOn("#saveChangesButton");
        verifyThat("#statusLabel", hasText("Map size must be a number"));
    }
    @Test
    void editGameWithEmptyName() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20);
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("100");
        clickOn("#saveChangesButton");
        verifyThat("#statusLabel", hasText("The name must be set!"));
    }
    @Test
    void editGameWithObservableError() {
        doReturn(Observable.error(new Error("Test"))).when(gameManagerService).editGame(
                eq("507f191e810c19729de860ea"),
                eq("superSpiel"),
                eq("12345678"),
                eq(100));
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        verifyThat("#nameInput", hasText("okayes Spiel"));
        verifyThat("#passwordInput", hasText(""));
        verifyThat("#mapSizeInput", hasText("50"));
        clickOn("#nameInput").eraseText(20).write("superSpiel");
        clickOn("#passwordInput").write("12345678");
        clickOn("#mapSizeInput").eraseText(3).write("100");
        clickOn("#saveChangesButton");
        verify(gameManagerService, times(1)).editGame(
                "507f191e810c19729de860ea",
                "superSpiel",
                "12345678",
                100);
        verifyThat("#statusLabel", hasText("Error: Test"));
    }
    @Test
    void cancelGameManager() {
        assertEquals("Edit the Game", stage.getTitle());
        waitForFxEvents();
        clickOn("#cancelButton");
        waitForFxEvents();
        verify(app, times(1)).show("/lobby");
        assertEquals("Lobby", stage.getTitle());
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, gameManagerService, resources);
        subscriber = null;
        gameManagerController = null;
        resources = null;
        gameManagerService = null;
        super.stop();
    }
}
