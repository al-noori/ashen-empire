package de.uniks.stp24.component;

import de.uniks.stp24.dto.EmpireDto;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.fulib.fx.annotation.controller.Component;
import org.fulib.fx.annotation.controller.Resource;
import org.fulib.fx.constructs.listview.ReusableItemComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ResourceBundle;

@Component(view = "Empire.fxml")
public class EmpireComponent extends HBox implements ReusableItemComponent<EmpireDto> {

    @FXML
    Text empireText;

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public EmpireComponent() {
    }

    @Override
    public void setItem(@NotNull EmpireDto empireDto) {
        empireText.setText(empireDto.name());
    }
}
