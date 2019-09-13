package com.example.android.moviesapp.model.Trailers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers {

    // Initialize variables
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<DetailsTrailer> details = null;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public List<DetailsTrailer> getResults() {
        return details;
    }

    public void setId(int id) {
        this.id = id;
    }
}