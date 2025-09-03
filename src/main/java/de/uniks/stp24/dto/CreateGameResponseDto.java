package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record CreateGameResponseDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String name,
        String owner,
        int members,
        int maxMembers,
        boolean started,

        int speed,
        int period,
        GameSettingsDto settings
) {
}