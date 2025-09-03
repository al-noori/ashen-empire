package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.TraitsDto;
import de.uniks.stp24.service.TraitsService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

@Component(view = "Traits.fxml")
public class TraitsComponent extends AnchorPane {

    public StackPane stackpane;

    @FXML
    public GridPane gridPaneTinyCards;

    @FXML
    Button saveTraitsButton;
    @FXML
    Text traitPointsText;
    @FXML
    Text selectedTraitsText;

    @Inject
    @Resource
    ResourceBundle resource;

    @Inject
    TraitsService traitsService;
    @Inject
    App app;

    @Inject
    Provider<TraitTinyCardComponent> traitTinyCardComponentProvider;

    Collection<TraitsDto> traits;


    @Inject
    public TraitsComponent() {
    }

    @OnInit
    public void onInit() {
        traits = traitsService.loadTraits().blockingFirst();
        traitsService.selectedTraitIds.addListener((observable, oldValue, newValue)
                -> Platform.runLater(() -> {
                    setText();
                    updateConflictCards(newValue);
                }));
        traitsService.cost.addListener((observable, oldValue, newValue)
                -> Platform.runLater(() -> saveTraitsButton.setDisable(newValue < 0)));
    }

    public void initialize() {
        if (gridPaneTinyCards == null) {
            System.out.println("tinyCards is null");
        } else {
            loadTraitTinyCards();
        }
    }

    private void loadTraitTinyCards() {
        int row = 0;
        int column = 0;
        final int maxColumns = 8;
        for (TraitsDto trait : traits) {
            TraitTinyCardComponent traitTinyCardComponent = app.initAndRender(traitTinyCardComponentProvider.get(), Map.of("trait", trait));

            traitTinyCardComponent.stackpaneTraits = stackpane;
            gridPaneTinyCards.add(traitTinyCardComponent, column, row);

            column++;
            if (column == maxColumns) {
                column = 0;
                row++;
            }
        }
    }


    @OnRender
    public void setText() {
        traitPointsText.setText(resource.getString("trait.points") + ": " + traitsService.getTraitPoints());
        selectedTraitsText.setText(resource.getString("selected.traits") + ": " + traitsService.getSelectedTraits());

    }

    public void updateConflictCards(List<String> traitIds) {
        List<TraitTinyCardComponent> conflictTraitTinyCardComponents = new ArrayList<>();
        List<TraitTinyCardComponent> traitTinyCardComponents = new ArrayList<>();
        for (Node node : gridPaneTinyCards.getChildren()) {
            if (node instanceof TraitTinyCardComponent traitTinyCardComponent) {
                traitTinyCardComponents.add(traitTinyCardComponent);
            }
        }
        List<TraitTinyCardComponent> nonConflictTraitTinyCardComponents = new ArrayList<>(traitTinyCardComponents);
        for (String traitId: traitIds){
            for (String conflictTrait : traitsService.getTrait(traitId).conflicts()) {
                for (TraitTinyCardComponent traitTinyCardComponent : traitTinyCardComponents) {
                    if (conflictTrait.equals(traitTinyCardComponent.currentTrait.id())) {
                        if (!conflictTraitTinyCardComponents.contains(traitTinyCardComponent)) {
                            conflictTraitTinyCardComponents.add(traitTinyCardComponent);
                            nonConflictTraitTinyCardComponents.remove(traitTinyCardComponent);
                        }
                    }
                }
            }
        }
        for (TraitTinyCardComponent traitTinyCardComponent : conflictTraitTinyCardComponents) {
            if (!traitTinyCardComponent.getStyleClass().contains("conflictTraitCard")) {
                traitTinyCardComponent.getStyleClass().add("conflictTraitCard");
            }
        }
        for (TraitTinyCardComponent traitTinyCardComponent : nonConflictTraitTinyCardComponents) {
            traitTinyCardComponent.getStyleClass().remove("conflictTraitCard");
        }
    }

    public void save() {
        stackpane.getChildren().remove(this);
    }

}
