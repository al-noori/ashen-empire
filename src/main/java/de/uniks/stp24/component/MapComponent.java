package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Fraction;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.GameSystemsService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component
public class MapComponent extends Pane {
    @Inject
    Subscriber subscriber;
    @Inject
    GameSystemsService gameSystemsService;
    @Inject
    GameService gameService;
    @SubComponent
    @Inject
    Provider<FractionComponent> fractionComponentProvider;
    @Inject
    App app;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Provider<ConnectionComponent> connectionComponentProvider;

    private Game game;
    private final Group group = new Group();
    private final Translate translate = new Translate();
    private final Scale scale = new Scale(1, 1);
    private final Pane background = new Pane();
    private double startX;
    private double startY;
    private double offsetX = 0;
    private double offsetY = 0;
    private final double MIN_SCALE = 0.5;
    /**
     * Maximaler Skalierungsfaktor
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double MAX_SCALE = 5;
    // Selection Property for Division
    public ObjectProperty<Fleet> selectedFleetProperty = new SimpleObjectProperty<>();


    @Inject
    public MapComponent() {
    }

    @OnInit
    public void onInit() {
        this.setOnScroll(this::scroll);
        this.setOnMousePressed(this::startDrag);
        this.setOnMouseDragged(this::drag);
        game = gameService.getGame();
    }

    @OnRender
    public void onRender() {
        this.getChildren().add(background);
        background.setLayoutX(0);
        background.setLayoutY(0);
        background.getStyleClass().add("inGame");
        background.minWidthProperty().bind(this.widthProperty());
        background.minHeightProperty().bind(this.heightProperty());
        background.maxWidthProperty().bind(this.widthProperty());
        background.maxHeightProperty().bind(this.heightProperty());
        List<Fraction> fractions = game.getFractions();
        for (int i = 0; i < fractions.size(); i++) {
            for (int j = i + 1; j < fractions.size(); j++) {
                if (fractions.get(i).getLinks().contains(fractions.get(j))) {
                    ConnectionComponent connectionComponent = connectionComponentProvider.get();
                    connectionComponent.setFractions(fractions.get(i), fractions.get(j));
                    this.group.getChildren().add(connectionComponent);
                }

            }
        }
        fractions.forEach(fraction -> {
            FractionComponent countryComponent = app.initAndRender(fractionComponentProvider.get(),
                    Map.of(
                            "fraction", fraction,
                            "game", game,
                            "selectedFleetProperty", selectedFleetProperty));
            this.group.getChildren().add(countryComponent);
        });
        background.getTransforms().add(translate);
        background.getTransforms().add(scale);
        background.getChildren().add(group);
        group.layoutXProperty().bind(background.widthProperty().divide(2));
        group.layoutYProperty().bind(background.heightProperty().divide(2));
        group.scaleXProperty().bind(
                Bindings.createDoubleBinding(
                        this::calcGroupScale,
                        background.widthProperty(),
                        background.heightProperty()));
        group.scaleYProperty().bind(
                Bindings.createDoubleBinding(
                        this::calcGroupScale,
                        background.widthProperty(),
                        background.heightProperty()));
        this.setOnMouseClicked(click -> {
            if (click.getTarget() == background && click.getButton() == MouseButton.PRIMARY) {
                selectedFleetProperty.set(null);
            }
        });
    }

    private void scroll(ScrollEvent event) {
        double scalingFactor = scale.getX() + event.getDeltaY() / 200;
        scalingFactor = calcScale(scalingFactor);
        offsetX += (this.getWidth() * (scale.getX() - scalingFactor)) / 2;
        offsetY += (this.getHeight() * (scale.getY() - scalingFactor)) / 2;
        scale.setX(scalingFactor);
        scale.setY(scalingFactor);
        makeInBound();
        translate.setX(offsetX);
        translate.setY(offsetY);
        event.consume();
    }

    private double calcScale(double scalingFactor) {
        if (scalingFactor > MAX_SCALE / MIN_SCALE)
            return MAX_SCALE / MIN_SCALE;
        if (scalingFactor < 1)
            return 1;
        return scalingFactor;
    }

    private void startDrag(MouseEvent event) {
        // Speichern Sie die Startposition, wenn das Ziehen beginnt
        startX = event.getSceneX();
        startY = event.getSceneY();
        event.consume();
    }

    private void drag(MouseEvent event) {
        // Berechnen Sie die Differenz zwischen der Startposition und der aktuellen Position
        if (event.getButton() == MouseButton.SECONDARY) {
            offsetX += event.getSceneX() - startX;
            offsetY += event.getSceneY() - startY;
            startX = event.getSceneX();
            startY = event.getSceneY();
        }
        translate.setX(offsetX);
        translate.setY(offsetY);
        makeInBound();
        translate.setX(offsetX);
        translate.setY(offsetY);
        event.consume();
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }

    private void makeInBound() {
        Bounds boundsInGlobal = background.getBoundsInParent();
        if (boundsInGlobal.getMinY() > 0) {
            offsetY = 0;
        }
        if (boundsInGlobal.getMinX() > 0) {
            offsetX = 0;
        }
        if (boundsInGlobal.getMaxY() < this.getHeight()) {
            offsetY = this.getHeight() - boundsInGlobal.getHeight();
        }
        if (boundsInGlobal.getMaxX() < this.getWidth()) {
            offsetX = this.getWidth() - boundsInGlobal.getWidth();
        }
    }

    double calcGroupScale() {
        Bounds boundsInLocal = group.getBoundsInLocal();
        double coefficient = Math.min(background.getWidth() / boundsInLocal.getWidth(), background.getHeight() / boundsInLocal.getHeight());
        return MIN_SCALE * coefficient;
    }
}
