package de.uniks.stp24.service;

import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.UpdateGameDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GamesApiService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;


@Singleton
public class GameControlMenuService {
    @Inject
    GamesApiService gamesApiService;
    public Disposable periodicUpdateDisposable;

    @Inject
    public GameControlMenuService() {
    }

    public Observable<GameDto> updateSpeed(Game game, Number speed) {
        return gamesApiService.updateCurrentGame(game.getId(), true, new UpdateGameDto(null, null, null, speed, null));
    }


    public void startPeriodicUpdate(Game game, int currentSpeed) {
        long interval = calculateInterval(currentSpeed);
        if (periodicUpdateDisposable != null && (!periodicUpdateDisposable.isDisposed() || currentSpeed == 0)) {
            periodicUpdateDisposable.dispose();
        }
        if (interval > 0) {
            periodicUpdateDisposable = Observable.interval(interval, TimeUnit.SECONDS)
                    .flatMap(tick -> updateSpeed(game, currentSpeed))
                    .subscribe(currentGame -> System.out.println("Game speed is " + currentGame.speed() +
                            " with interval " + interval + " seconds on period " + currentGame.period()));
        }
    }

    public void stopPeriodicUpdate() {
        if (periodicUpdateDisposable != null && !periodicUpdateDisposable.isDisposed()) {
            periodicUpdateDisposable.dispose();
        }
    }

    private long calculateInterval(int currentSpeed) {
        return switch (currentSpeed) {
            case 0 -> -1; // : stop
            case 1 -> 30; // : 10 ticks per minute
            case 2 -> 20; // : 20 ticks per minute
            case 3 -> 10; // : 30 ticks per minute
            default -> 60;
        };
    }

}
