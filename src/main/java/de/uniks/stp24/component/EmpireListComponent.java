package de.uniks.stp24.component;

import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.rest.GameEmpiresApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.ResourceBundle;

@Component(view = "EmpireList.fxml")
public class EmpireListComponent extends HBox implements ReusableItemComponent<Empire> {
    @FXML
    public ImageView empireImage;
    @FXML
    public Text empireNo;
    @FXML
    public ImageView economyImage;
    @FXML
    public Text economyText;
    @FXML
    public ImageView militaryImage;
    @FXML
    public Text militaryText;
    @FXML
    public ImageView techImage;
    @FXML
    public Text techText;
    @FXML
    public ImageView warImage;
    @FXML
    public Text warDefText;

    @Inject
    Subscriber subscriber;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    ImageCache imageCache;

    @Inject
    GameService gameService;

    @Inject
    EmpireApiService empireService;

    @Inject
    GameEmpiresApiService gameEmpiresApiService;

    @Inject
    public EmpireListComponent() {

    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

    @Override
    public void setItem(@NotNull Empire empire) {
        warDefText.setText("");
        warImage.setImage(imageCache.get("images/peace_icon.png"));

        subscriber.subscribe(empireService.getAll(gameService.getGame().getId()),
                response -> {
                    List<GameEmpireDto> empires = response.body();
                    assert empires != null;
                    for (GameEmpireDto empire1 : empires) {
                        if (empire1._id().equals(empire.get_id())) {
                            empireNo.setText(empire1.name());

                            gameService.getOwnUser().getEmpire().getAttackingWars().forEach(war -> {
                                if (war.getDefender().get_id().equals(empire1._id())) {
                                    warDefText.setText(">> DEFENDER");
                                    warImage.setImage(imageCache.get("images/war_icon.png"));
                                }
                            });

                            gameService.getOwnUser().getEmpire().getDefendingWars().forEach(war -> {
                                if (war.getAttacker().get_id().equals(empire1._id())) {
                                    warDefText.setText(">> ATTACKER");
                                    warImage.setImage(imageCache.get("images/war_icon.png"));
                                }
                            });
                        }
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );
        empireImage.setImage(imageCache.get("flags/Flag" + (empire.getFlag() + 1) + ".png"));

        economyImage.setImage(imageCache.get("icons/economy_icon.png"));
        militaryImage.setImage(imageCache.get("icons/tank_icon_yellow.png"));
        techImage.setImage(imageCache.get("icons/technology_icon.png"));

        subscriber.subscribe(gameEmpiresApiService.getEconomyCompareAggregate(gameService.getGame().getId(),
                        gameService.getOwnUser().getEmpire().get_id(), empire.get_id()),
                response -> {
                    switch (response.body().total()) {
                        case 0:
                            economyText.setText("Average");
                            break;
                        case 1:
                            economyText.setText("Strong");
                            break;
                        case 2:
                            economyText.setText("Very Strong");
                            break;
                        case -1:
                            economyText.setText("Weak");
                            break;
                        case -2:
                            economyText.setText("Very Weak");
                            break;
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

        subscriber.subscribe(gameEmpiresApiService.getTechnologyCompareAggregate(gameService.getGame().getId(),
                        gameService.getOwnUser().getEmpire().get_id(), empire.get_id()),
                response -> {
                    switch (response.body().total()) {
                        case 0:
                            techText.setText("Average");
                            break;
                        case 1:
                            techText.setText("Strong");
                            break;
                        case 2:
                            techText.setText("Very Strong");
                            break;
                        case -1:
                            techText.setText("Weak");
                            break;
                        case -2:
                            techText.setText("Very Weak");
                            break;
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

        subscriber.subscribe(gameEmpiresApiService.getMilitaryCompareAggregate(gameService.getGame().getId(),
                        gameService.getOwnUser().getEmpire().get_id(), empire.get_id()),
                response -> {
                    switch (response.body().total()) {
                        case 0:
                            militaryText.setText("Average");
                            break;
                        case 1:
                            militaryText.setText("Strong");
                            break;
                        case 2:
                            militaryText.setText("Very Strong");
                            break;
                        case -1:
                            militaryText.setText("Weak");
                            break;
                        case -2:
                            militaryText.setText("Very Weak");
                            break;
                    }
                },
                error -> {
                    System.out.println("Error: " + error);
                }
        );

    }
}
