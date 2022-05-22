package com.example.android.moviesapp.ui.details;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.data.local.MovieDao;
import com.example.android.moviesapp.data.model.Details;
import com.example.android.moviesapp.data.model.MoviesResponse;
import com.example.android.moviesapp.data.model.Reviews.ReviewsResults;
import com.example.android.moviesapp.data.network.APIService;
import com.example.android.moviesapp.databinding.ActivityDetailsBinding;
import com.example.android.moviesapp.ui.adapter.ReviewAdapter;
import com.example.android.moviesapp.ui.adapter.TrailerAdapter;
import com.example.android.moviesapp.util.OnAdapterClick;
import com.example.android.moviesapp.util.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class DetailsActivity extends AppCompatActivity implements View.OnClickListener,
        OnAdapterClick {

    // Initialization
    private static final String API_KEY = Constants.Api_key;
    private static final String VIDEOS = "videos";
    private static final String VND = "vnd.youtube:";
    private static final String VIDEO_ID = "VIDEO_ID";
    private DetailsViewModel viewModel;
    private ActivityDetailsBinding binding;
    private String givenPoster;
    private LinearLayoutManager lmTrailer;
    private MoviesResponse moviesResponse;

    @Inject
    APIService apiService;
    @Inject
    MovieDao movieDao;
    @Inject
    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        // Setup view binding
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        checkState();
        createApiRequest();
        updateBasicInfo();
        // Update reviews and trailers
        viewModel.deliverReviews()
                .observe(this, response -> updateReviews(response.getResults()));
        viewModel.deliverTrailers()
                .observe(this, response -> {
                    updateTrailers(response);
                    updateMoreInfo(response);
                });

        binding.cbFavourite.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cb_favourite) {
            if (binding.cbFavourite.isChecked()) {
                // Wrap observable with composite disposable
                compositeDisposable.add(movieDao.insertMovie(moviesResponse)
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> ViewUtils.showSnackBar(binding.scrollView,
                                R.string.movie_added, this)));
            } else {
                // Delete the selected movie by it's position
                compositeDisposable.add(movieDao.deleteMovie(moviesResponse)
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> ViewUtils.showSnackBar(binding.scrollView,
                                R.string.movie_removed)));
            }
        }
    }


    @Override
    public <T> void onItemClick(T data) {
        // Go to the youtube website by Intent
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VND + data))
                .putExtra(VIDEO_ID, data.toString()));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }


    private void initViews() {
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        moviesResponse = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (moviesResponse != null)
            givenPoster = Constants.IMAGE_BASE_URL_ORIGINAL + moviesResponse.getBackdrop();
        lmTrailer = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, true);
    }


    private void createApiRequest() {
        viewModel.initRequest(apiService.getMovieTrailers(moviesResponse.getId(), API_KEY, VIDEOS),
                apiService.getMovieReviews(moviesResponse.getId(), API_KEY));
    }


    private void updateReviews(List<ReviewsResults> response) {
        ReviewAdapter adapter = new ReviewAdapter(response);
        ViewUtils.setupRecyclerView(binding.rvReviews,
                new LinearLayoutManager(this), adapter);
        // If there's no reviews found
        if (adapter.getItemCount() == 0) {
            binding.tvReviewsLabel.setVisibility(GONE);
            binding.tvNoReviews.setVisibility(VISIBLE);
            binding.tvLabelComments.setVisibility(GONE);
        } else {
            binding.tvComments.setText(String.valueOf(adapter.getItemCount()));
        }
    }


    private void updateTrailers(Details response) {
        TrailerAdapter adapter = new TrailerAdapter(response.videos.getResults(), this);
        lmTrailer.setReverseLayout(false);
        ViewUtils.setupRecyclerView(binding.rvTrailer, lmTrailer, adapter);
        // If there's no trailers found
        if (adapter.getItemCount() == 0) {
            binding.tvTrailerLabel.setVisibility(GONE);
            binding.tvNoTrailers.setVisibility(VISIBLE);
            binding.tvSubLabelTrailers.setVisibility(GONE);
        } else {
            binding.tvTrailers.setText(String.valueOf(adapter.getItemCount()));
        }
    }


    private void updateMoreInfo(Details response) {
        binding.tvBudget.setText(ViewUtils.formatNumber((response.getBudget())));
        binding.tvRevenue.setText(ViewUtils.formatNumber(response.getRevenue()));
        binding.tvStatus.setText(response.getStatus());
        binding.tvVoteCount.setText(response.getVoteCount());
        binding.tvPopularity.setText(response.getPopularity());
        binding.tvLanguage.setText(response.getLanguage());
    }


    private void updateBasicInfo() {
        ViewUtils.setupGlide(this, givenPoster, binding.ivPoster);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(moviesResponse.getTitle());
        binding.tvRating.setText(moviesResponse.getUserRating());
        binding.tvOverview.setText(moviesResponse.getOverview());
        binding.tvReleaseDate.setText(moviesResponse.getReleaseDate());
    }


    // Set checkbox true when the movie is saved
    private void checkState() {
        compositeDisposable.add(movieDao.fetchInMovies(moviesResponse.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((allData, throwable) ->
                        binding.cbFavourite.setChecked(allData != null)));
    }
}