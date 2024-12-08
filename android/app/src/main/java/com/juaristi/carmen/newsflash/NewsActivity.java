package com.juaristi.carmen.newsflash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class NewsActivity extends AppCompatActivity {
    private NewsViewModel newsViewModel;
    private ActivityNewsBinding binding;
    private NewsRepository newsRepository;
    private NewsViewModelProviderFactory viewModelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar el binding
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar el repositorio y la factory del ViewModel
        newsRepository = new NewsRepository(ArticleDatabase.getInstance(this));
        viewModelProviderFactory = new NewsViewModelProviderFactory(getApplication(), newsRepository);

        // Inicializar el ViewModel
        newsViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel.class);

        // Configurar Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.newsNavHostFragment);
        NavController navController = navHostFragment.getNavController();
        binding.bottomNavigationView.setupWithNavController(navController);
    }
}
