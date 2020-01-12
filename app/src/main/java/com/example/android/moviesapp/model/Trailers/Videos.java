package com.example.android.moviesapp.model.Trailers;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    /**
     * Initialize variables
     */
    @SerializedName("results")
    private List<Results> results = null;

    /**
     * Getter
     */
    public List<Results> getResults() {
        return results;
    }
}