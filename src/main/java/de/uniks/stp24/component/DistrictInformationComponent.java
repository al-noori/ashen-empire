package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.DistrictDto;
import de.uniks.stp24.service.DistrictService;
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

@Component(view= "DistrictInformation.fxml")
public class DistrictInformationComponent extends VBox {
    @Inject
    App app;

    @Param("district")
    DistrictDto district;
    @Param("resourcePath")
    String resourcePath;
    @FXML
    public ImageView districtImage;
    @FXML
    public Label districtNameLabel;
    @FXML
    public Label costLabel;
    @FXML
    public ListView<String> resourceCostListView;
    @FXML
    public Label productionLabel;
    @FXML
    public ListView<String> resourceProductionListView;
    @FXML
    public Label upkeepLabel;
    @FXML
    public ListView<String> resourceUpkeepListView;
    @Inject
    DistrictService districtService;
    @Inject
    Provider<BuildingResourceComponent> districtResourceComponentProvider;
    @Inject
    public ImageCache imageCache;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public DistrictInformationComponent(){

    }

    @OnRender
    public void onRender() {
        districtImage.setImage(imageCache.get(resourcePath));
        districtNameLabel.setText(districtService.getDistrictName(district.id()));
        district.cost().forEach((key, value) -> resourceCostListView.getItems().add(key + "," + value));
        district.production().forEach((key, value) -> resourceProductionListView.getItems().add(key + "," + value));
        district.upkeep().forEach((key, value) -> resourceUpkeepListView.getItems().add(key + "," + value));
        resourceCostListView.setCellFactory(param -> {
            ListCell<String> stringListCell = new ComponentListCell<>(app, districtResourceComponentProvider);
            stringListCell.prefWidthProperty().bind(resourceCostListView.widthProperty().subtract(50));
            resourceCostListView.prefHeightProperty().bind(Bindings.size(resourceCostListView.getItems()).multiply(50));
            return stringListCell;
        });

        resourceProductionListView.setCellFactory(param -> {
            ListCell<String> stringListCell = new ComponentListCell<>(app, districtResourceComponentProvider);
            stringListCell.prefWidthProperty().bind(resourceProductionListView.widthProperty().subtract(50));
            resourceProductionListView.prefHeightProperty().bind(Bindings.size(resourceProductionListView.getItems()).multiply(50));
            return stringListCell;
        });

        resourceUpkeepListView.setCellFactory(param -> {
            ListCell<String> stringListCell = new ComponentListCell<>(app, districtResourceComponentProvider);
            stringListCell.prefWidthProperty().bind(resourceUpkeepListView.widthProperty().subtract(50));
            resourceUpkeepListView.prefHeightProperty().bind(Bindings.size(resourceUpkeepListView.getItems()).multiply(50));
            return stringListCell;
        });

    }

    @OnDestroy
    public void onDestroy() {
        resourceCostListView.getItems().clear();
        resourceProductionListView.getItems().clear();
        resourceUpkeepListView.getItems().clear();
    }
}
