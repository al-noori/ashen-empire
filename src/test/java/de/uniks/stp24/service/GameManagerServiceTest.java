package de.uniks.stp24.service;

import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameSettingsDto;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.util.response.ResponseEnum;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Response;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to Mock
public class GameManagerServiceTest {
    @Spy
    GamesApiService gamesApiService;
    @InjectMocks
    GameManagerService gameManagerService;

    @Test
    void createGameTest() {
        doReturn(Observable.just(
                Response.success(new CreateGameResponseDto(
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
                        new GameSettingsDto(50))
                ))
        ).when(gamesApiService).createGame(any());
        Observable<CreateGameResponseDto> response = gameManagerService.createGame("test", "test", 150);
        verify(gamesApiService, times(1)).createGame(any());
        assertEquals(new CreateGameResponseDto(
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
                new GameSettingsDto(50)), response.blockingFirst());
    }
    @Test
    void deleteGameTest() {
        doReturn(Observable.just(
                Response.success(new CreateGameResponseDto(
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
                        new GameSettingsDto(50))
                ))
        ).when(gamesApiService).deleteGame(any());
        Observable<Boolean> response = gameManagerService.deleteGame("507f191e810c19729de860ea");
        verify(gamesApiService, times(1)).deleteGame(any());
        assertEquals(true, response.blockingFirst());
    }
    @Test
    void getGameTest() {
        CreateGameResponseDto responseDto = new CreateGameResponseDto(
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
        doReturn(Observable.just(
                        Response.success(responseDto)
                )
        ).when(gamesApiService).getGameById(eq("507f191e810c19729de860ea"));
        Observable<CreateGameResponseDto> response = gameManagerService.getGame("507f191e810c19729de860ea");
        verify(gamesApiService, times(1)).getGameById(any());
        assertEquals(responseDto, response.blockingFirst());
    }
    @Test
    void updateGameTest() {
        CreateGameResponseDto responseDto = new CreateGameResponseDto(
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
        doReturn(Observable.just(
                        Response.success(responseDto)
                )
        ).when(gamesApiService).updateGame(eq("507f191e810c19729de860ea"),anyBoolean(), any());
        Observable<ResponseEnum> response = gameManagerService.editGame(
                "507f191e810c19729de860ea",
                "gutes Spiel",
                "1234567810",
                150);
        verify(gamesApiService, times(1)).updateGame(any(),anyBoolean(), any());
        assertEquals(ResponseEnum.OK, response.blockingFirst());
    }
}
