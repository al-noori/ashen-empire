package de.uniks.stp24.service;

import de.uniks.stp24.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

import javax.inject.Inject;

public class LobbyService {

    @Inject
    AuthApiService authApiService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    PrefService prefService;

    @Inject
    public LobbyService() {
    }

    public Observable<Response<Void>> logout() {
        Observable<Response<Void>> logoutResult = authApiService.logout();
        final String refreshToken = prefService.getRefreshToken();

        if (refreshToken != null && !refreshToken.isEmpty()) {
            prefService.setRefreshToken("");
        }
        return logoutResult;
    }

}
