package de.uniks.stp24.service;

import de.uniks.stp24.dto.SystemUpgradeDto;
import de.uniks.stp24.dto.SystemUpgradeResponseDto;
import de.uniks.stp24.model.game.Fraction;
import org.fulib.fx.annotation.controller.Resource;

import javax.inject.Inject;
import java.util.ResourceBundle;

public class UpgradeService {

    @Inject
    @Resource
    ResourceBundle resources;

    @Inject
    public UpgradeService() {
    }

    public SystemUpgradeDto getNextUpgradePreset(SystemUpgradeResponseDto upgrade, Fraction fraction) {
        if (getNextUpgrade(fraction.getUpgrade()) == null) return null;
        return switch (getNextUpgrade(fraction.getUpgrade())) {
            case "unexplored" -> upgrade.unexplored();
            case "explored" -> upgrade.explored();
            case "colonized" -> upgrade.colonized();
            case "upgraded" -> upgrade.upgraded();
            default -> upgrade.developed();
        };
    }

    public String getNextUpgradeText(String upgrade) {
        return switch (upgrade) {
            case "unexplored" -> resources.getString("upgradeService.explore");
            case "explored" -> resources.getString("upgradeService.colonize");
            case "colonized" -> resources.getString("upgradeService.upgrade");
            case "upgraded" -> resources.getString("upgradeService.develop");
            case "developed" -> resources.getString("upgradeService.maxed");
            default -> "Error";
        };
    }

    public String getUpgradeText(String upgrade) {
        return switch (upgrade) {
            case "unexplored" -> resources.getString("upgradeService.unexplored");
            case "explored" -> resources.getString("upgradeService.explored");
            case "colonized" -> resources.getString("upgradeService.colonized");
            case "upgraded" -> resources.getString("upgradeService.upgraded");
            case "developed" -> resources.getString("upgradeService.developed");
            default -> "Error";
        };
    }

    public String getNextUpgrade(String upgrade) {
        if (upgrade == null) return "developed";
        return switch (upgrade) {
            case "unexplored" -> "explored";
            case "explored" -> "colonized";
            case "colonized" -> "upgraded";
            case "upgraded" -> "developed";
            case "developed" -> null;
            default -> "Error";
        };
    }


}
