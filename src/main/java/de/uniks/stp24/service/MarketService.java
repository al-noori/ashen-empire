package de.uniks.stp24.service;

import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.dto.ResourcePresetDto;
import de.uniks.stp24.dto.VariableDto;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.PresetsApiService;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class MarketService {

    private Map<String, ResourcePresetDto> resourceCache;

    @Inject
    PresetsApiService presetsApiService;
    @Inject
    GameService gameService;
    @Inject
    EmpireApiService empireApiService;

    @Inject
    public MarketService() {
    }

    public @NonNull Observable<Map<String, ResourcePresetDto>> loadResources() {
        if (resourceCache != null) {
            return Observable.just(resourceCache);
        }
        return presetsApiService.getResources().doOnNext(resourceMap -> resourceCache = resourceMap);
    }

    public Observable<Integer> getCreditValue(String resourceName) {
        if (resourceCache != null) {
            return Observable.just(resourceCache.get(resourceName).creditValue());
        }
        return loadResources().map(resourceMap -> resourceMap.get(resourceName).creditValue());

    }
    public Observable<Double> getMarketFee() {
        return gameService.getVariable("empire.market.fee")
                .map(VariableDto::finalValue);
    }
    public Observable<GameEmpireDto> trade(String resource, int count){
        return empireApiService.update(
                gameService.getGame().getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                new GameEmpireDto(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        Map.of(resource, count),
                        null
                )
        );
    }
}