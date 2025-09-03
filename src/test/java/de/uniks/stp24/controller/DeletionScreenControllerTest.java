package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.service.GameManagerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import org.fulib.fx.controller.Subscriber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class DeletionScreenControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @Spy
    GameManagerService gameManagerService = spy(GameManagerService.class);
    @InjectMocks
    DeletionScreenController deletionScreenController;
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.show(deletionScreenController, Map.of("id", "507f191e810c19729de860ea")); //build start situation
    }
    @Test
    void deleteGameTest() {
        doReturn(Observable.just(true)).when(gameManagerService).deleteGame(
                eq("507f191e810c19729de860ea"));
        assertEquals("Deletion Screen", stage.getTitle());
        waitForFxEvents();
        clickOn("#deleteButton");
        waitForFxEvents();
        verify(gameManagerService, times(1)).deleteGame("507f191e810c19729de860ea");
        verify(app, times(1)).show("/lobby");
        assertEquals("Lobby", stage.getTitle());
    }
    @Test
    void abortDeleteGameTest() {
        assertEquals("Deletion Screen", stage.getTitle());
        waitForFxEvents();
        clickOn("#abortButton");
        waitForFxEvents();
        verify(app, times(1)).show("/lobby");
        assertEquals("Lobby", stage.getTitle());
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, gameManagerService, resources);
        subscriber = null;
        gameManagerService = null;
        resources = null;
        deletionScreenController = null;
        super.stop();
    }
}
