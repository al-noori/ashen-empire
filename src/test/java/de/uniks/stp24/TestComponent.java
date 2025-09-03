package de.uniks.stp24;

import dagger.Component;
import de.uniks.stp24.dagger.MainComponent;
import de.uniks.stp24.dagger.MainModule;
import de.uniks.stp24.rest.*;
import de.uniks.stp24.service.*;
import de.uniks.stp24.ws.EventListener;

import javax.inject.Singleton;

@Component(modules = {MainModule.class, TestModule.class})
@Singleton
public interface TestComponent extends MainComponent {
    AuthApiService authApiService();
    UserService userService();
    GamesApiService gamesApiService();
    EventListener eventListener();
    EmpireApiService empireApiService();
    GameService gameService();

    GameManagerService gameManagerService();

    TokenStorage tokenStorage();

    GameMembersApiService gameMembersApiService();

    UserApiService userApiService();
    GameEmpiresApiService gameEmpiresApiService();

    EditUserService editUserService();
    GameSystemsApiService gameSystemsApiService();
    PresetsApiService presetsApiService();
    JobApiService jobApiService();

    MusicService musicService();

    TechnologiesServices technologiesServices();

    MarketService marketService();
    AchievementsApiService achievementsApiService();
    TraitsService traitsService();
    ShipApiService shipApiService();
    FleetsApiService fleetApiService();
    WarsApiService warsApiService();
    AchievementsService achievementsService();

    void inject(GameService gameService);
    VariablesApiService variablesApiService();
    @Component.Builder
    interface Builder extends MainComponent.Builder {
        @Override
        TestComponent build();
    }
}
