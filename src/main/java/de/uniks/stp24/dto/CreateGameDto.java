package de.uniks.stp24.dto;

public record CreateGameDto(
        String name,
        String password,
        Boolean started,
        Integer speed,
        GameSettingsDto settings
) {
}
