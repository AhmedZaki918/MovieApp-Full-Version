package com.example.android.moviesapp.data.model.Reviews;

import com.google.gson.annotations.SerializedName;

public class ReviewsResults {


    // Initialize variables

    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("id")
    private String id;

    // Getter
    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    // Setter
    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }
}