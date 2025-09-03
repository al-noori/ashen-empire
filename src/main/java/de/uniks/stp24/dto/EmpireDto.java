package de.uniks.stp24.dto;


import java.time.LocalDateTime;
import java.util.List;

public record EmpireDto(
        String _id,
        String name,
        String description,
        String color,
        int flag,
        int portrait,
        String homeSystem,
        String[] traits,
        List<TechnologyDto> technologies,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
