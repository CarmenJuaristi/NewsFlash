package com.juaristi.carmen.newsflash;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {

    private final NewsRepository newsRepository;

    private MutableLiveData<Resource<NewsResponse>> headlines = new MutableLiveData<>();
    private int headlinesPage = 1;
    private NewsResponse headlinesResponse = null;

    private MutableLiveData<Resource<NewsResponse>> searchNews = new MutableLiveData<>();
    private int searchNewsPage = 1;
    private NewsResponse searchNewsResponse = null;
    private String newSearchQuery = null;
    private String oldSearchQuery = null;

    public NewsViewModel(Application app, NewsRepository repository) {
        super(app);
        this.newsRepository = repository;

        // Obtener titulares al inicializar
        getHeadlines("es");
    }

    public void getHeadlines(String countryCode) {
        // Ejecuta en segundo plano
        AsyncTask.execute(() -> fetchHeadlinesFromInternet(countryCode));
    }

    public void searchNews(String searchQuery) {
        // Ejecuta en segundo plano
        AsyncTask.execute(() -> fetchSearchNewsFromInternet(searchQuery));
    }

    private Resource<NewsResponse> handleHeadlinesResponse(Response<NewsResponse> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                headlinesPage++;
                NewsResponse resultResponse = response.body();
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse;
                } else {
                    List<Article> oldArticles = headlinesResponse.getArticles();
                    List<Article> newArticles = resultResponse.getArticles();
                    if (oldArticles == null) oldArticles = new ArrayList<>();
                    oldArticles.addAll(newArticles);
                }
                return new Resource.Success(headlinesResponse != null ? headlinesResponse : resultResponse);
            }
        }
        return new Resource.Error(response.message());
    }

    private Resource<NewsResponse> handleSearchNewsResponse(Response<NewsResponse> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                searchNewsPage++;
                NewsResponse resultResponse = response.body();
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse;
                } else {
                    List<Article> oldArticles = searchNewsResponse.getArticles();
                    List<Article> newArticles = resultResponse.getArticles();
                    if (oldArticles == null) oldArticles = new ArrayList<>();
                    oldArticles.addAll(newArticles);
                }
                return new Resource.Success(searchNewsResponse != null ? searchNewsResponse : resultResponse);
            }
        }
        return new Resource.Error(response.message());
    }

    public void addToFavourites(Article article) {
        AsyncTask.execute(() -> newsRepository.upsert(article));
    }

    public LiveData<List<Article>> getFavouriteNews() {
        return newsRepository.getFavouriteNews();
    }

    public void deleteArticle(Article article) {
        AsyncTask.execute(() -> newsRepository.deleteArticle(article));
    }

    private boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true;
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true;
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true;
            }
        }
        return false;
    }

    private void fetchHeadlinesFromInternet(String countryCode) {
        headlines.postValue(new Resource.Loading());
        try {
            if (isInternetConnected(getApplication())) {
                Response<NewsResponse> response = newsRepository.getHeadlines(countryCode, headlinesPage);
                headlines.postValue(handleHeadlinesResponse(response));
            } else {
                headlines.postValue(new Resource.Error("No internet connection"));
            }
        } catch (Throwable t) {
            if (t instanceof IOException) {
                headlines.postValue(new Resource.Error("Unable to connect"));
            } else {
                headlines.postValue(new Resource.Error("Unexpected error occurred"));
            }
        }
    }

    private void fetchSearchNewsFromInternet(String searchQuery) {
        newSearchQuery = searchQuery;
        searchNews.postValue(new Resource.Loading());
        try {
            if (isInternetConnected(getApplication())) {
                Response<NewsResponse> response = newsRepository.searchNews(searchQuery, searchNewsPage);
                searchNews.postValue(handleSearchNewsResponse(response));
            } else {
                searchNews.postValue(new Resource.Error("No internet connection"));
            }
        } catch (Throwable t) {
            if (t instanceof IOException) {
                searchNews.postValue(new Resource.Error("Unable to connect"));
            } else {
                searchNews.postValue(new Resource.Error("Unexpected error occurred"));
            }
        }
    }

    public MutableLiveData<Resource<NewsResponse>> getHeadlinesLiveData() {
        return headlines;
    }

    public MutableLiveData<Resource<NewsResponse>> getSearchNewsLiveData() {
        return searchNews;
    }
}
