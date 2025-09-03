package de.uniks.stp24.component;

import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.rest.UserApiService;
import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.TokenStorage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.fulib.fx.controller.Subscriber;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Objects;
import java.util.ResourceBundle;

@Component(view = "UserList.fxml")
public class UserListComponent extends HBox implements ReusableItemComponent<User> {
    @FXML
    ComboBox<String> comboBox;
    @FXML
    Text userText;
    @Param("game")
    Game game;

    @Inject
    UserApiService userApiService;
    @Inject
    GameMembersApiService gameMembersApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Subscriber subscriber;
    @Inject
    GameService gameService;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public UserListComponent() {

    }

    public UserListComponent(UserApiService userApiService, GameMembersApiService gameMembersApiService, TokenStorage tokenStorage) {
        this.userApiService = userApiService;
        this.gameMembersApiService = gameMembersApiService;
        this.tokenStorage = tokenStorage;
    }

    public UserListComponent inject(UserApiService userApiService, GameMembersApiService gameMembersApiService, TokenStorage tokenStorage, Subscriber subscriber) {
        this.userApiService = userApiService;
        this.gameMembersApiService = gameMembersApiService;
        this.tokenStorage = tokenStorage;
        this.subscriber = subscriber;
        return this;
    }

    public UserListComponent setGame(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public void setItem(@NotNull User user) {
        if (Objects.equals(user, game.getOwner())) {
            userText.setText(user.getName() + "(" + resources.getString("host") + ")");
        } else {
            userText.setText(user.getName());
        }
        comboBox.setItems(FXCollections.observableArrayList(resources.getString("player.ready"), resources.getString("player.not.ready")));
        comboBox.setValue(user.isReady() ? resources.getString("player.ready") : resources.getString("player.not.ready"));
        comboBox.setDisable(user.getEmpire() == null);
        comboBox.setOnMouseClicked(event -> {
            if (comboBox.isDisabled() && user.equals(gameService.getOwnUser())) {
                new Alert(Alert.AlertType.ERROR, "You can't change the readiness without choosing an empire").show();
            }
        });
        if (user.equals(gameService.getOwnUser())) {
            subscriber.listen(comboBox.valueProperty(), (observable, oldValue, newValue) -> {
                        if (newValue == null || newValue.equals(oldValue)) return;
                        subscriber.subscribe(gameService.changeReadiness(resources.getString("player.ready").equals(newValue)),
                                result -> {
                                },
                                error -> new Alert(Alert.AlertType.ERROR, error.getMessage()).show()
                        );
                    }
            );
        } else {
            subscriber.listen(user.listeners(), User.PROPERTY_READY, evt -> Platform.runLater(() -> comboBox.setValue((Boolean) evt.getNewValue() ? resources.getString("player.ready") : resources.getString("player.not.ready"))));
        }
        if (!user.equals(gameService.getOwnUser())) {
            comboBox.setDisable(true);
        }
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
}
