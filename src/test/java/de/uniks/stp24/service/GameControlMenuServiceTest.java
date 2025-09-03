package de.uniks.stp24.service;

import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.UpdateGameDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.GamesApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class GameControlMenuServiceTest {
    @Spy
    GamesApiService gamesApiService;
    @InjectMocks
    GameControlMenuService gameControlMenuService;

    @Test
    void testUpdateSpeed() {
        Game currentGame = new Game().setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now())
                .setSpeed(1)
                .setId("testGameID")
                .setName("testGame")
                .setOwner(new User().set_id("testUserID"))
                .setPeriod(1);
        UpdateGameDto updateGameDto = new UpdateGameDto(null, null, null, 2, null);
        GameDto gameDto = new GameDto(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "testGameID",
                "testGame",
                "testUserID",
                true,
                2,
                1,
                null,
                1,
                "testUser");
        Mockito.doReturn(Observable.just(gameDto)).when(gamesApiService).updateCurrentGame("testGameID", true, updateGameDto);
        final Observable<GameDto> gameResult$ = gameControlMenuService.updateSpeed(currentGame, 2);
        final Observable<Game> newCurrentGame$ = gameResult$.map(res -> {
            currentGame.setUpdatedAt(res.updatedAt())
                    .setSpeed(res.speed())
                    .setPeriod(res.period())
                    .setName(res.name())
                    .setMaxPlayers(res.playersCount());
            return currentGame;

        });
        final Game newCurrentGame = newCurrentGame$.blockingFirst();
        final GameDto gameResult = gameResult$.blockingFirst();
        Assertions.assertEquals((int) gameResult.speed(), newCurrentGame.getSpeed());
        Mockito.verify(gamesApiService, Mockito.times(1)).updateCurrentGame("testGameID", true, updateGameDto);
    }

}
