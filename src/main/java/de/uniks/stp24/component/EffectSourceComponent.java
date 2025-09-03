package de.uniks.stp24.component;

import de.uniks.stp24.dto.EffectSourceDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

@Component(view = "EffectSource.fxml")
public class EffectSourceComponent extends VBox {
    @FXML
    Label effectName;
    @FXML
    VBox effectsContainer;
    @Inject
    Provider<EffectComponent> effectComponentProvider;
    @Inject
    ResourceBundle resourceBundle;
    @Inject
    EffectSourceComponent() {
    }

    public void setItem(EffectSourceDto effectSourceDto){
        String effectName = effectSourceDto.id();
        try {
            if(effectName.startsWith("Event")) effectName = "custom";
            effectName = resourceBundle.getString(effectName);
        } catch (java.util.MissingResourceException e) {
            // If no effect name is found in the resource bundle, the effect name is used as is
            System.out.println("No effect name found for " + effectName + " in resource bundle!");
            effectName = resourceBundle.getString("unknown");
        }
        this.effectName.setText(effectName + ":");
        effectSourceDto.effects().forEach(effectDto -> {
            EffectComponent effectComponent = effectComponentProvider.get();
            effectComponent.setItem(effectDto);
            effectsContainer.getChildren().add(effectComponent);
        });
    }
}
