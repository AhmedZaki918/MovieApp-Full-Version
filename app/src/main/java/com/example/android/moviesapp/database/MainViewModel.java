package com.example.android.moviesapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.moviesapp.model.MovieData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    /**
     * Wrapping the <list<MovieData> with LiveData
     * to avoid requiring the data every time
     **/
    private LiveData<List<MovieData>> moviesData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase dataBase = AppDatabase.getInstance(this.getApplication());
        moviesData = dataBase.movieDao().loadAllResults();
    }

    public LiveData<List<MovieData>> getMoviesData() {
        return moviesData;
    }
}