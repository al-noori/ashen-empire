package de.uniks.stp24.component;

import de.uniks.stp24.dto.EffectDto;
import de.uniks.stp24.dto.TraitsDto;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.TraitsService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;

import javax.inject.Inject;
import java.util.Objects;
import java.util.ResourceBundle;

@Component(view = "TraitCardComponent.fxml")
public class TraitCardComponent extends VBox {
    @FXML
    Button selectButton;
    @FXML
    Label traitEffectsLabel;
    @FXML
    Label traitConflictsLabel;
    @FXML
    Label traitPointsLabel;
    @FXML
    Label traitNameLabel;
    @FXML
    VBox traitEffectsBox;

    @Inject
    TraitsService traitsService;

    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    ImageCache imageCache;

    public StackPane stackpaneTraits;
    public TraitsDto currentTrait;
    public TraitTinyCardComponent traitTinyCardComponent;

    @Inject
    public TraitCardComponent() {
    }

    @OnInit
    public void onInit() {
        if (currentTrait != null) {
            traitsService.counterSelectedTraits.addListener((observable, oldValue, newValue)
                    -> Platform.runLater(() ->
                    selectButton.setDisable((newValue >= 5) || ((traitsService.getTraitPoints() - currentTrait.cost()) < 0))
            ));
            traitsService.cost.addListener((observable, oldValue, newValue)
                    -> Platform.runLater(() ->
                    selectButton.setDisable((traitsService.getSelectedTraits() >= 5) || ((newValue - currentTrait.cost()) < 0))
            ));
        }
    }

    @OnRender
    public void onRender() {
        if (currentTrait != null) {
            traitNameLabel.setText(traitsService.getTraitName(currentTrait.id()));
            traitPointsLabel.setText(resources.getString("cost") + ": " + currentTrait.cost() + " " + resources.getString("trait.point"));
            traitConflictsLabel.setText(resources.getString("conflicts") + ": " + "\n" + conflictListToString(currentTrait.conflicts()));
            traitEffectsLabel.setText(resources.getString("effects") + ": ");
            for (EffectDto effect : currentTrait.effects()) {
                String imagePath = pathHandlerResources(effect.variable());
                Image image = imageCache.get(imagePath);
                ImageView effectIcon = new ImageView(image);
                effectIcon.setFitHeight(20);
                effectIcon.setFitWidth(20);
                Label effectLabel = new Label(effectToString(effect), effectIcon);
                effectLabel.getStyleClass().add("traitsCardText");
                traitEffectsBox.getChildren().add(effectLabel);
            }
        }
    }

    public void selectTraitCard() {
        stackpaneTraits.getChildren().remove(this);
        if (traitTinyCardComponent != null) {
            traitTinyCardComponent.highlightCard();
            traitsService.addTrait(currentTrait.id());
        }
    }

    public void clickOnCard() {
        stackpaneTraits.getChildren().remove(this);
    }

    public String effectToString(EffectDto effect) {
        StringBuilder effectString = new StringBuilder();
        if (effect.variable().startsWith("buildings.")) {
            effectString.append(traitsService.getEffectName(effect.variable())).append(": ").append(traitsService.getEffectMultiplier(effect.multiplier())).append("\n");
        } else {
            effectString.append(traitsService.getEffectName(effect.variable())).append(": ").append(effect.bonus()).append("\n");
        }
        return effectString.toString();
    }

    public String conflictListToString(String[] conflicts) {
        StringBuilder conflictString = new StringBuilder();
        for (String conflict : conflicts) {
            String conflictName = traitsService.getTraitName(conflict);
            conflictString.append(conflictName != null ? conflictName : conflict).append("\n");
        }
        return conflictString.toString();
    }

    public String pathHandlerResources(String id) {
        String[] iconIdentifier = id.split("\\.");
        String iconId;
        if (Objects.equals(iconIdentifier[0], "buildings"))  {
            iconId = iconIdentifier[3];
        }  else if (Objects.equals(iconIdentifier[0], "resources")) {
            iconId = iconIdentifier[1];
        } else {
            iconId = "";
        }
        return "icons/resources/" + iconId + ".png";
    }

}
