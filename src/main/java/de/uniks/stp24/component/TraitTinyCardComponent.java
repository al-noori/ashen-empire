package de.uniks.stp24.component;

import de.uniks.stp24.dto.TraitsDto;
import de.uniks.stp24.service.TraitsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "TraitTinyCardComponent.fxml")
public class TraitTinyCardComponent extends VBox implements ReusableItemComponent<TraitsDto>  {

    @FXML
    Label traitPointsLabel;
    @FXML
    Text traitNameLabel;

    @Inject
    TraitsService traitsService;

    @Inject
    @Resource
    ResourceBundle resource;

    @SubComponent
    @Inject
    TraitCardComponent traitCardComponent;

    public StackPane stackpaneTraits;

    @Param("trait")
    TraitsDto currentTrait;

    @Inject
    public TraitTinyCardComponent() {
    }

    @OnInit
    public void onInit() {
        traitCardComponent.currentTrait = currentTrait;
        traitCardComponent.traitTinyCardComponent = this;
    }

    @OnRender
    public void onRender() {
        if (currentTrait != null) {
            traitNameLabel.setText(traitsService.getTraitName(currentTrait.id()));
            traitPointsLabel.setText(currentTrait.cost() + " TP");
        } else {
            System.out.println("currentTrait is null in onRender");
        }
    }

    @Override
    public void setItem(@NotNull TraitsDto trait) {
        traitNameLabel.setText(traitsService.getTraitName(trait.id()));
        currentTrait = trait;
    }

    public void clickOnTinyCard() {
        traitCardComponent.currentTrait = currentTrait;
        traitCardComponent.stackpaneTraits = stackpaneTraits;
        traitCardComponent.traitTinyCardComponent = this;
        if (getStyleClass().contains("selectedTraitCard")) {
            deselectTraits();
            traitsService.removeTrait(currentTrait.id());
        } else if (getStyleClass().contains("conflictTraitCard")) {
            // Click not allowed, conflict with another trait
        } else {
            stackpaneTraits.getChildren().add(traitCardComponent);
        }
    }

    public void highlightCard() {
        this.getStyleClass().add("selectedTraitCard");
    }

    public void deselectTraits() {
        this.getStyleClass().remove("selectedTraitCard");
    }

}
