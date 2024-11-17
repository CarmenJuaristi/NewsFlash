package com.juaristi.carmen.newsflash;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;
public interface ApiNews {
    @GET("query")
    Call<ApiResponse> getNews(
            @Query("function") String function,
            @Query("apikey") String apiKey,
            @Query("tickers") String keyword // Palabra clave o categoría
    );
}
