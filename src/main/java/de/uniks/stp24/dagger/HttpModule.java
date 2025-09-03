package de.uniks.stp24.dagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dagger.Module;
import dagger.Provides;
import de.uniks.stp24.Main;
import de.uniks.stp24.rest.*;
import de.uniks.stp24.service.TokenStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;

@Module
public class HttpModule {
    @Provides
    @Singleton
    static OkHttpClient client(TokenStorage tokenStorage) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    final String token = tokenStorage.getToken();
                    if (token == null) {
                        return chain.proceed(chain.request());
                    }
                    final Request newRequest = chain
                            .request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }).addInterceptor(chain -> {
                    final Response response = chain.proceed(chain.request());
                    if (response.code() >= 300) {

                        System.err.println(chain.request());
                        System.err.println(response);
                    }
                    return response;
                }).build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient client, ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        return new Retrofit.Builder()
                .baseUrl(Main.API_URL + "/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    AuthApiService authApiService(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Provides
    @Singleton
    UserApiService userApiService(Retrofit retrofit) {
        return retrofit.create(UserApiService.class);
    }

    @Provides
    @Singleton
    GamesApiService gamesApiService(Retrofit retrofit) {
        return retrofit.create(GamesApiService.class);
    }

    @Provides
    @Singleton
    GameMembersApiService gameMembersApiService(Retrofit retrofit) {
        return retrofit.create(GameMembersApiService.class);
    }

    @Provides
    @Singleton
    GameEmpiresApiService gameEmpiresApiService(Retrofit retrofit) {
        return retrofit.create(GameEmpiresApiService.class);
    }

    @Provides
    @Singleton
    AchievementsApiService achievementsApiService(Retrofit retrofit) {
        return retrofit.create(AchievementsApiService.class);
    }

    @Provides
    @Singleton
    GameSystemsApiService gameSystemsApiService(Retrofit retrofit) {
        return retrofit.create(GameSystemsApiService.class);
    }

    @Provides
    @Singleton
    EmpireApiService empireApiService(Retrofit retrofit) {
        return retrofit.create(EmpireApiService.class);
    }

    @Provides
    @Singleton
    PresetsApiService presetsApiService(Retrofit retrofit) {
        return retrofit.create(PresetsApiService.class);
    }

    @Provides
    @Singleton
    JobApiService jobApiService(Retrofit retrofit) {
        return retrofit.create(JobApiService.class);
    }
    @Provides
    @Singleton
    VariablesApiService variablesApiService(Retrofit retrofit) {
        return retrofit.create(VariablesApiService.class);
    }
    @Provides
    @Singleton
    FleetsApiService fleetsApiService(Retrofit retrofit) {
        return retrofit.create(FleetsApiService.class);
    }
    @Provides
    @Singleton
    ShipApiService shipApiService(Retrofit retrofit) {
        return retrofit.create(ShipApiService.class);
    }
    @Provides
    @Singleton
    WarsApiService warsApiService(Retrofit retrofit) {
        return retrofit.create(WarsApiService.class);
    }
}
