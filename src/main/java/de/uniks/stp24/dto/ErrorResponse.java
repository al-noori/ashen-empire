package de.uniks.stp24.dto;

public record ErrorResponse(
        int statusCode,
        String error,
        String[] message
) {
}
