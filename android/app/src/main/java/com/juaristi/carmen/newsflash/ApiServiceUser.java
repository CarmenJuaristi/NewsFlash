package com.juaristi.carmen.newsflash;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServiceUser {

    @POST("/users")
    Call<UserResponse> createUser(@Body UserRequest userRequest);

    @POST("/sessions")
    Call<LoginResponse> loginUser(
            @Body LoginRequest loginRequest
    );

}