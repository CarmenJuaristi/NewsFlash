package com.juaristi.carmen.newsflash;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {
    private String status;
    private boolean success;

    private String message;
    @SerializedName("feed")
    private List<Article> feed;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getter y Setter
    public List<Article> getFeed() {
        return feed;
    }

    public void setFeed(List<Article> feed) {
        this.feed = feed;
    }
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
