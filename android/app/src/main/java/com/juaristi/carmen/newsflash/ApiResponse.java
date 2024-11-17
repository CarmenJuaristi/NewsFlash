package com.juaristi.carmen.newsflash;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {

    @SerializedName("feed")
    private List<Noticias> feed;

    // Getter y Setter
    public List<Noticias> getFeed() {
        return feed;
    }

    public void setFeed(List<Noticias> feed) {
        this.feed = feed;
    }
}
