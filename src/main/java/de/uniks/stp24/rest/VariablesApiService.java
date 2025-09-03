package de.uniks.stp24.rest;

import de.uniks.stp24.dto.VariableDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface VariablesApiService {
    @GET("games/{game}/empires/{empire}/variables")
    Observable<List<VariableDto>> getVariables(@Path("game") String game, @Path("empire") String empire, @Query("variables") List<String> variables);
}
