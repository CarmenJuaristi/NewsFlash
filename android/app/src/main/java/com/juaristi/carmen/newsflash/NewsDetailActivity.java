package com.juaristi.carmen.newsflash;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        TextView title = findViewById(R.id.news_detail_title);
        TextView summary = findViewById(R.id.news_detail_summary);
        TextView category = findViewById(R.id.news_detail_category);
        TextView ticker = findViewById(R.id.news_detail_ticker);

        // Obtener los datos pasados desde el Intent
        title.setText(getIntent().getStringExtra("news_title"));
        summary.setText(getIntent().getStringExtra("news_summary"));
        category.setText(getIntent().getStringExtra("news_category"));
        ticker.setText(getIntent().getStringExtra("news_ticker"));
    }
}

