package com.example.android.moviesapp.data.di;


import android.content.Context;

import androidx.room.Room;

import com.example.android.moviesapp.data.local.AppDatabase;
import com.example.android.moviesapp.data.local.MovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomModule {


    @Provides
    @Singleton
    public AppDatabase providesAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "allmovies")
                .fallbackToDestructiveMigration().build();
    }

    @Provides
    public MovieDao providesMovieDao(AppDatabase db) {
        return db.getMovieDao();
    }
}