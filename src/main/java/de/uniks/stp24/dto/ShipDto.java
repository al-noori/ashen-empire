package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record ShipDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String game,
        String empire,
        String fleet,
        String type,
        Integer health,
        Integer experience
) {
}
