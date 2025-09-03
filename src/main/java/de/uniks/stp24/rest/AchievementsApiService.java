package de.uniks.stp24.rest;

import de.uniks.stp24.dto.AchievementDto;
import de.uniks.stp24.dto.AchievementUpdateDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface AchievementsApiService {

    @GET("users/{user}/achievements")
    Observable<Response<List<AchievementDto>>> getAchievementsByUserId(@Path("user") String userId);

    @GET("users/{user}/achievements/{achievement}")
    Observable<Response<List<AchievementDto>>> getAchievementByUserId(@Path("user") String userId, @Path("achievement") String achievementId);

    @PUT("users/{user}/achievements/{achievement}")
    Observable<Response<AchievementDto>> updateAchievementByUserId(@Path("user") String userId, @Path("achievement") String achievementId, @Body AchievementUpdateDto dto);

}
