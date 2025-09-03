package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fraction;
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
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "JobsDetailsList.fxml")
public class JobsDetailsListComponent extends VBox {
    @FXML
    ListView<Jobs> jobsDetailsList;
    @Inject
    Subscriber subscriber;
    @Inject
    App app;
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    JobService jobService;
    @Inject
    ListService listService;

    @Param("fraction")
    Fraction fraction;

    private final ObservableList<Jobs> jobs = FXCollections.observableArrayList();

    @Inject
    public JobsDetailsListComponent() {
    }

    @OnRender
    public void onRender() {
        if (gameService.getOwnUser() != null) {
            jobs.addAll(fraction.getJobs());
            jobsDetailsList.setItems(jobs);

            listService.setCustomCellFactory(jobsDetailsList, subscriber);

            subscriber.listen(fraction.listeners(),
                    Empire.PROPERTY_JOBS,
                    evt -> Platform.runLater(() -> {
                        jobs.clear();
                        jobs.addAll(fraction.getJobs());
                    }));
        }

    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

}
