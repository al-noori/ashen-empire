package de.uniks.stp24.dto;

import java.util.Map;

public record CreateFleetDto(
        String name,
        String location,
        Map<String, Integer> size
) {
}
