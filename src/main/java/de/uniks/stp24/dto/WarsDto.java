package de.uniks.stp24.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WarsDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String game,
        String attacker,
        String defender,
        String name
) {
}
