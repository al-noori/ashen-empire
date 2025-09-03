package de.uniks.stp24.dto;

public record JoinGameDto(
        Boolean ready,
        EmpireDto empire,
        String password
) {
}
