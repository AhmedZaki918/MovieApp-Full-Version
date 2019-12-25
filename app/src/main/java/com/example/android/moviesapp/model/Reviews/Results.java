package com.example.android.moviesapp.model.Reviews;

import com.google.gson.annotations.SerializedName;

public class Results {

    // Initialize variables
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("id")
    private String id;

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }
}