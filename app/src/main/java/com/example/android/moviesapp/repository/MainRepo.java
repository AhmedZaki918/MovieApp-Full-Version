package com.example.android.moviesapp.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.moviesapp.model.AllData;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepo {

    // Initialization
    private final MutableLiveData<AllData> mutableLiveData = new MutableLiveData<>();
    public CompositeDisposable compositeDisposable;


    // Our constructor
    @Inject
    public MainRepo(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }


    public void getResponse(Single<AllData> endPoint) {
        Single<AllData> observable = endPoint
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        // Wrap observable with composite disposable
        compositeDisposable.add(observable.subscribe(mutableLiveData::setValue));
    }


    public MutableLiveData<AllData> getMutableLiveData() {
        return mutableLiveData;
    }
}