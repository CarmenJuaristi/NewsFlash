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
    private ApiNews apiNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_trend, container, false);

        recyclerView = view.findViewById(R.id.trendRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiNews = RetrofitClient.getClient().create(ApiNews.class);

        fetchTrendingNews();

        return view;
    }

    private void fetchTrendingNews() {
        Call<ApiResponse> call = apiNews.getTrendingNews("NEWS_SENTIMENT", "YOUR_API_KEY");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> newsList = response.body().getFeed();

                    if (newsList != null && !newsList.isEmpty()) {
                        List<News> top10News = newsList.size() > 10 ? newsList.subList(0, 10) : newsList;
                        setupRecyclerView(top10News);
                    } else {
                        Toast.makeText(getContext(), "No trending news found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error fetching trending news: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error connecting to API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<News> newsList) {
        adapter = new NewsAdapter(newsList, news -> {
            Intent intent = new Intent(getContext(), NewsDetailActivity.class);
            intent.putExtra("news_title", news.getTitle());
            intent.putExtra("news_summary", news.getSummary());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
