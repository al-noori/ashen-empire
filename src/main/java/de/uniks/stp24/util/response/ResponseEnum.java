package de.uniks.stp24.util.response;

public enum ResponseEnum {
    ERROR(-1),
    OK(200),
    SUCCESS(201),
    VALIDATION_FAILED(400),
    BAERER_TOKEN_INVALID(401),
    NOT_YOUR_GAME(403),
    NOT_FOUND(404),
    ALREADY_RUNNING(409),
    RATE_LIMIT_REACHED(429);

    int param = 0;

    ResponseEnum(int i) {
        param = i;
    }

    public static ResponseEnum fromInt(int i) {
        for (ResponseEnum response : ResponseEnum.values()) {
            if (response.param == i) {
                return response;
            }
        }
        return ERROR;
    }
}
