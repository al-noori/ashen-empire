package de.uniks.stp24.rest;

import de.uniks.stp24.dto.CreateGameDto;
import de.uniks.stp24.dto.CreateGameResponseDto;
import de.uniks.stp24.dto.GameDto;
import de.uniks.stp24.dto.UpdateGameDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface GamesApiService {

    @POST("games")
    Observable<Response<CreateGameResponseDto>> createGame(@Body CreateGameDto dto);

    @GET("games?members=true")
    Observable<Response<List<CreateGameResponseDto>>> getAllGames();

    @GET("games/{id}")
    Observable<Response<CreateGameResponseDto>> getGameById(@Path("id") String id);

    @PATCH("games/{id}")
    Observable<Response<CreateGameResponseDto>> updateGame(@Path("id") String id, @Query("tick") boolean tick, @Body CreateGameDto dto);

    @DELETE("games/{id}")
    Observable<Response<CreateGameResponseDto>> deleteGame(@Path("id") String id);

    @PATCH("games/{id}")
    Observable<GameDto> updateCurrentGame(@Path("id") String id, @Query("tick") boolean tick, @Body UpdateGameDto dto);

}
