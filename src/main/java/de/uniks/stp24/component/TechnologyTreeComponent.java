package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.TechnologieNode;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TechnologiesServices;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;

@Component
public class TechnologyTreeComponent extends Pane {
    @SubComponent
    @Inject
    Provider<TechnologieCardComponent> technologieCardComponentProvider;
    @Inject
    TechnologiesServices technologiesServices;
    @Inject
    App app;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;

    private Group group = new Group();
    private final Translate translate = new Translate();
    private final Scale scale = new Scale(0.1, 0.1); // Set initial scale to 1

    private double startX;
    private double startY;
    private double offsetX = 0;
    private double offsetY = 0;
    final double MIN_SCALE = 0.5;
    final double MAX_SCALE = 30;

    @Inject
    TechnologyTreeComponent() {
    }

    @OnInit
    void init() {
        group.getTransforms().addAll(translate, scale);
        this.getChildren().add(group);
        setMouseActions();
        if(gameService.getOwnUser() != null && gameService.getOwnUser().getEmpire() != null){
            subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_TECHNOLOGIES, evt -> Platform.runLater(this::render));
        }
    }
    @OnRender
    void onRender() {
        render();
    }

    private void setMouseActions() {
        this.setOnScroll(this::scroll);
        this.setOnMousePressed(this::startDrag);
        this.setOnMouseDragged(this::drag);
    }
    private void clearMouseActions() {
        this.setOnScroll(null);
        this.setOnMousePressed(null);
        this.setOnMouseDragged(null);
    }

    public void render() {
        clearMouseActions();
        group.getTransforms().clear();
        group.getChildren().clear();
        this.getChildren().clear();
        group = new Group();
        technologiesServices.loadTree().subscribe(nodes -> {
            Platform.runLater(() -> {
                renderEdges(nodes);
                renderNodes(nodes, null);
            });
        });
        this.getChildren().add(group);
        group.getTransforms().addAll(translate, scale);
        setMouseActions();
    }

    private void renderEdges(List<TechnologieNode> nodes) {
        for (TechnologieNode node : nodes) {
            for (TechnologieNode parent : node.getParents()) {
                EdgeComponent edgeComponent = new EdgeComponent(parent, node);
                group.getChildren().add(edgeComponent);
            }
            renderEdges(node.getChildren());
        }
    }

    private void renderNodes(List<TechnologieNode> nodes, TechnologieNode parent) {
        for (TechnologieNode node : nodes) {
            TechnologieCardComponent technologieCardComponent = app.initAndRender(technologieCardComponentProvider.get()
                    , Map.of("technologieNode", node , "technologyTreeComponent", this));
            technologieCardComponent.setItem(node);
            technologieCardComponent.setLayoutX(node.getX());
            technologieCardComponent.setLayoutY(node.getY());
            group.getChildren().add(technologieCardComponent);
            renderNodes(node.getChildren(), node);
        }
    }

    private void scroll(ScrollEvent event) {
        double scalingFactor = scale.getX() + event.getDeltaY() / 200;
        scalingFactor = calcScale(scalingFactor);
        scale.setX(scalingFactor);
        scale.setY(scalingFactor);
        makeInBound();
        translate.setX(offsetX + this.getWidth() / 2);
        translate.setY(offsetY + this.getHeight() / 2);
        event.consume();
    }

    private double calcScale(double scalingFactor) {
        Bounds bounds = group.getBoundsInLocal();
        if (scalingFactor * bounds.getWidth() < this.getWidth() * MIN_SCALE
                && scalingFactor * bounds.getHeight() < this.getHeight() * MIN_SCALE) {
            scalingFactor = Math.min(
                    this.getWidth() * MIN_SCALE / bounds.getWidth(),
                    this.getHeight() * MIN_SCALE / bounds.getHeight()
            );
            return scalingFactor;
        }
        if (scalingFactor * bounds.getWidth() > this.getWidth() * MAX_SCALE
                || scalingFactor * bounds.getHeight() > this.getHeight() * MAX_SCALE) {
            scalingFactor = Math.min(
                    this.getWidth() * MAX_SCALE / bounds.getWidth(),
                    this.getHeight() * MAX_SCALE / bounds.getHeight()
            );
            return scalingFactor;
        }
        return scalingFactor;
    }

    private void startDrag(MouseEvent event) {
        startX = event.getSceneX();
        startY = event.getSceneY();
        event.consume();
    }

    private void drag(MouseEvent event) {

        if (event.getButton() == MouseButton.SECONDARY) {
            offsetX += event.getSceneX() - startX;
            offsetY += event.getSceneY() - startY;
            startX = event.getSceneX();
            startY = event.getSceneY();
        }
        translate.setX(offsetX + this.getWidth() / 2);
        translate.setY(offsetY + this.getHeight() / 2);
        makeInBound();
        event.consume();
    }

    private void makeInBound() {
        Bounds boundsInLocal = group.getBoundsInLocal();
        Bounds boundsInParent = group.getBoundsInParent();
        double scalingFactor;
        if (this.getHeight() / boundsInLocal.getHeight() < this.getWidth() / boundsInLocal.getWidth()) {
            scalingFactor = this.getHeight() * MIN_SCALE / boundsInLocal.getHeight();
        } else {
            scalingFactor = this.getWidth() * MIN_SCALE / boundsInLocal.getWidth();
        }
        double xMin = this.getWidth() / 2 - boundsInLocal.getWidth() * scalingFactor / 2;
        double yMin = this.getHeight() / 2 - boundsInLocal.getHeight() * scalingFactor / 2;
        double xMax = this.getWidth() / 2 + boundsInLocal.getWidth() * scalingFactor / 2;
        double yMax = this.getHeight() / 2 + boundsInLocal.getHeight() * scalingFactor / 2;
        if (boundsInParent.getMinX() > xMin) {
            offsetX += xMin - boundsInParent.getMinX();
        }
        if (boundsInParent.getMinY() > yMin) {
            offsetY += yMin - boundsInParent.getMinY();
        }
        if (boundsInParent.getMaxX() < xMax) {
            offsetX += xMax - boundsInParent.getMaxX();
        }
        if (boundsInParent.getMaxY() < yMax) {
            offsetY += yMax - boundsInParent.getMaxY();
        }
    }
   @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}