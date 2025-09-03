package de.uniks.stp24.service;

import de.uniks.stp24.dto.LoginDto;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.dto.UpdateUserDto;
import de.uniks.stp24.dto.UserDto;
import de.uniks.stp24.rest.AuthApiService;
import de.uniks.stp24.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class EditUserService {

    @Inject
    public UserApiService userApiService;
    @Inject
    public AuthApiService authApiService;
    @Inject
    public TokenStorage tokenStorage;

    @Inject
    public EditUserService() {
    }

    // only id changes after password and/or name change, token is still valid so not need to update it
    public Observable<UserDto> update(String username, String password) {
        return userApiService
                .update(tokenStorage.getUserId(), new UpdateUserDto(username, password))
                .doOnNext(user -> tokenStorage.setUserId(user._id()));
    }

    public Observable<UserDto> delete() {
        return userApiService
                .delete(tokenStorage.getUserId());
    }

    public Observable<LoginResult> checkPassword(String password) {
        String username = userApiService.findOne(tokenStorage.getUserId()).blockingFirst().name();
        return authApiService
                .login(new LoginDto(username, password));
    }

    public Observable<UserDto> getUser() {
        return userApiService.findOne(tokenStorage.getUserId());
    }


}
