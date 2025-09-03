package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameSettingsDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.service.GameManagerService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.UserService;
import de.uniks.stp24.ws.Event;
import de.uniks.stp24.ws.EventListener;
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
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class CreateGameControllerTest extends ControllerTest {
    @Spy
    Subscriber subscriber = spy(new Subscriber());
    @InjectMocks
    CreateGameController createGameController;
    @Spy
    UserService userService = spy(UserService.class);
    @Spy
    GameManagerService gameManagerService = spy(GameManagerService.class);
    @Spy
    ResourceBundle resources = spy(ResourceBundle.getBundle("de/uniks/stp24/lang/main", Locale.ENGLISH));
    @Spy
    GameService gameService = spy(GameService.class);
    EventListener eventListener = mock(EventListener.class);

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        app.show(createGameController);
        doReturn(Observable.just(new Game().setOwner(new User()))).when(gameService).loadGame(any());
        doReturn(Observable.just(new Event("", null))).when(eventListener).listen(any(), any());
    }
    @Test
    void cancelGameCreation() {
        //Title: A user wants to cancel the game creation
        //Start: A user is in the Create a new game window.
        assertEquals("Create a new game",stage.getTitle());
        waitForFxEvents();
        //Action: He pushes the Cancel button.
        clickOn("#cancelButton");
        waitForFxEvents();
        //Result: The user is back in the Lobby window.
        verify(app, times(1)).show("/lobby");
    }
    @Test
    void createGame() {
        doReturn(Observable.just(new CreateGameResponseDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "1",
                "game1",
                "1",
                1,
                1,
                false,
                1,
                1,
                new GameSettingsDto(100)))).when(gameManagerService).createGame(any(), any(), any());
        doReturn(Observable.just("testuser")).when(userService).getUserNameById(any());
        doReturn(null).when(app).show("/gameOverview");

        //Title: A user wants to create a new game
        //Start: A user is in the Create a new game window.
        assertEquals("Create a new game",stage.getTitle());

        //Action: He enters the name of the game, the password, and the map size. Then he pushes the Save button.
        clickOn("#nameField");
        write("game1\t");
        write("password1\t");
        write("100");
        clickOn("#saveButton");

        waitForFxEvents();
        //Result: The game is created and the user is in the Lobby window.
        verify(gameManagerService, times(1)).createGame("game1", "password1", 100);
        assertEquals("Game Overview",stage.getTitle());
    }
   @Test
   void createGameWithInvalidMapSize() {
       //Title: A user wants to create a new game with an invalid map size
       //Start: A user is in the Create a new game window.
       assertEquals("Create a new game",stage.getTitle());

       //Action: He enters the name of the game, the password, and an invalid map size. Then he pushes the Save button.
       clickOn("#nameField");
       write("game1\t");
       write("password1\t");
       write("0");
       clickOn("#saveButton");

       waitForFxEvents();

       //Result: The game is not created and the user is still in the Create a new game window.
       assertEquals(createGameController.statusLabel.getText(),"Map size must be between 50 and 200");
       verify(gameManagerService, never()).createGame(any(), any(), any());
       assertEquals("Create a new game",stage.getTitle());
   }
   @Test
    void createGameWithEmptyPassword() {
        //Title: A user wants to create a new game with a short password
        //Start: A user is in the Create a new game window.
        assertEquals("Create a new game",stage.getTitle());

        //Action: He enters the name of the game, a short password, and the map size. Then he pushes the Save button.
        clickOn("#nameField");
        write("game1\t");
        write("\t");
        write("100");
        clickOn("#saveButton");

        waitForFxEvents();

        //Result: The game is not created and the user is still in the Create a new game window.
        assertEquals(createGameController.statusLabel.getText(),"The password must be set!");
        verify(gameManagerService, never()).createGame(any(), any(), any());
        assertEquals("Create a new game",stage.getTitle());
    }
   @Test
   void createGameWithInvalidName() {
       //Title: A user wants to create a new game with an invalid name
       //Start: A user is in the Create a new game window.
       assertEquals("Create a new game",stage.getTitle());

       //Action: He enters an invalid name, the password, and the map size. Then he pushes the Save button.
       clickOn("#nameField");
       write("\t");
       write("password1\t");
       write("100");
       clickOn("#saveButton");

       waitForFxEvents();

       //Result: The game is not created and the user is still in the Create a new game window.
       assertEquals("The name must be set!",createGameController.statusLabel.getText());
       verify(gameManagerService, never()).createGame(any(), any(), any());
       assertEquals("Create a new game",stage.getTitle());
    }
    @Test
    void createGameWithInvalidMapSizeInput() {
        //Title: A user wants to create a new game with an invalid map size input
        //Start: A user is in the Create a new game window.
        assertEquals("Create a new game",stage.getTitle());

        //Action: He enters the name of the game, the password, and an invalid map size input. Then he pushes the Save button.
        clickOn("#nameField");
        write("game1\t");
        write("password1\t");
        write("abc");
        clickOn("#saveButton");

        waitForFxEvents();

        //Result: The game is not created and the user is still in the Create a new game window.
        assertEquals(createGameController.statusLabel.getText(),"Map size must be a number");
        verify(gameManagerService, never()).createGame(any(), any(), any());
        assertEquals("Create a new game",stage.getTitle());
    }
    @Override
    public void stop() throws Exception {
        Mockito.reset(subscriber, gameManagerService, resources, eventListener);
        subscriber = null;
        gameManagerService = null;
        resources = null;
        createGameController = null;
        super.stop();
    }
}
