package com.example.android.moviesapp.network;


import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.model.Reviews.Reviews;
import com.example.android.moviesapp.model.Details;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("movie/popular")
    Call<AllData> get_popular(@Query("api_key") String key);

    @GET("movie/top_rated")
    Call<AllData> get_top_rated(@Query("api_key") String key);

    @GET("movie/now_playing")
    Call<AllData> get_now_playing(@Query("api_key") String key);

    @GET("movie/{id}")
    Call<Details> get_Movie_Trailers(@Path("id") int id, @Query("api_key") String key,
                                     @Query("append_to_response") String videos);

    @GET("movie/{id}/reviews")
    Call<Reviews> get_Movie_Reviews(@Path("id") int id, @Query("api_key") String key);
}