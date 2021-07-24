package com.example.android.moviesapp.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.moviesapp.data.model.Details;
import com.example.android.moviesapp.data.model.Reviews.Reviews;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsRepo {

    private final MutableLiveData<Details> trailersLiveData = new MutableLiveData<>();
    private final MutableLiveData<Reviews> ReviewsLiveData = new MutableLiveData<>();
    public CompositeDisposable compositeDisposable;


    @Inject
    public DetailsRepo(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }


    public void getResponse(Single<Details> trailers, Single<Reviews> reviews) {
        // Wrap trailers with composite disposable
        compositeDisposable.add(trailers
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailersLiveData::setValue));

        // Wrap reviews with composite disposable
        compositeDisposable.add(reviews
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ReviewsLiveData::setValue));
    }


    public MutableLiveData<Details> getTrailersLiveData() {
        return trailersLiveData;
    }

    public MutableLiveData<Reviews> getReviewsLiveData() {
        return ReviewsLiveData;
    }
}