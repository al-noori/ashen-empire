package de.uniks.stp24;

import dagger.Module;
import dagger.Provides;
import de.uniks.stp24.rest.*;
import de.uniks.stp24.service.*;
import de.uniks.stp24.ws.EventListener;
import okhttp3.OkHttpClient;
import org.mockito.Mockito;

import javax.inject.Singleton;
import java.util.Locale;

import static org.mockito.Mockito.mock;

@Module
public class TestModule {
    @Provides
    @Singleton
    PrefService prefService() {
        final PrefService mock = mock(PrefService.class);
        Mockito.doReturn(Locale.ENGLISH).when(mock).getLocale();
        Mockito.doReturn(null).when(mock).getRefreshToken();
        return mock;
    }

    @Provides
    @Singleton
    AuthApiService authApiService() {
        return mock(AuthApiService.class); //create instance from service with mockito
    }

    @Provides
    @Singleton
    GameMembersApiService gameMembersApiService() {
        return mock(GameMembersApiService.class);
    }

    @Provides
    @Singleton
    UserApiService userApiService() {
        return mock(UserApiService.class);
    }

    @Provides
    @Singleton
    GamesApiService gamesApiService() {
        return mock(GamesApiService.class);
    }

    @Provides
    @Singleton
    GameSystemsApiService gameSystemsApiService() {
        return mock(GameSystemsApiService.class);
    }

    @Provides
    @Singleton
    GameEmpiresApiService gameEmpiresApiService() {
        return mock(GameEmpiresApiService.class);
    }

    @Provides
    @Singleton
    UserService userService() {
        return mock(UserService.class);
    }

    @Provides
    @Singleton
    LoginService loginService() {
        return mock(LoginService.class);
    }

    @Provides
    @Singleton
    de.uniks.stp24.ws.EventListener eventListener() {
        return mock(EventListener.class);
    }

    @Provides
    @Singleton
    GameManagerService gameManagerService() {
        return mock(GameManagerService.class);
    }

    @Provides
    @Singleton
    AchievementsApiService achievementsApiService() {
        return mock(AchievementsApiService.class);
    }

    @Provides
    @Singleton
    LobbyService lobbyService() {
        return mock(LobbyService.class);
    }

    @Provides
    @Singleton
    TokenStorage tokenStorage() {
        final TokenStorage mock = mock(TokenStorage.class);
        Mockito.doReturn("3").when(mock).getUserId();
        return mock;
    }

    @Provides
    @Singleton
    EditUserService editUserService() {
        return mock(EditUserService.class);
    }

    @Provides
    @Singleton
    PresetsApiService presetsApiService() {
        return mock(PresetsApiService.class);
    }

    @Provides
    @Singleton
    OkHttpClient client() {
        return Mockito.mock(OkHttpClient.class);
    }

    @Provides
    @Singleton
    EmpireApiService empireApiService() {
        return mock(EmpireApiService.class);
    }

    @Provides
    @Singleton
    JobApiService jobApiService() {
        return mock(JobApiService.class);
    }

    @Provides
    @Singleton
    VariablesApiService variablesApiService() {
        return mock(VariablesApiService.class);
    }
  
    @Provides
    @Singleton
    TechnologiesServices technologiesServices() {
        return mock(TechnologiesServices.class);
    }

    @Provides
    @Singleton
    FleetsApiService fleetsApiService() {
        return mock(FleetsApiService.class);
    }

    @Provides
    @Singleton
    ShipApiService shipsApiService() {
        return mock(ShipApiService.class);
    }

    @Provides
    @Singleton
    WarsApiService warsApiService() {
        return mock(WarsApiService.class);
    }
    @Provides
    @Singleton
    AchievementsService achievementsService() {
        return mock(AchievementsService.class);
    }
}
