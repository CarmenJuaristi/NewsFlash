package com.juaristi.carmen.newsflash;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v2/top-headlines")
    Call<NewsResponse> getHeadlines(
            @Query("country") String countryCode,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );

    @GET("v2/everything")
    Call<NewsResponse> searchForNews(
            @Query("q") String searchQuery,
            @Query("page") int pageNumber,
            @Query("apiKey") String apiKey
    );

}



