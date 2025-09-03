package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.VariableDto;
import de.uniks.stp24.service.ImageCache;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

@Component(view = "ResourceVariable.fxml")
public class ResourceVariableComponent extends VBox {
    @FXML
    Label typeLabel;
    @FXML
    ImageView resourceImage;
    @FXML
    Label initialValueLabel;
    @FXML
    VBox effectsBox;
    @FXML
    Label finalValueLabel;
    @Inject
    ImageCache imageCache;
    @Inject
    ResourceBundle resourceBundle;
    @Inject
    Provider<EffectSourceComponent> effectSourceComponentProvider;
    @Inject
    App app;
    @Inject
    public ResourceVariableComponent() {
    }

    public void setItem(String type, VariableDto variableDto) {
        typeLabel.setText(resourceBundle.getString(type));
        String resource = variableDto.variable().substring(variableDto.variable().lastIndexOf(".") + 1);
        Image image = imageCache.get("icons/resources/" + resource + ".png");
        resourceImage.setImage(image);
        initialValueLabel.setText(String.valueOf(Math.round(variableDto.initial())));
        finalValueLabel.setText(String.valueOf(Math.round(variableDto.finalValue())));
        variableDto.sources().forEach(effectSourceDto -> {
            EffectSourceComponent effectSourceComponent = app.initAndRender(effectSourceComponentProvider.get());
            effectSourceComponent.setItem(effectSourceDto);
            effectsBox.getChildren().add(effectSourceComponent);
        });
    }
}
