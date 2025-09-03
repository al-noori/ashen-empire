package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record GameDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String owner,
        String name,
        Boolean started,
        Integer speed,
        Integer period,
        GameSettingsDto settings,
        int playersCount,
        String hostname
) {
}
