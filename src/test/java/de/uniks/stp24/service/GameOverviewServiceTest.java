package de.uniks.stp24.service;

import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class GameOverviewServiceTest {
    @Spy
    GameOverviewService gameOverviewService;

    @Test
    void deleteMember(){
        doReturn(Observable.just(true)).when(gameOverviewService).deleteMember("12345678", "987654321");

        Observable<Boolean> result = gameOverviewService.deleteMember("12345678", "987654321");

        assertEquals(true, result.blockingFirst());
    }
}
