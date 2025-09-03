package de.uniks.stp24.dto;

public record ResultDto(
        int statusCode,
        String error,
        String message
) {
}