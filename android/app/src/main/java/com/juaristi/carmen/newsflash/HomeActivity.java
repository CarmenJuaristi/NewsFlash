package com.juaristi.carmen.newsflash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ApiNews apiNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiNews = RetrofitClient.getClient().create(ApiNews.class);

        // Obtener la categoría desde el intent
        String category = getIntent().getStringExtra("category");
        if (category != null) {
            fetchNewsByCategory(category);
        } else {
            Toast.makeText(this, "No category provided.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchNewsByCategory(String category) {
        // Configura el endpoint para obtener noticias
        Call<ApiResponse> call = apiNews.getNews("NEWS_SENTIMENT", "YOUR_API_KEY", category);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> newsList = response.body().getFeed();

                    if (newsList != null && !newsList.isEmpty()) {
                        setupRecyclerView(newsList);
                    } else {
                        Toast.makeText(HomeActivity.this, "No news found for this category.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Error fetching news: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error connecting to API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<News> newsList) {
        adapter = new NewsAdapter(newsList, news -> {
            Intent intent = new Intent(HomeActivity.this, NewsDetailActivity.class);
            intent.putExtra("news_title", news.getTitle());
            intent.putExtra("news_summary", news.getSummary());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
