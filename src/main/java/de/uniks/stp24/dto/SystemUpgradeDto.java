package de.uniks.stp24.dto;

import java.util.Map;

public record SystemUpgradeDto(
        String id,
        String next,
        int upgrade_time,
        double pop_growth,
        Map<String, Integer> cost,
        Map<String, Integer> upkeep,
        double capacity_multiplier
) {
}
