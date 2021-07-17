package com.example.android.moviesapp.data.model.Trailers;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    /**
     * Initialize variables
     */
    @SerializedName("results")
    private List<TrailersResults> results = null;

    /**
     * Getter
     */
    public List<TrailersResults> getResults() {
        return results;
    }
}