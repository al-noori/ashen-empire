package de.uniks.stp24.controller;

import de.uniks.stp24.ControllerTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class LobbyControllerTest extends ControllerTest{

    /*

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize the components needed for testing



        mockDialog = mock(TextInputDialog.class);
        //when(mockDialog.showAndWait()).thenReturn(Optional.of("validpassword"));
        // Use dependency injection or reflection to set the mock dialog in the controller
    }


    @Test
    void testGameListModifications() {
        // Mock the response from gamesApiService.getAllGames()
        CreateGameResponseDto dto1 = new CreateGameResponseDto(null, null, "1234", "game1", "user1", false, 0,0,null);
        CreateGameResponseDto dto2 = new CreateGameResponseDto(null, null, "4321", "game2", "user2", false, 0,0,null);
        List<CreateGameResponseDto> dtos = Arrays.asList(dto1, dto2);

        Response<List<CreateGameResponseDto>> response = Response.success(dtos);
        doReturn(Observable.just(response)).when(mockGamesApiService).getAllGames();
        //when(mockGamesApiService.getAllGames()).thenReturn(Observable.just(response));

        // Mock event listener
        Observable<Event<Game>> eventObservable = Observable.empty(); // Assuming no events for simplicity
        when(mockEventListener.listen(eq("games.*.*"), eq(Game.class))).thenReturn(eventObservable);

        // Call init method
        Platform.runLater(() -> {
            lobbyController.init();
        });

        // Verify that gamesApiService.getAllGames() was called
        verify(mockGamesApiService).getAllGames();

        // Verify that games list was populated correctly
        assertEquals(2, lobbyController.games.size());
        assertEquals("1234", lobbyController.games.get(0)._id());
        assertEquals("4321", lobbyController.games.get(1)._id());

        // Verify subscriber subscription
        verify(subscriber).subscribe(eq(eventObservable), any());

        /*
        Game game1 = new Game(null, null, "1", "owner1", "Game 1", false, 1, 1, null);
        Game game2 = new Game(null, null, "2", "owner2", "Game 2", false, 1, 1, null);
        Game updatedGame1 = new Game(null, null, "1", "owner1", "Updated Game 1", false, 1, 1, null);

        ObservableList<Game> games = FXCollections.observableArrayList();
        lobbyController.games.setAll(games);

        // Add game
        Platform.runLater(() -> {
            lobbyController.games.add(game1);
            lobbyController.games.add(game2);
            assertTrue(lobbyController.games.contains(game1));
            assertTrue(lobbyController.games.contains(game2));
        });

        // Update game
        Platform.runLater(() -> {
            lobbyController.games.replaceAll(game -> game._id().equals(updatedGame1._id()) ? updatedGame1 : game);
            assertTrue(lobbyController.games.contains(updatedGame1));
            assertFalse(lobbyController.games.contains(game1));
        });


        // Remove game
        Platform.runLater(() -> {
            lobbyController.games.remove(game2);
            assertFalse(lobbyController.games.contains(game2));
        });


    }
    @Test
    void testGameManagementButtonFunctionality() {
        when(mockTokenStorage.getUserId()).thenReturn("user2");

        Game game = new Game(null, null, "1", "user1", "Game 1", false, 1, 1, null);
        lobbyController.gameList.getItems().add(game);
        lobbyController.gameList.getSelectionModel().select(game);

        // Simulate rendering logic
        Platform.runLater(() -> {
            lobbyController.render();
            assertTrue(lobbyController.gameManagement.isDisabled());
        });

        // Change selected game
        Game game1 = new Game(null, null, "2", "user2", "Game 1", false, 1, 1, null);
        lobbyController.gameList.getItems().add(game1);
        lobbyController.gameList.getSelectionModel().select(game1);
        MouseEvent mouseEvent = mock(MouseEvent.class);

        Platform.runLater(() -> {
            lobbyController.gameManagement(mouseEvent);
            assertEquals("Edit the Game", stage.getTitle());
        });



    }
    @Test
    void testCreateGame() {
        MouseEvent mouseEvent = mock(MouseEvent.class);

        // Create game without selection should do nothing
        Platform.runLater(() -> {
            // Create game with selection should switch scene
            lobbyController.createGame(mouseEvent);
            assertEquals("Create a new game", stage.getTitle());

        });
    }


    @Test
    void testDeleteGameButtonFunctionality() {
        when(mockTokenStorage.getUserId()).thenReturn("user2");

        Game game = new Game(null, null, "1", "user1", "Game 1", false, 1, 1, null);
        lobbyController.gameList.getItems().add(game);
        lobbyController.gameList.getSelectionModel().select(game);

        // Simulate rendering logic
        Platform.runLater(() -> {
            lobbyController.render();
            assertTrue(lobbyController.deleteGame.isDisabled());
        });

        // Change selected game
        Game game1 = new Game(null, null, "2", "user2", "Game 1", false, 1, 1, null);
        lobbyController.gameList.setItems(Observable.just(List<Game>).a);
        lobbyController.gameList.getSelectionModel().select(game1);
        MouseEvent mouseEvent = mock(MouseEvent.class);

        Platform.runLater(() -> {
            lobbyController.deleteGame(mouseEvent);
            assertEquals("Deletion Screen", stage.getTitle());

        });




    }

    @Test
    void testJoinGameButtonFunctionality() {
        WaitForAsyncUtils.waitForFxEvents();

        when(mockTokenStorage.getUserId()).thenReturn("user2");

        Game game1 = new Game(null, null, "1", "user1", "Game 1", false, 1, 1, null);
        // Use Platform.runLater to add the game and select it
        Platform.runLater(() -> {
            lobbyController.gameList.getItems().add(game1);
            lobbyController.gameList.getSelectionModel().select(game1);
        });
        MouseEvent mouseEvent = mock(MouseEvent.class);
        WaitForAsyncUtils.waitForFxEvents();
        // Simulate rendering logic
        Platform.runLater(() -> {
            lobbyController.joinTheGame(mouseEvent);
            assertFalse(lobbyController.joinTheGame.isDisabled());
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Mock TextInputDialog result
        String mockPassword = "validpassword";
        when(mockDialog.showAndWait()).thenReturn(Optional.of(mockPassword));
        //new


        // Verify the API call was made
        ArgumentCaptor<JoinGameDto> joinGameDtoCaptor = ArgumentCaptor.forClass(JoinGameDto.class);
        verify(mockGameMembersApiService.joinGame(), times(1)).joinGame(eq(game1._id()), joinGameDtoCaptor.capture());


        // Verify the correct password was passed
        JoinGameDto capturedDto = joinGameDtoCaptor.getValue();
        assertEquals(mockPassword, capturedDto.password());


        // Mock the response for joinGame API call
        when(mockGameMembersApiService.joinGame(eq(game1._id()), any(JoinGameDto.class)))
                .thenReturn(Observable.just(Response.success(null)));


        Platform.runLater(() -> {
            // Verify that the joinGame API is called with the correct parameters
            verify(mockGameMembersApiService).joinGame(eq(game1._id()), any(JoinGameDto.class));

            // Verify that the joinGame method is called with the correct parameters
            verify(lobbyController).joinGame(game1, mockPassword);
            });


    }*/
    /*
    @Test
    void testJoinGameWithValidPassword() {
        Game selectedGame = new Game(null, null, "1", "owner1", "Game 1", false, 1, 1, null);
        String expectedPassword = "validpassword";
        lobbyController.gameList.getItems().add(selectedGame);
        lobbyController.gameList.getSelectionModel().select(selectedGame);

        when(mockDialog.showAndWait()).thenReturn(Optional.of(expectedPassword));
        // Create a mock response for getPlayer
        GameMembersDto mockGameMembersDto = new GameMembersDto("1", "user", true, null, null, null);
        when(mockGameMembersApiService.getPlayer(anyString(), anyString()))
                .thenReturn(Observable.just(Response.success(mockGameMembersDto)));

        // Mock des joinGame, um das Passwort zu vergleichen
        when(mockGameMembersApiService.joinGame(anyString(), any(JoinGameDto.class)))
                .thenAnswer(invocation -> {
                    JoinGameDto dto = invocation.getArgument(1);
                    if (dto.password().equals(expectedPassword)) {
                        return Observable.just(Response.success(mockGameMembersDto));
                    } else {
                        return Observable.just(Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized")));
                    }
                });
        MouseEvent mouseEvent = mock(MouseEvent.class);

        Platform.runLater(() -> {
            lobbyController.joinTheGame(mouseEvent);
        });

        WaitForAsyncUtils.waitForFxEvents();
        assertEquals("Game Overview", stage.getTitle());
    }

*/
}
