package de.uniks.stp24.dto;

public record AchievementSummaryDto(
        String id,
        int started,
        int unlocked,
        int progress
) {
}
