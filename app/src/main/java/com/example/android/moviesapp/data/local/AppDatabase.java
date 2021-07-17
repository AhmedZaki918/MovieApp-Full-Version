package com.example.android.moviesapp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.android.moviesapp.data.model.AllData;

@Database(entities = {AllData.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao getMovieDao();
}