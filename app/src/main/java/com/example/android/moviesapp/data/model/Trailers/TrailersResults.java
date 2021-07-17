package com.example.android.moviesapp.data.model.Trailers;

import com.google.gson.annotations.SerializedName;

public class TrailersResults {

    /**
     * Initialize variables
     */
    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    /**
     * Getter
     */
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    /**
     * Setter
     */
    public void setId(String id) {
        this.id = id;
    }
}