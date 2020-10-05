package com.example.android.moviesapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.adapter.ReviewAdapter;
import com.example.android.moviesapp.adapter.TrailerAdapter;
import com.example.android.moviesapp.database.AppDatabase;
import com.example.android.moviesapp.database.AppExecutors;
import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.model.Reviews.Results;
import com.example.android.moviesapp.model.Reviews.Reviews;
import com.example.android.moviesapp.model.Details;
import com.example.android.moviesapp.network.APIClient;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Double.parseDouble;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster)
    ImageView ivPoster;
    @BindView(R.id.tv_rating)
    TextView tvUserRating;
    @BindView(R.id.tv_summary)
    ReadMoreTextView rmOverview;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.cb_favourite)
    CheckBox cbSave;
    @BindView(R.id.tv_trailer_label)
    TextView tvTrailerLabel;
    @BindView(R.id.rv_trailer)
    RecyclerView rvTrailers;
    @BindView(R.id.tv_reviews_label)
    TextView tvReviewsLabel;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.tv_revenue)
    TextView tvRevenue;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_budget)
    TextView tvBudget;
    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;
    @BindView(R.id.tv_popularity)
    TextView tvPopularity;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_no_reviews)
    TextView tvNoReviews;
    @BindView(R.id.tv_no_trailers)
    TextView tvNoTrailers;
    @BindView(R.id.tv_comments)
    TextView tvCommentsNumbers;
    @BindView(R.id.tv_trailers)
    TextView tvTrailersNumbers;
    @BindView(R.id.tv_label_comments)
    TextView tvLabelComments;
    @BindView(R.id.tv_sub_label_trailers)
    TextView tvSubLabelTrailers;
    @BindView(R.id.scroll_view)
    ScrollView sv;

    // String variables to store the given value passed by intent
    String givenPoster;
    String givenTitle;
    String givenRating;
    String givenOverview;
    String givenDate;

    // Initialize RecyclerView, Adapter, Api key and Model classes
    private RecyclerView mRvTrailer;
    private RecyclerView mRvReview;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private List<com.example.android.moviesapp.model.Trailers.Results> mTrailersList;
    private List<Results> mReviewsList;
    private String apiKey = Constants.Api_key;

    // Obj from model class
    private AllData mAllData;
    // Member variable for the Database
    private AppDatabase mDb;

    // variables for SharedPreferences
    SharedPreferences StatePreferences;
    private SharedPreferences.Editor mStatePrefsEditor;
    public Boolean State;
    public int movId;

    // Action Bar
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        //  Find a reference to ActionBar
        actionBar = getSupportActionBar();

        // Find a reference to AppDatabase class
        mDb = AppDatabase.getInstance(getApplicationContext());

        // Prepare the intent to use it
        Intent intent = getIntent();
        // Get the data from the model class
        mAllData = intent.getParcelableExtra(Constants.EXTRA_DATA);

        // Get all fields we need to show them in that activity By Intent
        assert mAllData != null;
        givenPoster = Constants.IMAGE_BASE_URL_ORIGINAL + mAllData.getBackdrop();
        givenTitle = mAllData.getTitle();
        givenRating = mAllData.getUserRating();
        givenOverview = mAllData.getOverview();
        givenDate = mAllData.getReleaseDate();


        // Display the poster of the selected movie By Picasso library
        Picasso.get()
                .load(givenPoster)
                .into(ivPoster);

        // Pass the given text by intent and display it in TextView
        actionBar.setTitle(givenTitle);
        tvUserRating.setText(givenRating);
        rmOverview.setText(givenOverview);
        tvReleaseDate.setText(givenDate);

        // Setup RecyclerView for trailers
        mRvTrailer = findViewById(R.id.rv_trailer);
        LinearLayoutManager lmTrailer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        lmTrailer.setReverseLayout(false);
        mRvTrailer.setLayoutManager(lmTrailer);
        mRvTrailer.setHasFixedSize(true);
        mTrailersList = new ArrayList<>();


        // Setup RecyclerView for reviews
        mRvReview = findViewById(R.id.rv_reviews);
        LinearLayoutManager lmReview = new LinearLayoutManager(this);
        mRvReview.setLayoutManager(lmReview);
        mRvReview.setHasFixedSize(true);
        mTrailersList = new ArrayList<>();

        // Display the data related of trailers and reviews by getId method in model class
        displayTrailersAndInfo(mAllData.getId());
        displayReviews(mAllData.getId());

        // To save the state of CheckBox Favourite button (Check And UnChecked)
        StatePreferences = getSharedPreferences("ChkPrefs", MODE_PRIVATE);
        mStatePrefsEditor = StatePreferences.edit();
        State = StatePreferences.getBoolean("CheckState", false);
        movId = StatePreferences.getInt("MovieId", 0);
        if (State && movId == mAllData.getId()) {
            cbSave.setChecked(true);
        }

        // Save the movie to the database
        cbSave.setOnClickListener(view -> {

            // If checked
            if (cbSave.isChecked()) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    // Insert the selected movie to the database
                    mDb.movieDao().insertMovie(mAllData);
                });
                // Save the id of the movie in shared preferences
                mStatePrefsEditor.putBoolean("CheckState", true);
                mStatePrefsEditor.putInt("MovieId", mAllData.getId());
                mStatePrefsEditor.apply();

                // SnackBar
                Snackbar snackbar =
                        Snackbar.make(sv, R.string.movie_added, Snackbar.LENGTH_LONG)
                                .setAction(R.string.see_list, view1 -> {
                                    Intent intent1 = new Intent(DetailsActivity.this, FavouriteActivity.class);
                                    startActivity(intent1);
                                }).setActionTextColor(Color.CYAN);
                snackbar.show();

                // If not checked
            } else {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    // Delete the selected movie by it's position
                    mDb.movieDao().deleteMovie(mAllData);
                });
                // Delete the id of the movie in shared preferences
                mStatePrefsEditor.putBoolean("CheckState", false);
                mStatePrefsEditor.putInt("MovieId", 0);
                mStatePrefsEditor.apply();

                // SnackBar
                Snackbar snackbar =
                        Snackbar.make(sv, R.string.movie_removed, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    /**
     * Get the trailers by Retrofit library
     *
     * @param id refer to getId method in model class.
     */
    public void displayTrailersAndInfo(int id) {

        Call<Details> call = APIClient.getInstance().getApi().get_Movie_Trailers(id, apiKey, "videos");
        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(@NonNull Call<Details> call, @NonNull Response<Details> response) {

                if (response.body() != null) {
                    mTrailersList = response.body().videos.getResults();

                    // Display the budget of the movie
                    String budgetValue = response.body().getBudget();
                    budgetValue = formatNumber(parseDouble(budgetValue));
                    tvBudget.setText(String.format("%s$", budgetValue));

                    // Display the revenue of the movie
                    String revenueValue = response.body().getRevenue();
                    revenueValue = formatNumber(parseDouble(revenueValue));
                    tvRevenue.setText(String.format("%s$", revenueValue));

                    // Display the remaining views
                    tvStatus.setText(response.body().getStatus());
                    tvVoteCount.setText(response.body().getVoteCount());
                    tvPopularity.setText(response.body().getPopularity());
                    tvLanguage.setText(response.body().getLanguage());
                }
                // Initialize the adapter
                mTrailerAdapter = new TrailerAdapter(DetailsActivity.this, mTrailersList);

                // Get the number of trailers in adapter
                int numOfComments = mTrailerAdapter.getItemCount();

                // If there's no trailers found, notify the user via text
                if (mTrailerAdapter.getItemCount() == 0) {
                    tvTrailerLabel.setVisibility(View.GONE);
                    tvNoTrailers.setVisibility(View.VISIBLE);
                    tvSubLabelTrailers.setVisibility(View.GONE);

                    // If there's trailers available
                } else {
                    // If there's one trailer only, write trailers without "S"
                    if (mTrailerAdapter.getItemCount() == 1) {
                        tvTrailersNumbers.setText(String.valueOf(numOfComments));
                        tvSubLabelTrailers.setText(R.string.one_trailer);

                        // Write trailers with "S"
                    } else {
                        tvTrailersNumbers.setText(String.valueOf(numOfComments));
                    }
                }
                // Set the adapter to RecyclerView
                mRvTrailer.setAdapter(mTrailerAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Details> call, @NonNull Throwable t) {
                Toast.makeText(DetailsActivity.this, R.string.error_fetch, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get the reviews by Retrofit library
     *
     * @param id refer to getId method in model class.
     */
    public void displayReviews(int id) {

        Call<Reviews> call = APIClient.getInstance().getApi().get_Movie_Reviews(id, apiKey);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {

                if (response.body() != null) {
                    mReviewsList = response.body().getResults();
                }
                // Initialize the adapter
                mReviewAdapter = new ReviewAdapter(DetailsActivity.this, mReviewsList);

                // Get the number of reviews in adapter
                int numOfComments = mReviewAdapter.getItemCount();

                // If there's no reviews found, notify the user via text
                if (mReviewAdapter.getItemCount() == 0) {
                    tvReviewsLabel.setVisibility(View.GONE);
                    tvNoReviews.setVisibility(View.VISIBLE);
                    tvLabelComments.setVisibility(View.GONE);

                    // If there's one review only, write reviews without "S"
                } else {
                    if (mReviewAdapter.getItemCount() == 1) {
                        tvCommentsNumbers.setText(String.valueOf(numOfComments));
                        tvLabelComments.setText(R.string.one_comment);

                        // Write reviews with "S"
                    } else {
                        tvCommentsNumbers.setText(String.valueOf(numOfComments));
                    }
                }
                // Set the adapter to RecyclerView
                mRvReview.setAdapter(mReviewAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
                Toast.makeText(DetailsActivity.this, R.string.error_fetch, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Add thousand separators in number
     *
     * @param number refer to the budget and revenue to format it
     * @return the number after formatting
     */
    private String formatNumber(double number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
}