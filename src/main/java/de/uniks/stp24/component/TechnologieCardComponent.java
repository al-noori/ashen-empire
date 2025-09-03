package de.uniks.stp24.component;

import de.uniks.stp24.dto.EffectDto;
import de.uniks.stp24.model.TechnologieNode;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.JobService;
import de.uniks.stp24.service.TechnologiesServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

@Component(view = "TechnologieCardComponent.fxml")
public class TechnologieCardComponent extends StackPane implements ReusableItemComponent<TechnologieNode> {
    //--------------------FXML--------------------//
    @FXML
    Button unlock_Cacnel_button;
    @FXML
    VBox blackout;
    @Inject
    ImageCache imageCache;
    @FXML
    VBox effectsHolder;
    @FXML
    Label costLabel;
    @FXML
    ImageView imageView;
    @FXML
    Label nameLabel;

    //---------------PARAMS-------------------//
    @Param("technologieNode")
    TechnologieNode technologieNode;
    @Param("technologyTreeComponent")
    TechnologyTreeComponent technologyTreeComponent;

    //---------------DEPENDENCIES-------------------//
    @Inject
    TechnologiesServices technologiesServices;
    @Inject
    JobService jobService;
    @Inject
    GameService gameService;
    @Inject
    Subscriber subscriber;
    @Inject
    ResourceBundle resourceBundle;
    @Inject
    Provider<TechnologyEffectComponent> effectComponentProvider;

    @OnInit
    void onInit() {
    }
    @Inject
    TechnologieCardComponent() {
    }
    @OnRender
    void render() {
        if(gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null) {
            return;
        }
        renderButtons();
        nameLabel.setText(technologieNode.getName());
        costLabel.setText(resourceBundle.getString("cost") + ": "+ technologieNode.getTechnology().cost() * 100);
        renderEffects();

        // render overlay

        renderImage();
    }

    private void renderButtons() {
            if(technologiesServices.isUnlocked(technologieNode)) {
                unlock_Cacnel_button.setVisible(false);
                setupUnlockButton();
                updateButton();
                addSubscriber();

            } else {
                unlock_Cacnel_button.setVisible(true);
                blackout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                if(inProcess()) {
                    setupCancelButton();
                } else {
                    setupUnlockButton();
                }

            }

    }

    private void renderImage() {
        imageView.setImage(imageCache.get("images/technologies/" + technologieNode.getName() + ".jpg"));
        Circle circle = new Circle(67.5, 67.5 , 67.5);
        imageView.setClip(circle);
    }

    void renderEffects() {
        for(EffectDto effect : technologieNode.getTechnology().effects()) {
            VBox holder = new VBox();
            holder.setAlignment(Pos.CENTER);
            Label effectName = new Label();
            try {
                effectName.setText(resourceBundle.getString(effect.variable()));
            } catch (java.util.MissingResourceException e) {
                System.out.println("No effect name found for " + effect.variable() + " in resource bundle!");
                effectName.setText(effect.variable());
            }
            TechnologyEffectComponent effectComponent = effectComponentProvider.get();
            effectComponent.setItem(effect);
            holder.getChildren().add(effectName);
            holder.getChildren().add(effectComponent);
            effectsHolder.getChildren().add(holder);
        }
    }
    void addSubscriber() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_RESOURCES, evt -> Platform.runLater(this::updateButton));
    }

    private boolean hasEnoughResources() {
        Integer resource = gameService.getOwnUser().getEmpire().getResources().get("research");
        return resource >= technologieNode.getTechnology().cost() * 100;
    }
    private void updateButton() {
        if(!technologiesServices.areAllRequirementsUnlocked(technologieNode) || !hasEnoughResources()) {
            unlock_Cacnel_button.setDisable(true);

        }
    }

    @Override
    public void setItem(@NotNull TechnologieNode technologieNode) {
        this.technologieNode = technologieNode;
    }

    private void setupCancelButton() {
        unlock_Cacnel_button.setText("Cancel");
        unlock_Cacnel_button.setOnAction(event -> onCancelButtonClicked());
        unlock_Cacnel_button.getStyleClass().clear();
        unlock_Cacnel_button.getStyleClass().add("cancel-technology-button");
    }
    private void setupUnlockButton() {
        unlock_Cacnel_button.setText("Unlock");
        unlock_Cacnel_button.setOnAction(event -> onUnlockButtonClicked());
        unlock_Cacnel_button.getStyleClass().clear();
        unlock_Cacnel_button.getStyleClass().add("unlock-technology-button");
    }


    public void onUnlockButtonClicked() {
        subscriber.dispose();
        unlock_Cacnel_button.setDisable(false);
        jobService.createTechnologyJob(null, technologieNode.getTechnology().id()).subscribe();
        setupCancelButton();
    }

    public void onCancelButtonClicked() {
        jobService.deleteJob(getJobId()).subscribe();
        addSubscriber();
        setupUnlockButton();
    }

    private boolean inProcess() {
        for(Jobs job : gameService.getJobs()) {
            if(job.getTechnology() != null && job.getTechnology().equals(technologieNode.getTechnology().id())) {
                return true;
            }
        }
        return false;
    }
    private String getJobId() {
        return gameService.getJobs().stream().filter(job -> job.getTechnology().equals(technologieNode.getTechnology().id())).findFirst().get().get_id();
    }
    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }

}
