package com.juaristi.carmen.newsflash;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {

    @SerializedName("feed")
    private List<News> feed;

    // Getter y Setter
    public List<News> getFeed() {
        return feed;
    }

    public void setFeed(List<News> feed) {
        this.feed = feed;
    }
}
