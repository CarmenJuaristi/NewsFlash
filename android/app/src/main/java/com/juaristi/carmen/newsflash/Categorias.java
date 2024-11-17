package com.juaristi.carmen.newsflash;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
public class Categorias extends Fragment {
    private static final String API_KEY = "6ICBVA0VZ27P46V0";
    private static final String FUNCTION = "NEWS_SENTIMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        // Hacer la llamada a la API dentro del fragmento
        ApiNews apiService = RetrofitClient.getClient().create(ApiNews.class);

        // Llamada al API con la categoría "sports"
        Call<ApiResponse> call = apiService.getNews(FUNCTION, API_KEY, "sports");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Noticias> newsList = response.body().getFeed();
                    for (Noticias noticias : newsList) {
                        Log.d("News", "Title: " + noticias.getTitle());
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch news", t);
            }
        });

        return view;
    }
}
