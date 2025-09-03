package de.uniks.stp24.rest;

import de.uniks.stp24.dto.ShipDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface ShipApiService {
    @GET("games/{game}/fleets/{fleet}/ships")
    Observable<List<ShipDto>> getShips(@Path("game") String game, @Path("fleet") String fleet);
    @GET("games/{game}/fleets/{fleet}/ships/{id}")
    Observable<ShipDto> getShipById(@Path("game") String game, @Path("fleet") String fleet, @Path("id") String id);
    @PATCH("games/{game}/fleets/{fleet}/ships/{id}")
    Observable<ShipDto> updateShip(@Path("game") String game, @Path("fleet") String fleet, @Path("id") String id, @Body ShipDto ship);
    @DELETE("games/{game}/fleets/{fleet}/ships/{id}")
    Observable<Void> deleteShip(@Path("game") String game, @Path("fleet") String fleet, @Path("id") String id);
}