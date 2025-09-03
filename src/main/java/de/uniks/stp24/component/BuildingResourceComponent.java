package de.uniks.stp24.component;

import de.uniks.stp24.service.ImageCache;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "BuildingResource.fxml")
public class BuildingResourceComponent extends HBox implements ReusableItemComponent<String> {
    @FXML
    ImageView resourceImage;
    @FXML
    Label resourceNumber;
    @Inject
    public ImageCache imageCache;
    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public BuildingResourceComponent() {
    }

    @Override
    public void setItem(@NotNull String item) {
        String[] resource = item.split(",");
        resourceNumber.setText(resource[1]);
        resourceImage.setImage(imageCache.get(pathHandler(resource[0])));
        }

    private String pathHandler(String p) {
        return "icons/resources/" + p + ".png";
    }

    @OnDestroy
    public void onDestroy() {
        resourceImage.setImage(null);
    }
}
