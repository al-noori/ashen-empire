package de.uniks.stp24.dto;

import java.time.LocalDateTime;

public record GameMembersDto(
        String game,
        String user,
        Boolean ready,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EmpireDto empire
) {
}
