package de.uniks.stp24.service;

import de.uniks.stp24.dto.*;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.rest.GameSystemsApiService;
import de.uniks.stp24.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import org.fulib.fx.annotation.controller.Resource;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.*;

public class BuildingService {

    @Inject
    GameSystemsApiService gameSystemsApiService;

    @Inject
    PresetsApiService presetsApiService;
    @Inject
    GameEmpiresApiService gameEmpiresApiService;

    @Inject
    public BuildingService() {
    }
    @Inject
    @Resource
    ResourceBundle bundle;

    public Observable<Response<FractionDto>> removeBuilding(BuildingDto currentBuilding, Game game, Fraction fraction) {
        List<String> reducedBuildings = new LinkedList<>(fraction.getBuildings());
        reducedBuildings.remove(currentBuilding.id());
        UpdateFractionDto updateDto = new UpdateFractionDto(null, null, reducedBuildings.toArray(new String[0]), null, fraction.getEmpire().get_id(), null);
        return gameSystemsApiService.updateFraction(game.getId(), fraction.get_id(), updateDto);
    }


    public Observable<List<BuildingDto>> getNotOwnedBuildings(Game game, Fraction fraction) {
        return presetsApiService.getBuildings().map(buildings -> buildings.stream().filter(building -> !fraction.getBuildings().contains(building.id())).toList());
    }


    public Observable<Response<FractionDto>> build(BuildingDto currentBuilding, Game game, Fraction fraction) {

        List<String> newBuildings = new LinkedList<>(fraction.getBuildings());
        newBuildings.add(currentBuilding.id());
        UpdateFractionDto updateDto = new UpdateFractionDto(null, null, newBuildings.toArray(new String[0]), null, fraction.getEmpire().get_id(), null);
        return gameSystemsApiService.updateFraction(game.getId(), fraction.get_id(), updateDto);
    }


}
