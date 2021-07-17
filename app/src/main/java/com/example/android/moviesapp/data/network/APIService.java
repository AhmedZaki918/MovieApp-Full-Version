package com.example.android.moviesapp.data.network;


import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.data.model.Details;
import com.example.android.moviesapp.data.model.Reviews.Reviews;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("movie/popular")
    Single<AllData> getPopular(@Query("api_key") String key);

    @GET("movie/top_rated")
    Single<AllData> getTopRated(@Query("api_key") String key);

    @GET("movie/now_playing")
    Single<AllData> getNowPlaying(@Query("api_key") String key);

    @GET("movie/{id}")
    Single<Details> getMovieTrailers(@Path("id") int id, @Query("api_key") String key,
                                     @Query("append_to_response") String videos);

    @GET("movie/{id}/reviews")
    Single<Reviews> getMovieReviews(@Path("id") int id, @Query("api_key") String key);
}