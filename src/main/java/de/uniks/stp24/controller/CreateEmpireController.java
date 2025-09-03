package de.uniks.stp24.controller;

import de.uniks.stp24.App;
import de.uniks.stp24.component.TraitCardComponent;
import de.uniks.stp24.component.TraitTinyCardComponent;
import de.uniks.stp24.component.TraitsComponent;
import de.uniks.stp24.dto.EmpireDto;
import de.uniks.stp24.model.game.Game;
import de.uniks.stp24.rest.GameMembersApiService;
import de.uniks.stp24.service.EmpireService;
import de.uniks.stp24.service.ImageCache;
import de.uniks.stp24.service.TokenStorage;
import de.uniks.stp24.service.TraitsService;
import de.uniks.stp24.ws.EventListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.fulib.fx.annotation.controller.Controller;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.controller.SubComponent;
import org.fulib.fx.annotation.controller.Title;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.annotation.event.OnInit;
import org.fulib.fx.annotation.event.OnRender;
import org.fulib.fx.annotation.param.Param;
import org.fulib.fx.controller.Subscriber;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Title("Create an empire")
public class CreateEmpireController {
    @FXML
    Button traitsButton;
    @FXML
    Button backButton;
    @FXML
    TextField nameInput;
    @FXML
    private ComboBox<String> selectColour;
    @FXML
    TextField descriptionInput;
    @FXML
    private ComboBox<ImageView> selectFlag;
    @FXML
    private ComboBox<ImageView> selectPortrait;
    @FXML
    Button saveButton;
    @FXML
    StackPane stackpane;

    @Inject
    @Resource
    ResourceBundle resources;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    ImageCache imageCache;
    @Inject
    GameMembersApiService gameMembersApiService;

    @Inject
    App app;
    @Inject
    EventListener eventListener;
    @Inject
    Subscriber subscriber;
    @Inject
    EmpireService empireService;

    @SubComponent
    @Inject
    TraitsComponent traitsComponent;

    @SubComponent
    @Inject
    TraitCardComponent traitCardComponent;

    @Inject
    TraitsService traitsService;

    @Param("game")
    Game game;
    @Param("empireSet")
    boolean empireSet;


    Map<String, String> colorHexMap = new HashMap<>();

    public ObservableList<ImageView> flagImages;
    public ObservableList<ImageView> portraitImages;
    public ObservableList<String> colours;

    String selectedColour;
    int selectedFlag;
    int selectedPortrait;

    @Inject
    public CreateEmpireController() {
    }

    @OnInit
    void onInit() {
        flagImages = FXCollections.observableArrayList();
        portraitImages = FXCollections.observableArrayList();
        colours = FXCollections.observableArrayList();
        initFlag();
        initPortrait();
        initColour();
    }

    public void initColour() {
        initColorHexMap();
        colours.addAll(colorHexMap.keySet());
    }

    public void initPortrait() {
        for (int i = 1; i <= 16; i++) {
            portraitImages.add(loadImageView("portraits/" + i + ".png"));
        }
    }

    public void initFlag() {
        for (int i = 1; i <= 16; i++) {
            flagImages.add(loadImageView("flags/Flag" + i + ".png"));
        }
    }

    @OnRender
    void render() {
        selectColour.setEditable(false);
        selectFlag.setEditable(false);
        selectPortrait.setEditable(false);
        selectColour.setItems(colours);
        selectPortrait.setItems(portraitImages);
        selectFlag.setItems(flagImages);
    }

    void initColorHexMap() {
        colorHexMap.put(resources.getString("color.red"), "#FF0000");
        colorHexMap.put(resources.getString("color.green"), "#008000");
        colorHexMap.put(resources.getString("color.blue"), "#0000FF");
        colorHexMap.put(resources.getString("color.yellow"), "#FFFF00");
        colorHexMap.put(resources.getString("color.black"), "#000000");
        colorHexMap.put(resources.getString("color.white"), "#FFFFFF");
        colorHexMap.put(resources.getString("color.orange"), "#FFA500");
        colorHexMap.put(resources.getString("color.pink"), "#FFC0CB");
        colorHexMap.put(resources.getString("color.purple"), "#800080");
        colorHexMap.put(resources.getString("color.brown"), "#A52A2A");
        colorHexMap.put(resources.getString("color.gray"), "#808080");
    }

    private ImageView loadImageView(String path) {
        Image image = imageCache.get(path);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        return imageView;
    }

    public void save() {
        createEmpire();
        traitsService.clear();
        app.show("/gameOverview", Map.of("game", game, "empireSet", empireSet));
    }

    public void back() {
        //back to GameOverview
        traitsService.clear();
        app.show("/gameOverview", Map.of("game", game, "empireSet", empireSet));
    }

    private void createEmpire() {
        String name = nameInput.getText();
        String description = descriptionInput.getText();
        String colour = colorHexMap.get(selectColour.getValue());
        int flag = selectedFlag;
        int portrait = selectedPortrait;
        String[] selectedTraitIds = traitsService.selectedTraitIds.get().toArray(new String[0]);
        EmpireDto newEmpire = new EmpireDto(null, name, description, colour, flag, portrait, null, selectedTraitIds, null, LocalDateTime.now(), LocalDateTime.now());
        empireService.createEmpire(game.getId(), newEmpire);
    }

    @OnRender
    public void updateSaveButton() {
        //lock save button until all input valid
        BooleanBinding isAllInputsNotEmpty = Bindings.createBooleanBinding(() ->
                        !nameInput.getText().isEmpty() && !descriptionInput.getText().isEmpty() && selectColour.getValue() != null && selectFlag.getValue() != null && selectPortrait.getValue() != null,
                descriptionInput.textProperty(),
                selectColour.valueProperty(),
                selectFlag.valueProperty(),
                selectPortrait.valueProperty());

        saveButton.disableProperty().bind(isAllInputsNotEmpty.not());
    }

    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
        imageCache.clear();
    }

    @FXML
    void colorSelectAction() {
        selectedColour = selectColour.getValue();
    }

    @FXML
    void flagSelectAction() {
        selectedFlag = selectFlag.getSelectionModel().getSelectedIndex();
    }

    @FXML
    void portraitSelectAction() {
        selectedPortrait = selectPortrait.getSelectionModel().getSelectedIndex();
    }

    public void selectTraits() {
        traitsComponent.stackpane = stackpane;
        for (Node node : traitsComponent.gridPaneTinyCards.getChildren()) {
            TraitTinyCardComponent traitTinyCardComponent = (TraitTinyCardComponent) node;
            traitTinyCardComponent.stackpaneTraits = stackpane;
        }
        stackpane.getChildren().add(traitsComponent);
    }

}
