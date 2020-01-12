package com.example.android.moviesapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.moviesapp.model.AllData;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<AllData>> loadAllResults();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(AllData allData);

    @Delete
    void deleteMovie(AllData allData);

    @Query("Delete FROM movie")
    void deleteAll();
}