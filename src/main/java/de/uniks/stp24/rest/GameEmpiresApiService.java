package de.uniks.stp24.rest;

import de.uniks.stp24.dto.AggregateResult;
import de.uniks.stp24.dto.GameEmpireDto;
import de.uniks.stp24.dto.UpdateEmpireDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface GameEmpiresApiService {
    @GET("games/{game}/empires")
    Observable<List<GameEmpireDto>> getAll(@Path("game") String game);

    @GET("games/{game}/empires/{empire}")
    Observable<Response<GameEmpireDto>> getOne(@Path("game") String game, @Path("empire") String empire);

    @PATCH("games/{game}/empires/{empire}")
    Observable<Response<GameEmpireDto>> editOne(@Path("game") String game, @Path("empire") String empire, @Query("free") boolean free, @Body UpdateEmpireDto dto);

    @GET("games/{game}/empires/{empire}/aggregates/resources.periodic")
    Observable<Response<AggregateResult>> getRessourceAggregate(@Path("game") String game, @Path("empire") String empire);

    @GET("games/{game}/empires/{empire}/aggregates/empire.level.military")
    Observable<Response<AggregateResult>> getMilitaryAggregate(@Path("game") String game, @Path("empire") String empire);

    @GET("games/{game}/empires/{empire}/aggregates/empire.level.technology")
    Observable<Response<AggregateResult>> getTechnologyAggregate(@Path("game") String game, @Path("empire") String empire);

    @GET("games/{game}/empires/{empire}/aggregates/empire.level.economy")
    Observable<Response<AggregateResult>> getEconomyAggregate(@Path("game") String game, @Path("empire") String empire);

    @GET("games/{game}/empires/{empire}/aggregates/empire.compare.economy")
    Observable<Response<AggregateResult>> getEconomyCompareAggregate(@Path("game") String game, @Path("empire") String empire, @Query("compare") String other);

    @GET("games/{game}/empires/{empire}/aggregates/empire.compare.technology")
    Observable<Response<AggregateResult>> getTechnologyCompareAggregate(@Path("game") String game, @Path("empire") String empire, @Query("compare") String other);

    @GET("games/{game}/empires/{empire}/aggregates/empire.compare.military")
    Observable<Response<AggregateResult>> getMilitaryCompareAggregate(@Path("game") String game, @Path("empire") String empire, @Query("compare") String other);
}
