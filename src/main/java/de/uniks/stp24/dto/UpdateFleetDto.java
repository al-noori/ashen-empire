package de.uniks.stp24.dto;

import java.util.Map;

public record UpdateFleetDto(
        String name,
        Map<String, Integer> size
) {
}
