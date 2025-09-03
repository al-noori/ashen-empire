package de.uniks.stp24.service;

import de.uniks.stp24.dto.LoginDto;
import de.uniks.stp24.dto.LoginResult;
import de.uniks.stp24.dto.RefreshDto;
import de.uniks.stp24.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    AuthApiService authApiService;
    @Spy
    TokenStorage tokenStorage;
    @Mock
    PrefService prefService;
    @InjectMocks
    LoginService loginService;

    @Test
    void login() {
        final LoginResult expectedResult = new LoginResult("1", "a", "r");
        Mockito.when(authApiService.login(new LoginDto("alice123", "hello456")))
                .thenReturn(Observable.just(expectedResult));

        final Observable<LoginResult> result$ = loginService.login("alice123", "hello456", false);

        // result should be untouched
        final LoginResult result = result$.blockingFirst();
        assertEquals(expectedResult, result);

        // TokenStorage should have the correct data
        assertEquals("1", tokenStorage.getUserId());
        assertEquals("a", tokenStorage.getToken());
    }

    @Test
    void loginRememberMe() {
        final LoginResult expectedResult = new LoginResult("1", "a", "r");
        Mockito.when(authApiService.login(new LoginDto("alice123", "hello456")))
                .thenReturn(Observable.just(expectedResult));

        loginService.login("alice123", "hello456", true)
                .blockingFirst();

        // refresh token should be stored in PrefService
        Mockito.verify(prefService).setRefreshToken("r");
    }

    @Captor
    ArgumentCaptor<RefreshDto> refreshDtoCaptor;

    @Test
    void autoLogin() {
        when(prefService.getRefreshToken()).thenReturn("r");
        when(authApiService.refresh(any())).thenReturn(Observable.just(new LoginResult("1", "a", "r")));

        boolean result = loginService.autoLogin();
        assertTrue(result);

        // TokenStorage should have the correct data
        assertEquals("1", tokenStorage.getUserId());
        assertEquals("a", tokenStorage.getToken());

        // refresh token should be stored in PrefService
        Mockito.verify(prefService).setRefreshToken("r");

        // AuthApiService.refresh should have been called with the previous refresh token
        Mockito.verify(authApiService).refresh(refreshDtoCaptor.capture());
        final RefreshDto actualRefreshDto = refreshDtoCaptor.getValue();
        assertEquals("r", actualRefreshDto.refreshToken());
    }
}
