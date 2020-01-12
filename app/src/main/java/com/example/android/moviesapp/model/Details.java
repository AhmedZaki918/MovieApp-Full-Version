package com.example.android.moviesapp.model;


import com.example.android.moviesapp.model.Trailers.Videos;
import com.google.gson.annotations.SerializedName;


public class Details {

    /**
     * Initialize variables
     */
    @SerializedName("videos")
    public Videos videos;

    @SerializedName("budget")
    private String budget;

    @SerializedName("revenue")
    private String revenue;

    @SerializedName("status")
    private String status;

    @SerializedName("vote_count")
    private String voteCount;

    @SerializedName("original_language")
    private String language;

    @SerializedName("popularity")
    private String popularity;

    /**
     * Getter
     */
    // Get the popularity
    public String getPopularity() {
        return popularity;
    }

    // Get the language
    public String getLanguage() {
        return language;
    }

    // Get the vote count
    public String getVoteCount() {
        return voteCount;
    }

    // Get the revenue
    public String getRevenue() {
        return revenue;
    }

    // Get the status
    public String getStatus() {
        return status;
    }

    // Get the budget
    public String getBudget() {
        return budget;
    }
}