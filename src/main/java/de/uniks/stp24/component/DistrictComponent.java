package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.DistrictDto;
import de.uniks.stp24.dto.UpdateFractionDto;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.rest.GameSystemsApiService;
import de.uniks.stp24.service.DistrictService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.util.Tuple;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "District.fxml")
public class DistrictComponent extends HBox implements ReusableItemComponent<Tuple<DistrictDto, Fraction>> {
    @Inject
    App app;

    @FXML
    public ImageView districtImage;
    @FXML
    public Label districtNameLabel;
    @FXML
    public Button plusButton;
    @FXML
    public Button deleteButton;
    @FXML
    public GridPane gridPane;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    GameService gameService;

    @Inject
    DistrictService districtService;

    @Inject
    ImageCache imageCache;

    @Inject
    DistrictInformationComponent districtInformationComponent;

    @Inject
    Subscriber subscriber;

    @Inject
    GameSystemsApiService gameSystemsApiService;

    private Popup popup;

    @Inject
    JobService jobService;

    DistrictDto currentDistrict;
    Map<String, Integer> currentSlots;
    Map<String, Integer> maxSlots;
    Fraction fraction;

    @Inject
    public DistrictComponent() {
    }

    public void initialize() {
        if (gameService.amISpectator()) {
            plusButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    @Override
    public void setItem(@NotNull Tuple<DistrictDto, Fraction> tuple) {
        districtNameLabel.setText(districtService.getDistrictName(tuple.getFirst().id()));
        districtImage.setImage(imageCache.get(pathHandler(tuple.getFirst().id())));
        currentDistrict = tuple.getFirst();
        currentSlots = tuple.getSecond().getDistricts();
        maxSlots = tuple.getSecond().getDistrictSlots();
        setButtons();
        fraction = tuple.getSecond();
        if(fraction.getEmpire() == null || fraction.getEmpire().getOwner() != gameService.getOwnUser()) {
            plusButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        subscriber.listen(tuple.getSecond().listeners(), Fraction.PROPERTY_DISTRICTS, event -> {
            currentSlots = (Map<String, Integer>) event.getNewValue();
            Platform.runLater(() -> {
                setButtons();
                updateGrid();
            });
        });
        int slots = getMaxSlots();
        int gridSize = (int) Math.ceil(Math.sqrt(slots));
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        for (int i = 0; i < gridSize; i++) {
            RowConstraints rc = new javafx.scene.layout.RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
            ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }
        updateGrid();
    }

    private void updateGrid() {
        gridPane.getChildren().clear();
        for (int i = 0; i < gridPane.getColumnCount() * gridPane.getRowCount(); i++) {
            if (i < getSlots()) {
                gridPane.add(new Pane() {
                    {
                        setStyle("-fx-background-color: #444956;;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 1px;");
                    }
                }, i % gridPane.getColumnCount(), i / gridPane.getColumnCount());
            } else if (i < getMaxSlots()) {
                gridPane.add(new Pane() {
                    {
                        setStyle("-fx-background-color: #ffffff;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 1px;");
                    }
                }, i % gridPane.getColumnCount(), i / gridPane.getColumnCount());
            } else {
                gridPane.add(new Pane() {
                    {
                        setStyle("-fx-background-color: #000000;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 1px;");
                    }
                }, i % gridPane.getColumnCount(), i / gridPane.getColumnCount());
            }
        }
    }

    private int getSlots() {
        return currentSlots.getOrDefault(currentDistrict.id(), 0);
    }

    private int getMaxSlots() {
        return maxSlots.getOrDefault(currentDistrict.id(), 0);
    }

    private void setButtons() {
        int slots = this.currentSlots.getOrDefault(currentDistrict.id(), 0);
        int maxSlots = this.maxSlots.getOrDefault(currentDistrict.id(), 0);

        plusButton.setDisable(slots >= maxSlots);
        deleteButton.setDisable(slots <= 0);
    }

    private String pathHandler(String id) {
        return switch (id) {
            case "city" -> "districts/Market_square.png";
            case "energy" -> "districts/Energy_Production.png";
            case "mining" -> "districts/Military_sector.png";
            case "agriculture" -> "districts/Agricultural_sector.png";
            case "industry" -> "districts/Manufacturing_Hub.png";
            case "research_site" -> "districts/Scientific_sector.png";
            case "ancient_foundry" -> "districts/Metal_Foundry.png";
            case "ancient_factory" -> "districts/Medical_zone.png";
            case "ancient_refinery" -> "districts/Oil_refinery.png";
            default -> "";
        };
    }

    private void createPopUp() {
        popup = new Popup();
        popup.getContent().addAll(app.initAndRender(districtInformationComponent, Map.of("district", currentDistrict, "resourcePath", pathHandler(currentDistrict.id()))));
    }

    public void showDistrictInfo(MouseEvent event) {
        createPopUp();
        popup.show(this, event.getScreenX(), event.getScreenY());
    }

    public void hideDistrictInfo() {
        popup.hide();
    }


    public void plusButton() {
        subscriber.subscribe(jobService.createDistrictJob(
                fraction.get_id(),
                currentDistrict.id()
        ));
    }

    public void deleteButton() {
        String jobID = jobService.findJobIdByDistrictName(currentDistrict.id(), fraction);
        if (jobID != null) {
            subscriber.subscribe(jobService.deleteJob(jobID));
        } else {
            subscriber.subscribe(gameSystemsApiService.updateFraction(
                    fraction.getGame().getId(),
                    fraction.get_id(),
                    new UpdateFractionDto(
                            null,
                            Map.of(currentDistrict.id(), -1),
                            null,
                            null,
                            null,
                            null
                    )));
        }
    }


    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
