package com.example.android.moviesapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.moviesapp.model.MovieData;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieData>> loadAllResults();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieData movieData);

    @Delete
    void deleteMovie(MovieData movieData);
}