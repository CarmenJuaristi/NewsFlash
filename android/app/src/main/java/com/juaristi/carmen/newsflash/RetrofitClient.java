package com.juaristi.carmen.newsflash;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://www.alphavantage.co/";  // URL base de Alpha Vantage
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Configuramos la base URL para todas las peticiones
                    .addConverterFactory(GsonConverterFactory.create())  // Convertimos JSON a objetos de Java
                    .build();
        }
        return retrofit;
    }
}
