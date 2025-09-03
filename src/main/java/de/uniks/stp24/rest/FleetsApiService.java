package de.uniks.stp24.rest;

import de.uniks.stp24.dto.CreateFleetDto;
import de.uniks.stp24.dto.FleetDto;
import de.uniks.stp24.dto.UpdateFleetDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface FleetsApiService {
    @POST("games/{game}/fleets")
    Observable<FleetDto> createFleet(@Path("game") String game, @Body CreateFleetDto fleet);
    @GET("games/{game}/fleets")
    Observable<List<FleetDto>> getFleets(@Path("game") String game);
    @GET("games/{game}/fleets/{id}")
    Observable<FleetDto> getFleetById(@Path("game") String game, @Path("id") String id);
    @PATCH("games/{game}/fleets/{id}")
    Observable<FleetDto> updateFleet(@Path("game") String game, @Path("id") String id, @Body UpdateFleetDto fleet);
    @DELETE("games/{game}/fleets/{id}")
    Observable<Void> deleteFleet(@Path("game") String game, @Path("id") String id);
}
