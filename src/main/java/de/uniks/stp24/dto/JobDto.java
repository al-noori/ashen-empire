package de.uniks.stp24.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record JobDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        double progress,
        double total,
        String game,
        String empire,
        String system,
        int priority,
        String type,
        String building,
        String district,
        String technology,
        String ship,
        Map<String, Integer> cost,
        ResultDto result,
        List<String> path,
        String fleet
) {
}
