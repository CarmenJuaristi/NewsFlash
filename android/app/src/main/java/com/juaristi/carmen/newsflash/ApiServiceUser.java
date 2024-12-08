package com.juaristi.carmen.newsflash;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ApiServiceUser {

    @POST("api/register/")
    Call<UserResponse> createUser(@Body UserRequest userRequest);
    @POST("api/login/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("delete/{email}/")
    Call<ApiResponse> deleteUser(@Path("email") String email);
    // Método para obtener los datos del usuario
    @GET("user/") // Ajusta el endpoint según tu API
    Call<UserResponse> getUserData();
    // Método para actualizar los datos del usuario
    @PUT("user/") // Ajusta el endpoint según tu API
    Call<Void> updateUser(@Body Map<String, String> updates); // Aquí enviamos un Map con los datos a actualizar

}


