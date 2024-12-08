package com.juaristi.carmen.newsflash;

public class FavoriteRequest {
    private String userId;
    private String newsId;
    private boolean isFavorite;

    public FavoriteRequest(String userId, String newsId, boolean isFavorite) {
        this.userId = userId;
        this.newsId = newsId;
        this.isFavorite = isFavorite;
    }

    // Getters and setters
}

