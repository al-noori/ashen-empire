package de.uniks.stp24.service;

import de.uniks.stp24.dto.PresetShipDto;
import de.uniks.stp24.rest.PresetsApiService;
import de.uniks.stp24.dto.VariableDto;
import io.reactivex.rxjava3.core.Observable;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TankService {
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    GameService gameService;
    @Inject
    Subscriber subscriber;
    @Inject
    PresetsApiService presetsApiService;
    private List<PresetShipDto> ships;

    @Inject
    public TankService() {
    }

    public String getTankName(String tankType) {
        return resources.getString("tankType." + tankType);
    }

    public String getTankSpeed(int tankSpeed) {
        if (tankSpeed < 7) {
            return resources.getString("tankSpeed.5");
        } else if (tankSpeed < 14) {
            return resources.getString("tankSpeed.8");
        } else {
            return resources.getString("tankSpeed.10");
        }
    }

    public Observable<Double> getMaxHealth(String shipId) {
        String variablePath = "ships." + shipId + ".health";
        return gameService.getVariable(variablePath)
                .map(VariableDto::finalValue);
    }

    public String getTankHealth(int tankHealth) {
        return tankHealth + "/";
    }

    public Observable<Double> getBuildTime(String shipId) {
        String variablePath = "ships." + shipId + ".build_time";
        return gameService.getVariable(variablePath)
                .map(VariableDto::finalValue);
    }

    public Boolean enoughResources(Map<String, Integer> shipCost) {

        // Get the player's current resources from the game service
        Map<String, Integer> playerResources = gameService.getOwnUser().getEmpire().getResources();

        for (Map.Entry<String, Integer> entry : shipCost.entrySet()) {
            String resource = entry.getKey();
            int cost = entry.getValue();
            int playerAmount = playerResources.getOrDefault(resource, 0);

            if (playerAmount < cost) {
                return false; // Disable button (player does not have enough resources)
            }
        }
        return true; // Enable button (player has enough resources)
    }

    public boolean resourcesCheck(String shipType, List<PresetShipDto> ships) {
        PresetShipDto ship = ships.stream().filter(s -> s.id().equals(shipType)).findFirst().orElse(null);

        if (ship == null) {
            return false;
        }

        Map<String, Integer> cost = ship.cost();
        Map<String, Integer> currentResources = gameService.getOwnUser().getEmpire().getResources();

        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
            String resource = entry.getKey();
            int costAmount = entry.getValue();
            int currentAmount = currentResources.getOrDefault(resource, 0);

            if (currentAmount < costAmount) {
                return false;
            }
        }
        return true;
    }

    public Observable<List<PresetShipDto>> getShips() {
        if (ships == null) {
            return presetsApiService.getShips()
                    .doOnNext(fetchedShips -> this.ships = fetchedShips);
        }
        return Observable.just(ships);
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
