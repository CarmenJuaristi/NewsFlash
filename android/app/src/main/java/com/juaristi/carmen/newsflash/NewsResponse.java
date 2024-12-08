package com.juaristi.carmen.newsflash;

import java.util.List;

public class NewsResponse {

    private List<Article> articles; // Cambiado a "articles" para que coincida con los métodos getter y setter.

    // Constructor vacío
    public NewsResponse() {
    }

    // Constructor opcional
    public NewsResponse(List<Article> articles) {
        this.articles = articles;
    }

    // Getter para articles
    public List<Article> getArticles() {
        return articles;
    }

    // Setter para articles
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
