package com.juaristi.carmen.newsflash;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiNews {
    @GET("query") // Endpoint base para las consultas
    Call<ApiResponse> getNews(
            @Query("function") String function,   // Especifica la función (por ejemplo, "NEWS")
            @Query("apikey") String apiKey,       // La clave de API
            @Query("tickers") String keyword      // Filtro por palabra clave (opcional)
    );
}

