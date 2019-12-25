package com.example.android.moviesapp.model.Trailers;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {


    @SerializedName("results")
    private List<Results> results = null;


    public List<Results> getResults() {
//        Log.e("Videos","Trailers = " + results);
        return results;
    }
}
