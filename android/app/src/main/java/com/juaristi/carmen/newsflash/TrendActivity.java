package com.juaristi.carmen.newsflash;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class TrendActivity extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ApiService apiService;

    private static final String API_KEY = "YOUR_API_KEY";  // Asegúrate de reemplazarla con tu clave de API de Alpha Vantage

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_trend, container, false);

        recyclerView = view.findViewById(R.id.trendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configuración del cliente de Retrofit para noticias
        apiService = RetrofitClient.getNewsClient().create(ApiService.class);

        // Cargar noticias en tendencia
        fetchTrendingNews();

        return view;
    }

    private void fetchTrendingNews() {
        String function = "NEWS_SENTIMENT"; // Ajustar según tu API
        String apiKey = API_KEY; // Usar la clave de la API aquí

        // Llamada a la API para obtener noticias en tendencia
        Call<ApiResponse> call = apiService.getTrendingNews(function, apiKey);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtener las noticias desde la respuesta de la API
                    List<Article> articleList = response.body().getFeed();

                    if (articleList != null && !articleList.isEmpty()) {
                        // Limitar a las 10 mejores noticias
                        List<Article> top10News = articleList.size() > 10 ? articleList.subList(0, 10) : articleList;
                        setupRecyclerView(top10News);
                    } else {
                        Toast.makeText(getContext(), "No trending news found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar códigos de error
                    Toast.makeText(getContext(), "Error fetching trending news: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Manejar errores de conexión o tiempo de espera
                Toast.makeText(getContext(), "Error connecting to API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Article> articleList) {
        adapter = new NewsAdapter(articleList, news -> {
            // Navegar a NewsDetailActivity al hacer clic en un ítem
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra("news_title", news.getTitle());
            intent.putExtra("news_summary", news.getSummary());
            intent.putExtra("news_category", news.getCategory());
            intent.putExtra("news_ticker", news.getTicker());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}

