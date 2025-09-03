package de.uniks.stp24.rest;

import de.uniks.stp24.dto.CreateJobDto;
import de.uniks.stp24.dto.JobDto;
import de.uniks.stp24.dto.UpdateJobDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface JobApiService {
    @GET("games/{game}/empires/{empire}/jobs")
    Observable<Response<List<JobDto>>> getJobList(@Path("game") String game, @Path("empire") String empire);
    @POST("games/{game}/empires/{empire}/jobs")
    Observable<Response<JobDto>> createJob(@Path("game") String game, @Path("empire") String empire, @Body CreateJobDto dto);

    @PATCH("games/{game}/empires/{empire}/jobs/{id}")
    Observable<Response<JobDto>> updateJob(@Path("game") String game, @Path("empire") String empire, @Path("id") String id, @Body UpdateJobDto dto);

    @DELETE("games/{game}/empires/{empire}/jobs/{id}")
    Observable<Response<JobDto>> deleteJob(@Path("game") String game, @Path("empire") String empire, @Path("id") String id);
}
