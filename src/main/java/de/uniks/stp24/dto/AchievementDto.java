package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record AchievementDto(
        String id,
        String user,
        int progress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime unlockedAt
) {
}
