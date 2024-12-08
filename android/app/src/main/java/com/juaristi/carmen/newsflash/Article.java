package com.juaristi.carmen.newsflash;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "articles")
public class Article  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id; // Puede ser null
    private String author;
    private String content;
    private String description;
    private String publishedAt;
    private String source;
    private String url;
    private String urlToImage;
    private String title;

    // Constructor vacío requerido por Room
    public Article() {
    }

    // Constructor opcional para inicializar los valores
    public Article(Integer id, String author, String content, String description, String publishedAt, String source, String url, String urlToImage, String title) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.description = description;
        this.publishedAt = publishedAt;
        this.source = source;
        this.url = url;
        this.urlToImage = urlToImage;
        this.title = title;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(url, article.url) &&
                Objects.equals(title, article.title) &&
                Objects.equals(publishedAt, article.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, publishedAt);
    }
}

