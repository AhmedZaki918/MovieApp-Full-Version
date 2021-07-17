package com.example.android.moviesapp.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.data.repository.MainRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    MainRepo repo;

    // Constructor for our class
    @Inject
    public MainViewModel(@NonNull Application application, MainRepo repo) {
        super(application);
        this.repo = repo;
    }

    // Init get request of movies
    public void initRequest(Single<AllData> endPoint) {
        repo.getResponse(endPoint);
    }

    // @return mutable live data of movies
    public MutableLiveData<AllData> deliverResponse() {
        return repo.getMutableLiveData();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        repo.compositeDisposable.clear();
    }
}
