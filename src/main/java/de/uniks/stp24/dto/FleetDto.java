package de.uniks.stp24.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record FleetDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String game,
        String empire,
        String name,
        String location,
        Map<String, Integer> size
) {
}
