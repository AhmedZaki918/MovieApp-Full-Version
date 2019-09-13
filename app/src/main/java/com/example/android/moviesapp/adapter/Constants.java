package com.example.android.moviesapp.adapter;

import com.example.android.moviesapp.BuildConfig;

// Class hold all Constants to use it from one place
public final class Constants {

    // Key to use it in putExtra method by Intent class
    public static final String SOURCE = "source";

    // Base url of the website
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    // Create a full url instead of the given url path to show the poster of the movie
    private static final String URL_HEAD = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w500/";
    public static final String IMAGE_BASE_URL = URL_HEAD + SIZE;

    // Api key
    public static final String Api_key = BuildConfig.THE_MOVIEDB_API_KEY;
}