package com.juaristi.carmen.newsflash;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class NewsRepository {

    private final ArticleDatabase db;
    private final ExecutorService executorService;

    public NewsRepository(ArticleDatabase db) {
        this.db = db;
        this.executorService = Executors.newSingleThreadExecutor();  // Executor para operaciones en segundo plano
    }

    // Método para obtener los titulares de noticias
    public void getHeadLines(String countryCode, int pageNumber, String apiKey, final Callback<NewsResponse> callback) {
        executorService.execute(() -> {
            try {
                Call<NewsResponse> call = RetrofitClient.getApi().getHeadlines(countryCode, pageNumber, apiKey);

                // Ejecutar asincrónicamente usando enqueue
                call.enqueue(new retrofit2.Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());  // Llamada al callback con el resultado exitoso
                        } else {
                            callback.onError(new Exception("Error en la respuesta: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        callback.onError(new Exception("Error en la solicitud: " + t.getMessage()));
                    }
                });

            } catch (Exception e) {
                callback.onError(e);  // Llamada al callback con el error
            }
        });
    }

    // Método para buscar noticias
    public void searchNews(String searchQuery, int pageNumber, String apiKey, final Callback<NewsResponse> callback) {
        executorService.execute(() -> {
            try {
                Call<NewsResponse> call = RetrofitClient.getApi().searchForNews(searchQuery, pageNumber, apiKey);

                // Ejecutar asincrónicamente usando enqueue
                call.enqueue(new retrofit2.Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess(response.body());  // Llamada al callback con el resultado exitoso
                        } else {
                            callback.onError(new Exception("Error en la respuesta: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        callback.onError(new Exception("Error en la solicitud: " + t.getMessage()));
                    }
                });
            } catch (Exception e) {
                callback.onError(e);  // Llamada al callback con el error
            }
        });
    }

    // Método para insertar o actualizar un artículo
    public void upsert(Article article, final Callback<Long> callback) {
        executorService.execute(() -> {
            try {
                long result = db.getArticleDao().upsert(article);
                callback.onSuccess(result);  // Retornar el resultado como un long
            } catch (Exception e) {
                callback.onError(e);  // Llamada al callback con el error
            }
        });
    }

    // Método para obtener todas las noticias favoritas
    public LiveData<List<Article>> getFavoriteNews() {
        return db.getArticleDao().getAllArticles();
    }

    // Método para eliminar un artículo
    public void deleteArticle(Article article, final Callback<Void> callback) {
        executorService.execute(() -> {
            try {
                db.getArticleDao().deleteArticle(article);
                callback.onSuccess(null);  // Llamada al callback con el resultado
            } catch (Exception e) {
                callback.onError(e);  // Llamada al callback con el error
            }
        });
    }

    // Interfaz Callback para manejar el resultado
    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }
}
