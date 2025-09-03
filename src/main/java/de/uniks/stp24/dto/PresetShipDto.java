package de.uniks.stp24.dto;


import java.util.Map;

public record PresetShipDto(
        String id,
        int speed,
        int health,
        Map<String, Integer> attack,
        Map<String, Integer> defense,
        Map<String, Integer> cost

) {
}
