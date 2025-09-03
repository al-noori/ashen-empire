package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record UserDto(
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String _id,
        String name,
        String avatar
) {
}
