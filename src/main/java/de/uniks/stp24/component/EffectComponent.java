package de.uniks.stp24.component;

import de.uniks.stp24.dto.EffectDto;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component
public class EffectComponent extends VBox {
    @Inject
    ResourceBundle resourceBundle;
    @Inject
    public EffectComponent() {
        super();
    }
    public void setItem(EffectDto effectDto){
        if(effectDto.base() != null) {
            this.getChildren().add(createComponent(
                    resourceBundle.getString("base") +
                    ": " + effectDto.base()));
        }
        if(effectDto.multiplier() != null) {
            if (effectDto.multiplier() >= 1) {
                this.getChildren().add(createComponent(
                        resourceBundle.getString("multiplier") +
                        ": +" + Math.round((effectDto.multiplier() - 1)*100) + "%"));
            } else {
                this.getChildren().add(createComponent(
                        resourceBundle.getString("multiplier") +
                        ": " + Math.round((effectDto.multiplier() - 1)*100) + "%"));
            }

        }
        if(effectDto.bonus() != null) {
            this.getChildren().add(createComponent(
                    resourceBundle.getString("bonus") + ": " +
                    effectDto.bonus()));
        }
    }
    private Label createComponent(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("yellow-text-small");
        return label;
    }
}
