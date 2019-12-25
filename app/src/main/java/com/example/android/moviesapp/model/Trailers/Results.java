package com.example.android.moviesapp.model.Trailers;

import com.google.gson.annotations.SerializedName;

public class Results {

    // Initialize variables
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setId(String id) {
        this.id = id;
    }
}