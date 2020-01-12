package com.example.android.moviesapp.network;

import com.example.android.moviesapp.adapter.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = Constants.MOVIE_BASE_URL;
    private static com.example.android.moviesapp.network.APIClient mInstance;
    private Retrofit retrofit;

    @SuppressWarnings("WeakerAccess")
    public APIClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized APIClient getInstance() {
        if (mInstance == null) {
            mInstance = new APIClient();
        }
        return mInstance;
    }

    public APIInterface getApi() {
        return retrofit.create(APIInterface.class);
    }
}