package com.example.android.moviesapp.ui.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.android.moviesapp.data.model.Details;
import com.example.android.moviesapp.data.model.Reviews.Reviews;
import com.example.android.moviesapp.data.repository.DetailsRepo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class DetailsViewModel extends AndroidViewModel {

    DetailsRepo repo;

    @Inject
    public DetailsViewModel(@NonNull Application application, DetailsRepo repo) {
        super(application);
        this.repo = repo;
    }

    public void initRequest(Single<Details> trailers, Single<Reviews> reviews) {
        repo.getResponse(trailers, reviews);
    }


    // @return mutable live data of trailers
    public MutableLiveData<Details> deliverTrailers() {
        return repo.getTrailersLiveData();
    }

    // @return mutable live data of reviews
    public MutableLiveData<Reviews> deliverReviews() {
        return repo.getReviewsLiveData();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        repo.compositeDisposable.clear();
    }
}
