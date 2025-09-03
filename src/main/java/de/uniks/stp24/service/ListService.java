package de.uniks.stp24.service;

import de.uniks.stp24.model.game.Jobs;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.ResourceBundle;

public class ListService {
    @Inject
    JobService jobService;
    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public ListService() {
    }


    public HBox createJobCell(Jobs item, Subscriber subscriber) {
        // All the Cell content will be in this container
        HBox container = new HBox();

        // Create the Texts
        Text fractionName = new Text();
        if (item.getFraction() != null) {
            fractionName = new Text(resources.getString("jobList.fraction") + ": " + ((item.getFraction().getName() == null) ? resources.getString("home.fraction") : item.getFraction().getName()));
        } else if (item.getFleet() != null) {
            fractionName = new Text(resources.getString("jobList.fleet") + ": " + item.getFleet().getName());
        }

        Text JobType = new Text(resources.getString("jobList.job") + ": " + resources.getString("jobList." + item.getType()));
        Text jobStatus = new Text(resources.getString("jobList.status") + ": " + ((item.getProgress() == 0) ? resources.getString("jobList.statusFalse") : resources.getString("jobList.statusTrue")));
        // Listen for changes in the progress
        subscriber.listen(item.listeners(), Jobs.PROPERTY_PROGRESS, evt -> jobStatus.setText(resources.getString("jobList.status") + ": " + ((item.getProgress() == 0) ? resources.getString("jobList.statusFalse") : resources.getString("jobList.statusTrue"))));

        // Create a container for the Texts
        VBox textContainer = new VBox();
        textContainer.getChildren().addAll(fractionName, JobType, jobStatus);

        // Create cancel button
        Button cancelButton = new Button(resources.getString("jobList.cancel"));
        cancelButton.setOnAction(event -> cancelJob(item, subscriber));

        // Add spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add progress bar
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMinWidth(60);
        progressBar.setMaxWidth(60);
        progressBar.setMinHeight(15);
        progressBar.setProgress(item.getProgress() / item.getTotal());
        // Listen for changes in the progress
        subscriber.listen(item.listeners(), Jobs.PROPERTY_PROGRESS, evt -> progressBar.setProgress(item.getProgress() / item.getTotal()));

        // Add progress bar and cancel button to elementContainer
        VBox elementContainer = new VBox();
        elementContainer.getChildren().addAll(progressBar, cancelButton);

        // Style everything
        fractionName.getStyleClass().setAll("jobListNameText");
        JobType.getStyleClass().setAll("jobListNameText");
        jobStatus.getStyleClass().setAll("jobListNameText");
        progressBar.getStyleClass().add("jobListProgressBar");
        cancelButton.getStyleClass().setAll("jobListCancleButton");

        container.getChildren().addAll(textContainer, spacer, elementContainer);

        return container;
    }

    private void cancelJob(Jobs job, Subscriber subscriber) {
        subscriber.subscribe(jobService.deleteJob(job.get_id()));
    }

    public void setCustomCellFactory(ListView<Jobs> jobsList, Subscriber subscriber) {
        jobsList.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(Jobs item, boolean empty) {
                super.updateItem(item, empty);
                // if (!empty && item != null && item.getType() != null) {
                if (empty || item == null) {    //
                    setText(null);              //
                    setGraphic(null);           //
                } else {                        //
                    setGraphic(createJobCell(item, subscriber));
                }
            }
        });
    }

}
