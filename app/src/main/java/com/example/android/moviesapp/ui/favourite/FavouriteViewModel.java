package com.example.android.moviesapp.ui.favourite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.moviesapp.data.local.MovieDao;
import com.example.android.moviesapp.data.model.MoviesResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavouriteViewModel extends AndroidViewModel {

    /**
     * Wrapping the <list<MoviesResponse> with LiveData
     * to avoid requiring the data every time
     **/
    private final LiveData<List<MoviesResponse>> mAllDataList;
    MovieDao movieDao;

    @Inject
    public FavouriteViewModel(@NonNull Application application, MovieDao movieDao) {
        super(application);
        this.movieDao = movieDao;
        mAllDataList = movieDao.loadAllResults();
    }

    public LiveData<List<MoviesResponse>> getAllDataList() {
        return mAllDataList;
    }
}