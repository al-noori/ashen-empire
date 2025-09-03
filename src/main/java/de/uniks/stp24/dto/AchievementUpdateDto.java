package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record AchievementUpdateDto(
        String unlockedAt,
        int progress
) {
}
