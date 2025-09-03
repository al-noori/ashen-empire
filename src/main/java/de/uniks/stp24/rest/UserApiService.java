package de.uniks.stp24.rest;

import de.uniks.stp24.dto.CreateUserDto;
import de.uniks.stp24.dto.UpdateUserDto;
import de.uniks.stp24.dto.UserDto;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface UserApiService {
    @POST("users")
    Observable<UserDto> createUser(@Body CreateUserDto dto);

    @GET("users")
    Observable<List<UserDto>> findAll();

    @GET("users")
    Observable<List<UserDto>> findAll(@Query("ids") String ids);

    @GET("users/{id}")
    Observable<UserDto> findOne(@Path("id") String id);

    @PATCH("users/{id}")
    Observable<UserDto> update(@Path("id") String id, @Body UpdateUserDto dto);

    @DELETE("users/{id}")
    Observable<UserDto> delete(@Path("id") String id);

}
