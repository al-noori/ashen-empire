package de.uniks.stp24.rest;

import de.uniks.stp24.dto.GameEmpireDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

import java.util.List;

public interface EmpireApiService {
    @GET("games/{game}/empires")
    Observable<Response<List<GameEmpireDto>>> getAll(@Path("game") String game);

    @GET("games/{game}/empires/{id}")
    Observable<GameEmpireDto> getById(@Path("game") String game, @Path("id") String id);

    @PATCH("games/{game}/empires/{id}")
    Observable<GameEmpireDto> update(@Path("game") String game, @Path("id") String id, @Body GameEmpireDto empire);
}