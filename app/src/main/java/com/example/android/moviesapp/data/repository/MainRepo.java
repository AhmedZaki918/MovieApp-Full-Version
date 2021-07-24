package com.example.android.moviesapp.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.moviesapp.data.model.MoviesResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepo {

    // Initialization
    private final MutableLiveData<MoviesResponse> mutableLiveData = new MutableLiveData<>();
    public CompositeDisposable compositeDisposable;


    // Our constructor
    @Inject
    public MainRepo(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }


    public void getResponse(Single<MoviesResponse> observable) {
        // Wrap observable with composite disposable
        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mutableLiveData::setValue));
    }

    public MutableLiveData<MoviesResponse> getMutableLiveData() {
        return mutableLiveData;
    }
}