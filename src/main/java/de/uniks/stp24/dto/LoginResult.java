package de.uniks.stp24.dto;

public record LoginResult(
    String _id,
    String accessToken,
    String refreshToken
) {
}
