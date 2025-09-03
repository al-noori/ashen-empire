package de.uniks.stp24.rest;

import de.uniks.stp24.dto.AggregateResult;
import de.uniks.stp24.dto.FractionDto;
import de.uniks.stp24.dto.SystemUpgradeResponseDto;
import de.uniks.stp24.dto.UpdateFractionDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface GameSystemsApiService {
    @GET("games/{game}/systems")
    Observable<Response<List<FractionDto>>> getAllFractions(@Path("game") String game);

    @GET("games/{game}/systems")
    Observable<Response<List<FractionDto>>> getFractionsFromUser(@Path("game") String game, @Query("owner") String owner);

    @PATCH("games/{game}/systems/{id}")
    Observable<Response<FractionDto>> updateFraction(@Path("game") String game, @Path("id") String id, @Body UpdateFractionDto dto);

    @GET("presets/system-upgrades")
    Observable<SystemUpgradeResponseDto> getFractionUpgrades();

    @GET("games/{game}/empires/{empire}/aggregates/system.max_health")
    Observable<Response<AggregateResult>> getFractionMaxHealth(@Path("game") String game, @Path("empire") String empire, @Query("system") String fraction);

    @GET("games/{game}/empires/{empire}/aggregates/system.defense")
    Observable<Response<AggregateResult>> getFractionDefense(@Path("game") String game, @Path("empire") String empire, @Query("system") String fraction);
}
