package de.uniks.stp24.component;

import de.uniks.stp24.service.GameService;
import de.uniks.stp24.service.MarketService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.fulib.fx.annotation.controller.Component;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.annotation.event.OnDestroy;
import org.fulib.fx.controller.Subscriber;
import javax.inject.Inject;
import java.util.ResourceBundle;


@Component(view = "Market.fxml")
public class MarketComponent extends AnchorPane {

    @Inject
    public MarketService marketService;

    public StackPane stackPane;
    @FXML
    Button changeButton;
    @FXML
    private Text marketFeeText;

    @FXML
    private Button backButton;

    @FXML
    private TextField energyField;

    @FXML
    private TextField mineralsField;

    @FXML
    private TextField foodField;

    @FXML
    private TextField fuelField;

    @FXML
    private TextField alloysField;

    @FXML
    private TextField consumer_goodsField;

    @FXML
    private Text buyText;
    @FXML
    private Text slashText;
    @FXML
    private Text sellText;
    @FXML
    private ToggleButton toggleButton;

    private double marketFee;

    @Inject
    Subscriber subscriber;

    @Inject
    GameService gameService;

    @FXML
    private RadioButton energyRadio;
    @FXML
    private RadioButton mineralsRadio;
    @FXML
    private RadioButton foodRadio;
    @FXML
    private RadioButton fuelRadio;
    @FXML
    private RadioButton alloysRadio;
    @FXML
    private RadioButton consumer_goodsRadio;
    @FXML
    private Label creditField;
    private ToggleGroup resourcesToggleGroup = new ToggleGroup();
    @Inject
    @Resource
    ResourceBundle resourceBundle;

    @Inject
    public MarketComponent() {
    }

    public void initialize() {
        energyRadio.setToggleGroup(resourcesToggleGroup);
        mineralsRadio.setToggleGroup(resourcesToggleGroup);
        foodRadio.setToggleGroup(resourcesToggleGroup);
        fuelRadio.setToggleGroup(resourcesToggleGroup);
        alloysRadio.setToggleGroup(resourcesToggleGroup);
        consumer_goodsRadio.setToggleGroup(resourcesToggleGroup);
        subscriber.subscribe(marketService.getMarketFee(),
                fee -> {
                    marketFee = fee;
                    marketFeeText.setText("Market Fee: " + fee * 100 + "%");
                });

        backButton.setOnAction(event -> {
            stackPane.getChildren().remove(this);
        });

        toggleButton.setOnAction(event -> {
            toggleButton.setStyle("-fx-background-color: red;");
            if (toggleButton.isSelected()) {
                buyText.setFill(Color.RED);
                slashText.setFill(Color.BLACK);
                sellText.setFill(Color.BLACK);
            } else {
                buyText.setFill(Color.BLACK);
                slashText.setFill(Color.BLACK);
                sellText.setFill(Color.RED);
            }
        });
        fixNums(energyField);
        fixNums(mineralsField);
        fixNums(foodField);
        fixNums(fuelField);
        fixNums(alloysField);
        fixNums(consumer_goodsField);

        resourcesToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            String resource = ((RadioButton) resourcesToggleGroup.getSelectedToggle()).getId();
            resource = resource.substring(0, resource.length()-5);
            final String finalResource = resource;
            subscriber.subscribe(marketService.getCreditValue(resource),
                    creditValue -> {
                        TextField textField = findTextFieldById(finalResource + "Field");
                        creditField.textProperty().bind(Bindings.createStringBinding(
                                () -> {
                                    try {
                                        double value = Double.parseDouble(textField.getText());
                                        double credits = value * creditValue;
                                        if(!toggleButton.isSelected()) {
                                            double finalCredits = credits * (1 - marketFee);
                                            return (int)finalCredits + " (" + (int)credits + " - " + (int)(marketFee * 100 ) + "%)";
                                        } else {
                                            double finalCredits = credits * (1 + marketFee);
                                            return (int)finalCredits + " (" + (int) credits + " + " + (int)(marketFee * 100 ) + "%)";
                                        }
                                    } catch (NumberFormatException ignored) {
                                    }
                                    return "Error";
                                },
                                textField.textProperty(),
                                toggleButton.selectedProperty()));
                    });
        });
    }

    private void fixNums(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Double.parseDouble(newValue);
            } catch (NumberFormatException ignored) {
                try {
                    Double.parseDouble(oldValue);
                    field.setText(oldValue);
                } catch (NumberFormatException ignored2) {
                }
            }
        });
    }

    public TextField findTextFieldById(String id) {
        return switch (id) {
            case "energyField" -> energyField;
            case "mineralsField" -> mineralsField;
            case "foodField" -> foodField;
            case "fuelField" -> fuelField;
            case "alloysField" -> alloysField;
            case "consumer_goodsField" -> consumer_goodsField;
            default -> null;
        };
    }
    @OnDestroy
    public void onDestroy() {
        subscriber.dispose();
    }
    @FXML
    void change(){
        RadioButton selected = (RadioButton) resourcesToggleGroup.getSelectedToggle();
        if(selected == null){
            return;
        }
        String resource = selected.getId();
        resource = resource.substring(0, resource.length()-5);
        int count = Integer.parseInt(findTextFieldById(resource + "Field").getText());
        if (!toggleButton.isSelected()) {
            count = -count;
        }
        subscriber.subscribe(marketService.trade(resource, count),
                gameEmpireDto -> {
                    System.out.println("Trade successful");
                });
    }
}