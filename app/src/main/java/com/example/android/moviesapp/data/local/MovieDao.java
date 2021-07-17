package com.example.android.moviesapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.moviesapp.data.model.AllData;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<AllData>> loadAllResults();

    @Query("SELECT * FROM movie WHERE id = :id")
    Single<AllData> fetchInMovies(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertMovie(AllData allData);

    @Delete
    Completable deleteMovie(AllData allData);

    @Query("Delete FROM movie")
    Completable deleteAll();
}