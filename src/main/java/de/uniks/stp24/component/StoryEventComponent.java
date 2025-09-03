package de.uniks.stp24.component;

import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.dto.EffectDto;
import de.uniks.stp24.dto.EffectSourceDto;
import de.uniks.stp24.dto.UpdateEmpireDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.service.*;
import io.reactivex.rxjava3.core.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component(view = "StoryEvent.fxml")
public class StoryEventComponent extends AnchorPane {

    public StackPane stackPane;
    @FXML
    Button optionOne;
    @FXML
    Button optionTwo;
    @FXML
    ImageView eventImage;
    @FXML
    Label eventText;

    @Param("game")
    Game game;

    @Inject
    JobService jobService;

    @Inject
    ImageCache imageCache;

    @Inject
    GameEmpiresApiService gameEmpiresApiService;

    @Inject
    Subscriber subscriber;

    @Inject
    GameService gameService;

    @Inject
    BuildingService buildingService;

    @Inject
    GameControlMenuService gameControlMenuService;

    @Inject
    public StoryEventComponent() {
    }

    @Inject
    @Resource
    ResourceBundle bundle;

    @Param("eventNumber")
    Integer eventNumber;

    private Map<Integer, String[]> eventMap;

   @OnInit
   public void init(){
       eventMap = Map.ofEntries(
               Map.entry(1, new String[]{
                       bundle.getString("event1.description"),
                       bundle.getString("event1.effect1"),
                       bundle.getString("event1.effect2")
               }),
               Map.entry(2, new String[]{
                       bundle.getString("event2.description"),
                       bundle.getString("event2.effect1"),
                       bundle.getString("event2.effect2")
               }),
               Map.entry(3, new String[]{
                       bundle.getString("event3.description"),
                       bundle.getString("event3.effect1"),
                       bundle.getString("event3.effect2")
               }),
               Map.entry(4, new String[]{
                       bundle.getString("event4.description"),
                       bundle.getString("event4.effect1"),
                       bundle.getString("event4.effect2")
               }),
               Map.entry(5, new String[]{
                       bundle.getString("event5.description"),
                       bundle.getString("event5.effect1"),
                       bundle.getString("event5.effect2")
               }),
               Map.entry(6, new String[]{
                       bundle.getString("event6.description"),
                       bundle.getString("event6.effect1"),
                       bundle.getString("event6.effect2")
               }),
               Map.entry(7, new String[]{
                       bundle.getString("event7.description"),
                       bundle.getString("event7.effect1"),
                       bundle.getString("event7.effect2")
               }),
               Map.entry(8, new String[]{
                       bundle.getString("event8.description"),
                       bundle.getString("event8.effect1"),
                       bundle.getString("event8.effect2")
               }),
               Map.entry(9, new String[]{
                       bundle.getString("event9.description"),
                       bundle.getString("event9.effect1"),
                       bundle.getString("event9.effect2")
               }),
               Map.entry(10, new String[]{
                       bundle.getString("event10.description"),
                       bundle.getString("event10.effect1"),
                       bundle.getString("event10.effect2")
               }),
               Map.entry(11, new String[]{
                       bundle.getString("event11.description"),
                       bundle.getString("event11.effect1"),
                       bundle.getString("event11.effect2")
               }),
               Map.entry(12, new String[]{
                       bundle.getString("event12.description"),
                       bundle.getString("event12.effect1"),
                       bundle.getString("event12.effect2")
               }),
               Map.entry(13, new String[]{
                       bundle.getString("event13.description"),
                       bundle.getString("event13.effect1"),
                       bundle.getString("event13.effect2")
               }),
               Map.entry(14, new String[]{
                       bundle.getString("event14.description"),
                       bundle.getString("event14.effect1"),
                       bundle.getString("event14.effect2")
               }),
               Map.entry(15, new String[]{
                       bundle.getString("event15.description"),
                       bundle.getString("event15.effect1"),
                       bundle.getString("event15.effect2")
               }),
               Map.entry(16, new String[]{
                       bundle.getString("event16.description"),
                       bundle.getString("event16.effect1"),
                       bundle.getString("event16.effect2")
               })
       );

   }
    @OnRender
    public void render() {

        String[] event = eventMap.get(eventNumber);
        eventImage.setImage(imageCache.get("events/Event_Nr-" + eventNumber + ".jpg"));
        eventText.setText(event[0]);
        optionOne.setText(event[1]);
        optionTwo.setText(event[2]);
    }

    public void execOptionOne() {
        switch (eventNumber) {
            case 1:
                updateResources(Map.of("energy", -5));
                break;
            case 2:
                updateResources(Map.of("food", -10));
                break;
            case 3:
                updateVariables(List.of(
                                "buildings.mine.production.minerals",
                                "buildings.research_lab.production.research",
                                "buildings.foundry.production.alloys",
                                "buildings.factory.production.consumer_goods",
                                "buildings.refinery.production.fuel"
                        ),
                        1.2);
                break;
            case 4:
                updateResources(Map.of("credits", -10));
                break;
            case 5:
                updateVariables(List.of("districts.ancient_factory.cost.minerals"),
                        1.2);
                break;
            case 6:
                Integer currentOilAmount = gameService.getOwnUser().getEmpire()
                        .getResources().get("fuel");
                updateResources(Map.of("oil", -currentOilAmount / 2));
                break;
            case 7, 8:
                updateResources(Map.of("credits", 8));
                break;
            case 9:
                updateVariables(List.of("districts.ancient_factory.production.consumer_goods"),
                        1.2);
                break;
            case 10:
                Map<String, Integer> resources = gameService.getOwnUser().getEmpire()
                        .getResources();
                updateResources(Map.of(
                        "credits", resources.get("credits"),
                        "food", resources.get("food"),
                        "fuel", resources.get("fuel"),
                        "minerals", resources.get("minerals"),
                        "alloys", resources.get("alloys"),
                        "consumer_goods", resources.get("consumer_goods"),
                        "research", resources.get("research"),
                        "energy", resources.get("energy"))
                );
                break;
            case 11:
                updateResources(Map.of("food", 8));
                break;
            case 12:
                updateResources(Map.of("consumer_goods", -17));
                break;
            case 13:
                updateVariables(List.of(
                        "buildings.factory.production.consumer_goods",
                        "districts.ancient_factory.production.consumer_goods"
                ), 1.2);
            case 14:
                updateResources(Map.of("minerals", -6));
                break;
            case 15:
                updateResources(Map.of("minerals", -10));
                break;
            case 16:
                updateVariables(List.of("buildings.power_plant.upkeep.minerals"),
                        1.2);
                break;
            default:
                System.out.println("Event not found");
                break;
        }
        stackPane.getChildren().remove(this);
    }

    public void execOptionTwo() {
        switch (eventNumber) {
            case 1:
                subscriber.subscribe(
                        buildingService.getNotOwnedBuildings(game, gameService.getOwnUser().getEmpire().getHomeSystem()),
                        buildings -> {
                            List<String> buildingsWithCost = buildings.stream()
                                    .filter(building -> building.cost().get("minerals") != null)
                                    .map(BuildingDto::id)
                                    .toList();
                            String var = "buildings." + buildingsWithCost.getFirst() + ".cost.minerals";
                            updateVariables(List.of(var), 0.9);
                        }
                );

                break;
            case 2:
                updateResources(Map.of("credits", -4));
                break;
            case 3:
                updateResources(Map.of("energy", 10));
                break;
            case 4:
                updateVariables(List.of("districts.agriculture.production.food",
                                "buildings.farm.production.food"),
                        0.85);
                break;
            case 5:
                updateVariables(List.of("districts.research_site.upkeep.energy",
                                "districts.research_site.upkeep.consumer_goods"),
                        0.9);
                break;
            case 6:
                updateResources(Map.of("food", 10));
                break;
            case 7:
                int coinsAmount = gameService.getOwnUser().getEmpire()
                        .getResources().get("credits");
                updateResources(Map.of("credits", coinsAmount));
                break;
            case 8:
                updateVariables(List.of(
                                "buildings.mine.production.minerals",
                                "buildings.research_lab.production.research",
                                "buildings.foundry.production.alloys",
                                "buildings.factory.production.consumer_goods",
                                "buildings.refinery.production.fuel"
                                ),
                        0.9);

                break;
            case 9:
                gameControlMenuService.stopPeriodicUpdate();
                Observable.interval(2, TimeUnit.SECONDS)
                        .take(2)
                        .subscribe(tick -> gameControlMenuService.startPeriodicUpdate(game, 1)
                        );
                break;
            case 10:
                int weaponsAmount = gameService.getOwnUser().getEmpire()
                        .getResources().get("minerals");
                updateResources(Map.of("minerals", -weaponsAmount / 5));
                break;
            case 11:
                updateResources(Map.of("credits", -10));
                break;
            case 12:
                updateVariables(List.of("districts.agriculture.production.food",
                                "buildings.farm.production.food"),
                        0.95);
                break;
            case 13:
                updateResources(Map.of("alloys", -10));
                break;
            case 14:
                updateResources(Map.of("credits", 3));
                break;
            case 15:
                updateVariables(List.of("buildings.foundry.upkeep.minerals",
                                "buildings.foundry.upkeep.energy"),
                        1.1);
                break;
            case 16:
                updateVariables(List.of("empire.market.fee"),
                        1.1);
                break;
            default:
                System.out.println("Event not found");
                break;
        }
        stackPane.getChildren().remove(this);
    }

    private void updateVariables(List<String> variables, double mult) {
        List<EffectDto> effects = new LinkedList<>();
        variables.forEach(s -> effects.add(
                new EffectDto(s, 0, mult, 0)
        ));
        createEffect(variables, effects);
    }

    private void updateResources(Map<String, Integer> res) {
        subscriber.subscribe(gameEmpiresApiService.editOne(
                game.getId(),
                gameService.getOwnUser().getEmpire().get_id(),
                true,
                new UpdateEmpireDto(res, null)
        ));
    }

    private void createEffect(List<String> res, List<EffectDto> effects) {
        String delimiter = "_";
        List<EffectSourceDto> effectSources = List.of(
                new EffectSourceDto("Event_" +
                        String.join(delimiter, res), effects)
        );
        if (gameService.getOwnUser() != null) {
            subscriber.subscribe(
                    gameEmpiresApiService.editOne(
                            game.getId(),
                            gameService.getOwnUser().getEmpire().get_id(),
                            true,
                            new UpdateEmpireDto(null, effectSources)
                    )
            );
        }
    }


}
