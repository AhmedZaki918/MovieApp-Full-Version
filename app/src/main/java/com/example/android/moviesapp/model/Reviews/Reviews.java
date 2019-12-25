package com.example.android.moviesapp.model.Reviews;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {

    // Initialize variables
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Results> results = null;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setId(int id) {
        this.id = id;
    }
}