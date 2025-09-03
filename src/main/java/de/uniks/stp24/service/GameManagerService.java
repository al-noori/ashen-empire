package de.uniks.stp24.service;

import de.uniks.stp24.dto.CreateGameDto;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameSettingsDto;
import de.uniks.stp24.rest.GamesApiService;
import de.uniks.stp24.util.response.ResponseEnum;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.AsyncSubject;

import javax.inject.Inject;
import java.util.Objects;

public class GameManagerService {
    @Inject
    GamesApiService gamesApiService;
    @Inject
    TokenStorage tokenStorage;

    @Inject
    public GameManagerService() {
    }

    public Observable<CreateGameResponseDto> createGame(String name, String password, Integer mapSize) {
        AsyncSubject<CreateGameResponseDto> subject = AsyncSubject.create();

        Disposable subscribed = gamesApiService.createGame(
                new CreateGameDto(
                        name,
                        password,
                        false,
                        0,
                        new GameSettingsDto(mapSize)
                )
        ).subscribe(
                result -> {
                    assert result.body() != null;
                    subject.onNext(result.body());
                    subject.onComplete();
                }, error -> {
                    subject.onError(error);
                    subject.onComplete();
                }
        );
        return subject.doOnDispose(subscribed::dispose);
    }

    public Observable<ResponseEnum> editGame(
            String id,
            String name,
            String password,
            int mapSize
    ) {
        AsyncSubject<ResponseEnum> subject = AsyncSubject.create();
        Disposable subscribed = gamesApiService.updateGame(
                id, false,
                new CreateGameDto(
                        name,
                        password,
                        null,
                        null,
                        new GameSettingsDto(
                                mapSize
                        )
                )
        ).subscribe(res -> {
            subject.onNext(ResponseEnum.fromInt(res.code()));
            subject.onComplete();
        }, error -> {
            subject.onNext(ResponseEnum.ERROR);
            subject.onComplete();
        });
        return subject.doOnDispose(subscribed::dispose);
    }

    public Observable<CreateGameResponseDto> getGame(
            String id
    ) {
        AsyncSubject<CreateGameResponseDto> subject = AsyncSubject.create();

        Disposable subscribed = gamesApiService.getGameById(id).subscribe(res -> {
            subject.onNext(Objects.requireNonNull(res.body()));
            subject.onComplete();
        }, error -> {
            subject.onError(error);
            subject.onComplete();
        });
        return subject.doOnDispose(subscribed::dispose);
    }

    public Observable<Boolean> deleteGame(
            String id
    ) {
        AsyncSubject<Boolean> subject = AsyncSubject.create();
        Disposable subscribed = gamesApiService.deleteGame(id).subscribe(res -> {
            subject.onNext(res.isSuccessful());
            subject.onComplete();
        }, error -> {
            subject.onNext(false);
            subject.onComplete();
        });
        return subject.doOnDispose(subscribed::dispose);
    }
}
