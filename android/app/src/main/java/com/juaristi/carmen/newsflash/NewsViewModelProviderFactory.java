package com.juaristi.carmen.newsflash;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsViewModelProviderFactory implements ViewModelProvider.Factory {
    private final Application app;
    private final NewsRepository newsRepository;

    public NewsViewModelProviderFactory(Application app, NewsRepository newsRepository) {
        this.app = app;
        this.newsRepository = newsRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            return (T) new NewsViewModel(app, newsRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

