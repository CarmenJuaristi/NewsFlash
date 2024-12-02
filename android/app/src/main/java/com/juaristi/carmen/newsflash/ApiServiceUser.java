package com.juaristi.carmen.newsflash;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.util.Map;

public interface ApiServiceUser {

    // Cambiamos el tipo de solicitud a Map<String, String> en lugar de una clase personalizada
    @POST("/users")
    Call<UserResponse> createUser(@Body Map<String, String> userData);

    @POST("/sessions")
    Call<LoginResponse> loginUser(@Body Map<String, String> loginData);
}

