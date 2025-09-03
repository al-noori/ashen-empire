package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.model.game.*;
import de.uniks.stp24.service.FleetService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.util.ui.CustomTooltip;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.*;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.WHITE;

@Component
public class FractionComponent extends Pane {
    @Inject
    App app;
    @Inject
    ImageCache imageC;
    @Param("game")
    Game game;
    @Param("fraction")
    Fraction fraction;
    @Param("selectedFleetProperty")
    ObjectProperty<Fleet> selectedFleetProperty;
    @Inject
    @Resource
    ResourceBundle resources;
    ImageView imageView = new ImageView();
    StackPane imageHolder = new StackPane(imageView);
    HBox fleetHolder = new HBox();
    @Inject
    Subscriber subscriber;
    @Inject
    Subscriber fleetSubscriber;
    @Inject
    GameService gameService;
    @Inject
    FleetService fleetService;
    ImageView fightImage = new ImageView();
    ImageView fractionFightImage = new ImageView();
    Label fractionHpLabel = new Label();

    @Inject
    FractionComponent() {
        super();
    }

    @OnRender
    public void onRender() {
        this.setLayoutY(fraction.getY() - 2.5);
        this.setLayoutX(fraction.getX() - 2.5);
        if (fraction.getEmpire() != null) {
            imageHolder.setStyle("-fx-border-color: " + fraction.getEmpire().getColor() + ";");
        }

        subscriber.listen(fraction.listeners(), Fraction.PROPERTY_EMPIRE, evt -> imageHolder.setStyle("-fx-border-color: " + fraction.getEmpire().getColor() + ";"));

        fightImage.setFitHeight(5);
        fightImage.setFitWidth(10);
        fractionFightImage.setFitHeight(5);
        fractionFightImage.setFitWidth(10);
        fightImage.setImage(imageC.get("icons/fight_icon.png"));
        fractionFightImage.setImage(imageC.get("icons/fight_icon.png"));
        fightImage.setLayoutX(5);
        fightImage.setLayoutY(0);
        fractionFightImage.setLayoutX(-3);
        fractionFightImage.setLayoutY(-5);
        imageView.setImage(imageC.get("images/fractions/" + fraction.getType() + ".png"));
        imageView.setFitWidth(5);
        imageView.setFitHeight(5);
        fractionHpLabel.setLayoutY(-1);
        fractionHpLabel.setText("HP: " + fraction.getHealth());
        fractionHpLabel.setStyle("-fx-font-size: 1px;" +
                "-fx-text-fill: #E2BD2B;");
        this.getChildren().clear();
        this.getChildren().add(imageHolder);
        this.getChildren().add(fleetHolder);
        fleetHolder.setLayoutX(5);
        fleetHolder.setLayoutY(5);
        this.setPickOnBounds(false);
        fleetHolder.setPickOnBounds(false);
        handleFleets(fraction.getFleets());
        subscriber.listen(fraction.listeners(), Fraction.PROPERTY_FLEETS, evt -> Platform.runLater(() -> handleFleets(fraction.getFleets())));
        imageHolder.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (selectedFleetProperty.isNull().get()) {
                    app.show("/inGame/FractionDetails", Map.of("game", game, "fraction", fraction));
                } else {
                    subscriber.subscribe(fleetService.moveFleet(selectedFleetProperty.get(), fraction));
                    selectedFleetProperty.set(null);
                }
            }
        });
        makeTooltip();
        subscriber.listen(fraction.listeners(), Fraction.PROPERTY_EMPIRE, evt -> Platform.runLater(this::makeTooltip));
        subscriber.listen(fraction.listeners(), Fraction.PROPERTY_HEALTH, evt -> Platform.runLater(() -> fractionHpLabel.setText("HP: " + fraction.getHealth())));
        this.getChildren().add(fractionHpLabel);
    }

    private void makeTooltip() {
        String fractionName = fraction.getName() != null ? fraction.getName() : "Noname";
        Label label;
        Empire ownEmpire = gameService.getOwnUser().getEmpire();
        if (fraction.getEmpire() == null) {
            label = new Label(fractionName);
            label.setTextFill(WHITE);
        } else if (fraction.getEmpire() == ownEmpire) {
            label = new Label(fractionName);
            label.setTextFill(Color.valueOf("#E2BD2B"));
        } else {
            label = new Label(fractionName);
            label.setTextFill(WHITE);
        }
        label.getStyleClass().add("tooltip");
        Tooltip.install(imageHolder, new CustomTooltip(label));
    }

    private void handleFleets(List<Fleet> fleets) {
        this.getChildren().remove(fightImage);
        fleetSubscriber.dispose();
        fleets.forEach(fleet -> fleetSubscriber.listen(fleet.listeners(), Fleet.PROPERTY_SHIPS, evt -> Platform.runLater(() -> handleFleets(fleets))));
        fleets.forEach(fleet -> {
            if (fleet.getEmpire() != null) {
                fleetSubscriber.listen(fleet.getEmpire().listeners(), Empire.PROPERTY_ATTACKING_WARS, evt -> Platform.runLater(() -> handleFleets(fleets)));
            }
        });
        fleets.forEach(fleet -> {
            if (fleet.getEmpire() != null) {
                fleetSubscriber.listen(fleet.getEmpire().listeners(), Empire.PROPERTY_DEFENDING_WARS, evt -> Platform.runLater(() -> handleFleets(fleets)));
            }
        });
        fleets.forEach(fleet -> {
            fleet.getShips().forEach(ship -> {
                fleetSubscriber.listen(ship.listeners(), Ship.PROPERTY_HEALTH, evt -> Platform.runLater(() -> handleFleets(fleets)));
            });
        });
        fleetHolder.getChildren().clear();
        Set<Empire> inWarEmpires = new HashSet<>();
        Empire ownEmpire = gameService.getOwnUser().getEmpire();
        if (ownEmpire != null) {
            for (War war : ownEmpire.getAttackingWars()) {
                inWarEmpires.add(war.getDefender());
            }
            for (War war : ownEmpire.getDefendingWars()) {
                inWarEmpires.add(war.getAttacker());
            }
        }
        Set<Fleet> ownFleets = new HashSet<>();
        fraction.getFleets().forEach(fleet -> {
            if (ownEmpire != null && fleet.getEmpire() == ownEmpire) {
                ownFleets.add(fleet);
            }
        });
        Set<Fleet> enemyFleets = new HashSet<>();
        fraction.getFleets().forEach(fleet -> {
            if (inWarEmpires.contains(fleet.getEmpire()) || fleet.getEmpire() == null) {
                enemyFleets.add(fleet);
            }
        });
        Set<Fleet> neutralFleets = new HashSet<>();
        fraction.getFleets().forEach(fleet -> {
            if (fleet.getEmpire() != null && !enemyFleets.contains(fleet) && !ownFleets.contains(fleet)) {
                neutralFleets.add(fleet);
            }
        });
        ownFleets.forEach(fleet -> {
            if (fleet.getShips().isEmpty()) return;
            VBox container = new VBox();
            ImageView divImage = new ImageView();
            divImage.setFitHeight(5);
            divImage.setFitWidth(5);
            Tooltip tooltip;
            divImage.setImage(imageC.get("icons/owndivision.png"));
            Label label = new Label(fleet.getName());
            label.setTextFill(Color.valueOf("#E2BD2B"));
            label.getStyleClass().add("tooltip");
            tooltip = new CustomTooltip(label);
            divImage.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    selectedFleetProperty.set(fleet);
                }
            });
            subscriber.listen(selectedFleetProperty, (observableValue, oldVal, newVal) -> {
                if (fleet == newVal) {
                    divImage.setImage(imageC.get("icons/owndivisionchosen.png"));
                } else {
                    divImage.setImage(imageC.get("icons/owndivision.png"));
                }
            });
            int hp = fleet.getShips().stream().mapToInt(Ship::getHealth).sum();
            Label hpLabel = new Label("HP: " + hp);
            hpLabel.setStyle("-fx-font-size: 1px;" +
                    "-fx-text-fill: #E2BD2B;");
            Tooltip.install(divImage, tooltip);
            container.setAlignment(Pos.TOP_RIGHT);
            container.getChildren().add(hpLabel);
            container.getChildren().add(divImage);
            fleetHolder.getChildren().add(container);
        });
        boolean fighting = !enemyFleets.isEmpty() && !ownFleets.isEmpty();
        if (!fighting) {
            for (Fleet fleet: fraction.getFleets()){
                if (fleet.getShips().isEmpty()) continue;
                for (Fleet otherFleet: fraction.getFleets()) {
                    if (otherFleet.getShips().isEmpty()) continue;
                    Empire fleetEmpire = fleet.getEmpire();
                    Empire otherFleetEmpire = otherFleet.getEmpire();
                    if (fleetEmpire == null || otherFleetEmpire == null) break;
                    if (
                        fleetEmpire.getAttackingWars()
                            .stream()
                            .anyMatch(war -> otherFleetEmpire.getDefendingWars().contains(war))
                    ) {
                        fighting = true;
                        break;
                    } else if (
                            otherFleetEmpire.getAttackingWars()
                                    .stream()
                                    .anyMatch(war -> fleetEmpire.getDefendingWars().contains(war))
                    ) {
                        fighting = true;
                        break;
                    }
                }
            }
        }
        final boolean finalFighting = fighting;
        enemyFleets.forEach(fleet -> {
            if (fleet.getShips().isEmpty()) return;
            VBox container = new VBox();
            ImageView divImage = new ImageView();
            divImage.setFitHeight(5);
            divImage.setFitWidth(5);
            Tooltip tooltip;
            divImage.setImage(imageC.get("icons/enemydivision.png"));
            if (finalFighting) divImage.setScaleX(-1);
            Label label;
            if (fleet.getEmpire() != null) label = new Label(fleet.getEmpire().getName() + " - " + fleet.getName());
            else label = new Label(fleet.getName());
            label.setTextFill(RED);
            label.getStyleClass().add("tooltip");
            tooltip = new CustomTooltip(label);
            Tooltip.install(divImage, tooltip);
            int hp = fleet.getShips().stream().mapToInt(Ship::getHealth).sum();
            Label hpLabel = new Label("HP: " + hp);
            hpLabel.setStyle("-fx-font-size: 1px;" +
                    "-fx-text-fill: red;");
            Tooltip.install(divImage, tooltip);
            container.setAlignment(Pos.TOP_RIGHT);
            container.getChildren().add(hpLabel);
            container.getChildren().add(divImage);
            fleetHolder.getChildren().add(container);
        });
        neutralFleets.forEach(fleet -> {
            if (fleet.getShips().isEmpty()) return;
            VBox container = new VBox();
            ImageView divImage = new ImageView();
            divImage.setFitHeight(5);
            divImage.setFitWidth(5);
            Tooltip tooltip;
            divImage.setImage(imageC.get("icons/neutraldivision.png"));
            Label label;
            if (fleet.getEmpire() != null) label = new Label(fleet.getEmpire().getName() + " - " + fleet.getName());
            else label = new Label(fleet.getName());
            label.setTextFill(WHITE);
            label.getStyleClass().add("tooltip");
            tooltip = new CustomTooltip(label);
            Tooltip.install(divImage, tooltip);
            int hp = fleet.getShips().stream().mapToInt(Ship::getHealth).sum();
            Label hpLabel = new Label("HP: " + hp);
            hpLabel.setStyle("-fx-font-size: 1px;" +
                    "-fx-text-fill: white;");
            Tooltip.install(divImage, tooltip);
            container.setAlignment(Pos.TOP_RIGHT);
            container.getChildren().add(hpLabel);
            container.getChildren().add(divImage);
            fleetHolder.getChildren().add(container);
        });
        if (fighting) {
            this.getChildren().add(fightImage);
        }
        boolean fractionFighting = false;
        for (Fleet fleet: fleets) {
            if (fleet.getShips().isEmpty()) continue;
            Empire fractionEmpire = fraction.getEmpire();
            Empire fleetEmpire = fleet.getEmpire();
            if (fractionEmpire == null || fleetEmpire == null) break;
            if (
                    fractionEmpire
                            .getAttackingWars()
                            .stream()
                            .anyMatch(war -> fleetEmpire.getDefendingWars().contains(war))
            ) {
                fractionFighting = true;
                break;
            } else if (
                    fleetEmpire
                            .getAttackingWars()
                            .stream()
                            .anyMatch(war -> fractionEmpire.getDefendingWars().contains(war))
            ) {
                fractionFighting = true;
                break;
            }
        }
        this.getChildren().remove(fractionFightImage);
        if (fractionFighting) {
            this.getChildren().add(fractionFightImage);
        }

    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
