package de.uniks.stp24.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record GameEmpireDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String game,
        String user,
        String name,
        String description,
        String color,
        Integer flag,
        Integer portrait,
        String homeSystem,
        String[] traits,
        Map<String, Integer> resources,
        List<String> technologies
) {
}