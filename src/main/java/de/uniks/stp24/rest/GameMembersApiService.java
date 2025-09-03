package de.uniks.stp24.rest;

import de.uniks.stp24.dto.ChangeMemberDto;
import de.uniks.stp24.dto.GameMembersDto;
import de.uniks.stp24.dto.JoinGameDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface GameMembersApiService {
    @POST("games/{game}/members")
    Observable<Response<GameMembersDto>> joinGame(@Path("game") String game, @Body JoinGameDto dto);

    @GET("games/{game}/members")
    Observable<Response<List<GameMembersDto>>> getPlayer(@Path("game") String game);

    @GET("games/{game}/members/{user}")
    Observable<Response<GameMembersDto>> getPlayer(@Path("game") String game, @Path("user") String user);

    @PATCH("games/{game}/members/{user}")
    Observable<Response<GameMembersDto>> updateGameMembership(@Path("game") String game, @Path("user") String user, @Body ChangeMemberDto dto);

    @DELETE("games/{game}/members/{user}")
    Observable<Response<GameMembersDto>> leaveGame(@Path("game") String game, @Path("user") String user);
}
