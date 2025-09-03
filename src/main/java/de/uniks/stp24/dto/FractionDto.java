package de.uniks.stp24.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record FractionDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String game,
        String name,
        Map<String, Integer> districtSlots,
        Map<String, Integer> districts,
        int capacity,
        String[] buildings,
        String upgrade,
        int population,
        Map<String, Integer> links,
        int x,
        int y,
        String owner,
        String homeSystem,
        String type,
        Integer health
) {
}
