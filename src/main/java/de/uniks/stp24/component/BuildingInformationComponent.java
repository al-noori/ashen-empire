package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.BuildingDto;
import de.uniks.stp24.service.BuildingService;
import de.uniks.stp24.service.ImageCache;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ComponentListCell;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

@Component(view = "BuildingInformation.fxml")
public class BuildingInformationComponent extends VBox {

    @Inject
    App app;
    @Param("building")
    BuildingDto building;
    @Param("neededResourceInfo")
    String resourceInfo;
    @Param("resourcePath")
    String resourcePath;
    @FXML
    ImageView buildingImage;
    @FXML
    Label buildingNameLabel;
    @FXML
    Label resourceLabel;
    @FXML
    ListView<String> resourceListView;
    @Inject
    BuildingService buildingService;
    @Inject
    Provider<BuildingResourceComponent> buildingResourceComponentProvider;
    @Inject
    public ImageCache imageCache;
    @Inject
    @Resource
    ResourceBundle bundle;

    @Inject
    public BuildingInformationComponent() {
    }

    @OnRender
    public void onRender() {
        buildingImage.setImage(imageCache.get(resourcePath));
        buildingNameLabel.setText(bundle.getString(building.id()));
        resourceLabel.setText(resourceInfo);
        switch (resourceInfo) {
            case "Cost" -> building.cost().forEach((key, value) -> resourceListView.getItems().add(key + "," + value));
            case "Production" ->
                    building.production().forEach((key, value) -> resourceListView.getItems().add(key + "," + value));
            case "Upkeep" ->
                    building.upkeep().forEach((key, value) -> resourceListView.getItems().add(key + "," + value));
        }
        resourceListView.setCellFactory(param -> {
            ListCell<String> stringListCell = new ComponentListCell<>(app, buildingResourceComponentProvider);
            stringListCell.prefWidthProperty().bind(resourceListView.widthProperty().subtract(50));
            resourceListView.prefHeightProperty().bind(Bindings.size(resourceListView.getItems()).multiply(50));
            return stringListCell;
        });

    }

    @OnDestroy
    public void onDestroy() {
        resourceListView.getItems().clear();
    }

}
