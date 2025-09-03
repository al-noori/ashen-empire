package de.uniks.stp24.service;

import de.uniks.stp24.rest.GameMembersApiService;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;

public class GameOverviewService {

    @Inject
    GameMembersApiService gameMembersApiService;

    @Inject
    Subscriber subscriber;

    @Inject
    public GameOverviewService() {
    }

    public Observable<Boolean> deleteMember(
            String game, String id
    ) {
        AsyncSubject<Boolean> subject = AsyncSubject.create();
        Disposable subscribed = gameMembersApiService.leaveGame(game, id).subscribe(res -> {
            subject.onNext(res.isSuccessful());
            subject.onComplete();
        }, error -> {
            subject.onNext(false);
            subject.onComplete();
        });
        return subject.doOnDispose(subscribed::dispose);
    }
}
