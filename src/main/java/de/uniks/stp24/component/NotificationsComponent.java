package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.model.game.War;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.ImageCache;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import java.util.*;

@Component(view = "NotificationsComponent.fxml")
public class NotificationsComponent extends AnchorPane {

    //---------------------------------------FXML Components---------------------------------------//
    @FXML
    ImageView imageView;
    @FXML
    Label message;
    //---------------------------------------DEPENDENCIES---------------------------------------//
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    ImageCache imageCache;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;
    //---------------------------------------NOTIFICATION PROPERTIES---------------------------------------//
    private boolean notifiedFewSurvivors = false;
    private boolean notifiedTooMuchSurvivors = false;
    private boolean notifiedFewTokens = false;
    private boolean notifiedTooMuchTokens = false;
    private boolean notifiedFewReactiveEnergy = false;
    private boolean notifiedTooMuchReactiveEnergy = false;
    private Timer notificationTimer;
    //---------------------------------------Lists to tack Notifications-----------------------------------//
    public List<War> defendingWars = new ArrayList<>();
    public List<Jobs> currentJobs = new ArrayList<>();
    public List<Fleet> fleets = new ArrayList<>();

    //---------------------------------------INIT and RENDER---------------------------------------//
    @Inject NotificationsComponent() {

    }
    @OnRender
    void onRender() {
        this.setVisible(false);
        if (gameService.getOwnUser() == null || gameService.getOwnUser().getEmpire() == null) {
            return;
        }
        initialHandle();
        addSubscribers();
    }
    private void initialHandle() {
        handleDefendingWarsChanges();
        handleJobChanges();
        handleFleetChanges();
    }
    //---------------------------------------Timer Management---------------------------------------//
    private void cancelTimer() {
        // Cancel any existing timer
        if (notificationTimer != null) {
            notificationTimer.cancel();
            notificationTimer.purge();
            notificationTimer = null;
        }
    }

    private void startTimer() {
        cancelTimer();
        notificationTimer = new Timer();

        // Schedule the task to dismiss the notification after 30 seconds
        notificationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Run on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> dismissNotification());
            }
        }, 30000); // 30000 milliseconds = 30 seconds
    }

    private void dismissNotification() {
        // Stop the timer
        cancelTimer();
        // Clear the notification
        message.setText("");
        imageView.setImage(null);
        this.setVisible(false);
    }
    // ---------------------------------------SUBSCRIBERS---------------------------------------//
    private void addSubscribers() {
        addResourcesSubscribers();
        addWarSubscribers();
        addJobSubscribers();
        addFleetSubscribers();
    }
    private void addResourcesSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_RESOURCES, evt -> Platform.runLater(this::handleResourcesChanges));
    }
    private void addWarSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_DEFENDING_WARS, evt -> Platform.runLater(this::handleDefendingWarsChanges));
    }
    private void addJobSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_JOBS, evt -> Platform.runLater(this::handleJobChanges));
    }
    private void addFleetSubscribers() {
        subscriber.listen(gameService.getOwnUser().getEmpire().listeners(), Empire.PROPERTY_FLEETS, evt -> Platform.runLater(this::handleFleetChanges));
    }
    // ---------------------------------------HANDLERS---------------------------------------//
    private void handleFleetChanges() {
        Empire empire = gameService.getOwnUser().getEmpire();
        empire.getFleets().forEach(fleet -> fleet.getShips().forEach(ship -> {
          if (ship.getHealth() < getMaximumHealth(ship.get_id())) {
              if(!fleets.contains(fleet)) {
                  fleets.add(fleet);
              }
          }
        }));
        fleets.forEach(fleet -> fleet.getShips().forEach(ship -> {
            if (allShipsHealed(fleet) && fleets.contains(fleet)) {
                notifyDivisionHealed(fleet);
                fleets.remove(fleet);
            }
        }));
    }
    private boolean allShipsHealed(Fleet fleet) {
        return fleet.getShips().stream().allMatch(ship -> Objects.equals(ship.getHealth(), getMaximumHealth(ship.get_id())));
    }

    private void handleJobChanges() {
        Empire empire = gameService.getOwnUser().getEmpire();
        empire.getJobs().forEach(job -> {
            if (Objects.equals(job.getType(), "ship")) {
                if (!currentJobs.contains(job)) {
                    currentJobs.add(job);
                }
            }
        });
        List<Jobs> cacheList = new ArrayList<>(currentJobs);
        currentJobs.forEach(job -> {
            if (!empire.getJobs().contains(job)) {
                notifyJobFinished();
                cacheList.remove(job);
            }
        });
        currentJobs = cacheList;
    }

    private void handleDefendingWarsChanges() {
        List<War> newDefendingWars= gameService.getOwnUser().getEmpire().getDefendingWars();
        newDefendingWars.forEach(war -> {
            if (!defendingWars.contains(war)) {
                notifyWarDeclaration(war.getAttacker());
                defendingWars.add(war);
            }
        });
        defendingWars.forEach(war -> {
            if (!newDefendingWars.contains(war)) {
                notifyWarEnding(war.getAttacker());
                defendingWars.remove(war);
            }
        });
    }

    private void handleResourcesChanges() {
        Empire empire = gameService.getOwnUser().getEmpire();
        handleFewSurvivors(empire);
        handleManySurvivors(empire);
        handleFewTokens(empire);
        handleManyTokens(empire);
        handleLowReactiveEnergy(empire);
        handleHighReactiveEnergy(empire);
    }

    private void handleHighReactiveEnergy(Empire empire) {
        Integer reactiveEnergyHighLimit = 100;
        if (empire.getResources().get("energy") >= reactiveEnergyHighLimit && !notifiedTooMuchReactiveEnergy) {
            notifiedTooMuchReactiveEnergy = true;
            notifyHighReactiveEnergy();
        } else if (empire.getResources().get("energy") < reactiveEnergyHighLimit && notifiedTooMuchReactiveEnergy) {
            notifiedTooMuchReactiveEnergy = false;
        }
    }

    private void handleLowReactiveEnergy(Empire empire) {
        Integer reactiveEnergyLowLimit = 10;
        if (empire.getResources().get("energy") <= reactiveEnergyLowLimit && !notifiedFewReactiveEnergy) {
            notifiedFewReactiveEnergy = true;
            notifyLowReactiveEnergy();
        } else if (empire.getResources().get("energy") > reactiveEnergyLowLimit && notifiedFewReactiveEnergy) {
            notifiedFewReactiveEnergy = false;
        }
    }

    private void handleManyTokens(Empire empire) {
        Integer tokensHighLimit = 100;
        if (empire.getResources().get("credits") >= tokensHighLimit && !notifiedTooMuchTokens) {
            notifiedTooMuchTokens = true;
            notifyManyTokens();
        } else if (empire.getResources().get("credits") < tokensHighLimit && notifiedTooMuchTokens) {
            notifiedTooMuchTokens = false;
        }
    }

    private void handleFewTokens(Empire empire) {
        Integer tokensLowLimit = 10;
        if (empire.getResources().get("credits") <= tokensLowLimit && !notifiedFewTokens) {
            notifiedFewTokens = true;
            notifyFewTokens();
        } else if (empire.getResources().get("credits") > tokensLowLimit && notifiedFewTokens) {
            notifiedFewTokens = false;
        }
    }

    private void handleManySurvivors(Empire empire) {
        Integer survivorsHighLimit = 100;
        if (empire.getResources().get("population") >= survivorsHighLimit && !notifiedTooMuchSurvivors) {
            notifiedTooMuchSurvivors = true;
            notifyManySurvivors();
        } else if (empire.getResources().get("population") < survivorsHighLimit && notifiedTooMuchSurvivors) {
            notifiedTooMuchSurvivors = false;
        }
    }

    private void handleFewSurvivors(Empire empire) {
        Integer survivorsLowLimit = 10;
        if (empire.getResources().get("population") <= survivorsLowLimit && !notifiedFewSurvivors) {
            notifiedFewSurvivors = true;
            notifyFewSurvivors();
        } else if (empire.getResources().get("population") > survivorsLowLimit && notifiedFewSurvivors) {
            notifiedFewSurvivors = false;
        }
    }
    // ---------------------------------------NOTIFICATION METHODS---------------------------------------//
    private void notifyWarDeclaration(Empire attacker) {
        cancelTimer();
        String notificationMessage = attacker.getName() + " " + resources.getString("notification.war.declaration");
        message.setText(notificationMessage);
        imageView.setImage(imageCache.get("images/notifications/war_icon_green.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyWarEnding(Empire attacker) {
        cancelTimer();
        String notificationMessage = attacker.getName() + " " + resources.getString("notification.war.end");
        message.setText(notificationMessage);
        imageView.setImage(imageCache.get("images/notifications/war_icon_green.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyFewSurvivors() {
        cancelTimer();
        message.setText(resources.getString("notification.few.survivors"));
        imageView.setImage(imageCache.get("images/notifications/survivors_icon.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyManySurvivors() {
        cancelTimer();
        message.setText(resources.getString("notification.many.survivors"));
        imageView.setImage(imageCache.get("images/notifications/survivors_icon.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyFewTokens() {
        cancelTimer();
        message.setText(resources.getString("notification.few.tokens"));
        imageView.setImage(imageCache.get("images/notifications/coins-crossed-out.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyManyTokens() {
        cancelTimer();
        message.setText(resources.getString("notification.many.tokens"));
        imageView.setImage(imageCache.get("images/notifications/coins.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyLowReactiveEnergy() {
        cancelTimer();
        message.setText(resources.getString("notification.few.energy"));
        imageView.setImage(imageCache.get("images/notifications/thunderbolt-crossed-out.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyHighReactiveEnergy() {
        cancelTimer();
        message.setText(resources.getString("notification.many.energy"));
        imageView.setImage(imageCache.get("images/notifications/thunderbolt.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyJobFinished() {
        cancelTimer();
        message.setText(resources.getString("notification.job.finished"));
        imageView.setImage(imageCache.get("images/notifications/tank_icon_green.png"));
        this.setVisible(true);
        startTimer();
    }

    private void notifyDivisionHealed(Fleet fleet) {
        cancelTimer();
        String message = resources.getString("notification.healed.division") + " " + fleet.getName() + " " + resources.getString("notification.healed.division2");
        this.message.setText(message);
        imageView.setImage(imageCache.get("images/notifications/love-always-wins.png"));
        this.setVisible(true);
        startTimer();
    }
    private Integer getMaximumHealth(String id) {
        return switch (id) {
            case "explorer", "colonizer", "interceptor", "fighter", "corvette" -> 100;
            case "bomber", "frigate" -> 150;
            case "destroyer" -> 250;
            case "cruiser", "vanguard", "sentinel" -> 300;
            case "battleship" -> 600;
            case "carrier" -> 800;
            case "dreadnought" -> 1000;
            default -> 0;
        };
    }
    //---------------------------------------DESTROY---------------------------------------//
    @OnDestroy
    void onDestroy() {
        cancelTimer();
        subscriber.dispose();
    }
}