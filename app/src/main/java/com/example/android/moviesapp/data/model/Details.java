package com.example.android.moviesapp.data.model;


import com.example.android.moviesapp.data.model.Trailers.Videos;
import com.google.gson.annotations.SerializedName;


public class Details {

    // Initialization
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


    // Getter
    public String getPopularity() {
        return popularity;
    }

    public String getLanguage() {
        return language;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getStatus() {
        return status;
    }

    public String getBudget() {
        return budget;
    }
}