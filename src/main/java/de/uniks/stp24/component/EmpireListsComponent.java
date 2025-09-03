package de.uniks.stp24.component;

import de.uniks.stp24.App;
import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Fleet;
import de.uniks.stp24.model.game.Jobs;
import de.uniks.stp24.model.game.User;
import de.uniks.stp24.rest.EmpireApiService;
import de.uniks.stp24.service.EmpireService;
import de.uniks.stp24.service.GameService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.constructs.listview.ComponentListCell;
import org.fulib.fx.controller.Subscriber;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Component(view = "EmpireLists.fxml")
public class EmpireListsComponent extends VBox {
    @FXML
    public ListView<Empire> empireLists;

    @Inject
    App app;

    @Inject
    Provider<EmpireListComponent> empireListComponentProvider;

    @Inject
    GameService gameService;
    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    Subscriber subscriber;
    @Inject
    EmpireApiService empireService;

    public Empire empire;

    private final ObservableList<Empire> empiresList = FXCollections.observableArrayList();

    @Inject
    public EmpireListsComponent() {
    }

    @OnInit
    public void onInit() {
        empiresList.clear();
        for (User member : gameService.getGame().getMembers()) {
            if (member.getEmpire() != null && member.getEmpire() != gameService.getOwnUser().getEmpire()){
                empiresList.add(member.getEmpire());
            }
        }

    }

    @OnRender
    public void setText() {
        empireLists.setItems(empiresList);

        empireLists.setCellFactory(list -> new ComponentListCell<>(app, empireListComponentProvider));
    }

    public void onItemClicked() {
        Empire selectedEmpire = empireLists.getSelectionModel().getSelectedItem();
        if (selectedEmpire != null) {
            empire = selectedEmpire;
        }
    }

    @OnDestroy
    void onDestroy() {
        subscriber.dispose();
    }
}
