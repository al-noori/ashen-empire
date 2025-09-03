package de.uniks.stp24.dto;

public record UpdateGameDto(
        String name,
        String password,
        Boolean started,
        Number speed,
        GameSettingsDto settings
) {
}
