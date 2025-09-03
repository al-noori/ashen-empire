package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.ListService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "JobsList.fxml")
public class JobsListComponent extends VBox {

    @FXML
    ListView<Jobs> jobsList;
    @Inject
    Subscriber subscriber;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    App app;
    @Inject
    GameService gameService;
    @Inject
    JobService jobService;
    @Inject
    ListService listService;

    private final ObservableList<Jobs> jobs = FXCollections.observableArrayList();

    @Inject
    public JobsListComponent() {
    }

    @OnRender
    public void onRender() {
        if (gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null) {
            jobs.addAll(gameService.getOwnUser().getEmpire().getJobs());
            jobsList.setItems(jobs);

            listService.setCustomCellFactory(jobsList, subscriber);

            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(),
                    Empire.PROPERTY_JOBS,
                    evt -> Platform.runLater(() -> {
                        jobs.clear();
                        jobs.addAll(gameService.getOwnUser().getEmpire().getJobs());
                    }));
        }

    }

    public void onItemClicked() {
        Jobs selectedJob = jobsList.getSelectionModel().getSelectedItem();
        if (selectedJob != null && selectedJob.getFraction() != null) {
            app.show("/inGame/FractionDetails", Map.of("game", gameService.getGame(), "fraction", selectedJob.getFraction()));
        }
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
