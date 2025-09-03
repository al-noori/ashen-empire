package de.uniks.stp24.service;

import de.uniks.stp24.dto.FractionDto;
import de.uniks.stp24.rest.GameSystemsApiService;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class GameSystemsService {
    String currentGame;
    List<FractionDto> systemsCache;

    @Inject
    GameSystemsApiService gameSystemsApiService;

    @Inject
    public GameSystemsService() {
    }

    public Observable<Response<List<FractionDto>>> getSystems(String game) {

        if (currentGame != null && currentGame.equals(game) && systemsCache != null) {
            return Observable.just(Response.success(systemsCache));
        }
        return gameSystemsApiService.getAllFractions(game).map(
                res -> {
                    if (res.isSuccessful()) {
                        systemsCache = res.body();
                    }
                    return res;
                }
        );
    }
}
