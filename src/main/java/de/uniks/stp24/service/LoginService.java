package de.uniks.stp24.service;

import de.uniks.stp24.dto.LoginDto;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.dto.RefreshDto;
import de.uniks.stp24.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class LoginService {

    @Inject
    AuthApiService authApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    PrefService prefService;

    @Inject
    public LoginService() {
    }

    public boolean autoLogin() {
        return autoLogin(prefService.getRefreshToken());
    }
    public boolean autoLogin(String refreshToken) {
        if (refreshToken == null || System.getenv("DISABLE_AUTO_LOGIN") != null || refreshToken.isEmpty()) {
            return false;
        }
        try {
            final LoginResult result = authApiService.refresh(new RefreshDto(refreshToken)).blockingFirst();
            tokenStorage.setToken(result.accessToken());
            tokenStorage.setUserId(result._id());
            tokenStorage.setRefreshToken(refreshToken);
            prefService.setRefreshToken(refreshToken);
            return true;
        } catch (Exception exception) {
            System.out.println("Auto login failed: " + exception.getMessage());
            return false;
        }
    }

    public Observable<LoginResult> login(String username, String password, boolean rememberMe) {
        return authApiService
                .login(new LoginDto(username, password))
                .doOnNext(loginResult -> {
                    tokenStorage.setToken(loginResult.accessToken());
                    tokenStorage.setUserId(loginResult._id());
                    tokenStorage.setRefreshToken(loginResult.refreshToken());

                    if (rememberMe) {
                        prefService.setRefreshToken(loginResult.refreshToken());
                    }
                });
    }

    public Observable<LoginResult> refreshToken() {
        if (tokenStorage.getRefreshToken() == null) {
            return Observable.error(new RuntimeException("No refresh token"));
        }
        return authApiService
                .refresh(new RefreshDto(tokenStorage.getRefreshToken()))
                .doOnNext(loginResult -> tokenStorage.setRefreshToken(loginResult.refreshToken()));
    }
}
