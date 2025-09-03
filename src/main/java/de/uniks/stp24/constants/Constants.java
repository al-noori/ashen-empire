package de.uniks.stp24.constants;

import java.util.List;

public class Constants {
    public static final String EMPTY_STRING = "";

    // LOGIN, SIGNUP
    public static final int PASSWORD_CHARACTER_LIMIT = 8;
    public static final String NAME_ALREADY_EXISTS = "name.already.exists";
    public static final String PASSWORD_TOO_SHORT = "password.must.be.at.least.8.characters.long";
    public static final String PASSWORDS_DO_NOT_MATCH = "passwords.do.not.match";
    public static final String HTTP_409 = "HTTP 409";
    public static final String HTTP_401 = "HTTP 401";
    public static final String INCORRECT_PASSWORD = "password.incorrect";
    public static final String SET_NAME = "name.must.be.set";
    public static final String SET_PASSWORD = "password.must.be.set";
    public static final String INCORRECT_PASSWORD_AND_NAME = "name.or.password.incorrect";
    public static final String DE = "german";
    public static final String EN = "english";
    public static final String SUCCESSFUL_UPDATE = "successful.update";
    public static final String CUSTOM_ERROR = "error.occured";
    public static final String SUCCESSFUL_DELETION = "successful.deletion";
    public static final String ACCOUNT_DELETION = "account.deletion";
    //Game created successfully
    public static final String GAME_CREATED_SUCCESSFULLY = "game.created.successfully";
    //"Map size must be between 50 and 200"
    public static final String MAP_SIZE_ERROR = "map.size.error";
    // "Map size must be a number"
    public static final String MAP_SIZE_TYPE_ERROR = "map.size.type.error";
    public static final String CREATE_GAME_ERROR = "error.creating.game";
    public static final String MAP_SIZE_TOO_SMALL = "map.size.must.be.bigger.than.50";
    public static final String MAP_SIZE_TOO_BIG = "map.size.must.be.smaller.than.200";
    public static final String CHANGES_SAVED = "changes.saved";
    public static final String NOT_OWNER = "not.owner";

    public static final int GAME_SPEED_0 = 0;
    public static final int GAME_SPEED_1 = 1;
    public static final int GAME_SPEED_2 = 2;
    public static final int GAME_SPEED_3 = 3;
    public static final List<String> VARIABLES = List.of("empire.market.fee");
}
