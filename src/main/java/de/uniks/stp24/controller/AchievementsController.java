package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.model.Achievement;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.service.AchievementsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
@Title("Achievements")
public class AchievementsController {
    @FXML
    TableColumn<Achievement, String> unlockedAtColumn;
    @FXML
    TableColumn<Achievement, String> progressColumn;
    @FXML
    TableColumn<Achievement, String> descriptionColumn;
    @FXML
    TableView<Achievement> table;
    @FXML
    TableColumn<Achievement, String> nameColumn;
    @FXML
    Button backButton;

    @Inject
    public AchievementsController() {
    }

    @Inject
    App app;
    @Inject
    AchievementsService achievementService;
    @Inject
    Subscriber subscriber;
    @Inject
    DateTimeFormatter dateTimeFormatter;
    @Param("backTo")
    String backTo = "/lobby";
    @Param("game")
    Game game;
    @Param("empireSet")
    boolean empireSet;
    @Inject
    @Resource
    ResourceBundle resources;

    @OnRender
    public void onRender() {
        subscriber.subscribe(achievementService.getAchievementsFromUser(),
                achievements -> {
                    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                    progressColumn.setCellValueFactory(cellDataFeatures -> {
                        Achievement achievement = cellDataFeatures.getValue();
                        return new SimpleStringProperty(achievement.getProgress() + "/" + achievement.getMaxProgress());
                    });
                    unlockedAtColumn.setCellValueFactory(cellDataFeatures -> {
                        Achievement achievement = cellDataFeatures.getValue();
                        if (achievement.getUnlockedAt() != null) return new SimpleStringProperty(
                                achievement.getUnlockedAt().format(
                                        dateTimeFormatter
                                )
                        );
                        else return new SimpleStringProperty(resources.getString("not.unlocked.yet"));
                    });
                    table.setItems(FXCollections.observableArrayList(achievements));
                },
                error -> {
                }
        );
    }

    public void backButtonClicked() {
        if (backTo.equals("/gameOverview")) {
            app.show(backTo, Map.of("game", game, "empireSet", empireSet));
        } else if (backTo.equals("/inGame")) {
            app.show(backTo, Map.of("game", game));
        } else {
            app.show(backTo);
        }
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        subscriber = null;
        app = null;
        achievementService = null;
        dateTimeFormatter = null;
    }
}
