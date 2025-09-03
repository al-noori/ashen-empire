package de.uniks.stp24.rest;

import de.uniks.stp24.dto.*;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;

public interface PresetsApiService {
    @GET("presets/buildings/{id}")
    Observable<BuildingDto> getBuildingInfo(@Path("id") String id);

    @GET("presets/districts")
    Observable<Response<List<DistrictDto>>> getDistricts();

    @GET("presets/buildings")
    Observable<List<BuildingDto>> getBuildings();

    @GET("presets/traits")
    Observable<Response<List<TraitsDto>>> getTraits();

    @GET("presets/technologies")
    Observable<Response<List<TechnologyDto>>> getTechnologies();

    @GET("presets/resources")
    Observable<Map<String, ResourcePresetDto>> getResources();

    @GET("presets/ships")
    Observable<List<PresetShipDto>> getShips();

    @GET("presets/ships/{id}")
    Observable<PresetShipDto> getShip(@Path("id") String id);
}
