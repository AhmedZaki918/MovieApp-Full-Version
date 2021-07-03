package com.example.android.moviesapp.di;


import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.network.APIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {


    @Provides
    @Singleton
    public Retrofit provideClient() {
        return new Retrofit.Builder().baseUrl(Constants.MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    public APIService provideService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }


    @Provides
    @Singleton
    public CompositeDisposable providesDisposable() {
        return new CompositeDisposable();
    }
}
