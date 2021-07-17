package com.example.android.moviesapp.data.model.Reviews;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Reviews {

    /**
     * Initialize variables
     */
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<ReviewsResults> results = null;

    /**
     * Getter
     */
    public int getId() {
        return id;
    }

    public List<ReviewsResults> getResults() {
        return results;
    }

    /**
     * Setter
     */
    public void setId(int id) {
        this.id = id;
    }
}