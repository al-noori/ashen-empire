package de.uniks.stp24.rest;

import de.uniks.stp24.dto.CreateWarDto;
import de.uniks.stp24.dto.WarsDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface WarsApiService {
    @POST("games/{game}/wars")
    Observable<WarsDto> createWar(@Path("game") String game, @Body CreateWarDto createWarDto);
    @GET("games/{game}/wars")
    Observable<List<WarsDto>> getWars(@Path("game") String game);
    @GET("games/{game}/wars/{war}")
    Observable<WarsDto> getWar(@Path("game") String game, @Path("war") String war);
    @PATCH("games/{game}/wars/{war}")
    Observable<WarsDto> updateWar(@Path("game") String game, @Path("war") String war, @Body WarsDto warsDto);
    @DELETE("games/{game}/wars/{war}")
    Observable<WarsDto> deleteWar(@Path("game") String game, @Path("war") String war);
}
