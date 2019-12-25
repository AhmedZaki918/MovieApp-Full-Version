package com.example.android.moviesapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Double.parseDouble;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster)
    ImageView poster;
    @BindView(R.id.tv_rating)
    TextView userRating;
    @BindView(R.id.tv_summary)
    ReadMoreTextView overview;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.ch_favou)
    CheckBox save;
    @BindView(R.id.tv_overview_label)
    TextView textView5;
    @BindView(R.id.tv_trailer_label)
    TextView tvTrailerLabel;
    @BindView(R.id.recycler_view_trailer)
    RecyclerView recyclerViewTrailer;
    @BindView(R.id.tv_reviews_label)
    TextView tvReviewsLabel;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView recyclerViewReviews;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_revenue)
    TextView revenue;
    @BindView(R.id.tv_status)
    TextView status;
    @BindView(R.id.tv_budget)
    TextView budget;
    @BindView(R.id.tv_vote_count)
    TextView voteCount;
    @BindView(R.id.tv_popularity)
    TextView popularity;
    @BindView(R.id.tv_language)
    TextView language;
    @BindView(R.id.tv_no_reviews)
    TextView noReviews;
    @BindView(R.id.tv_no_trailers)
    TextView noTrailers;


    // String variables to store the given value passed by the intent
    public String givenPoster;
    public String givenTitle;
    public String givenRating;
    public String givenOverview;
    private String givenDate;

    // Initialize RecyclerView, Adapter, Api key and Model classes
    private RecyclerView recyclerTrailer;
    private RecyclerView recyclerReview;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private List<com.example.android.moviesapp.model.Trailers.Results> trailersResults;
    private List<Results> reviewsResults;
    private String apiKey = Constants.Api_key;

    // Obj from model class
    AllData allData;
    // Member variable for the Database
    private AppDatabase mDb;


    // variables for SharedPreferences
    public SharedPreferences StatePreferences;
    private SharedPreferences.Editor StatePrefsEditor;
    public Boolean State;
    public int movId;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Prepare the code to use ButterKnife library
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();

        // Find a reference to the AppDatabase class
        mDb = AppDatabase.getInstance(getApplicationContext());
        // Prepare the intent to use it
        Intent intent = getIntent();
        // Get the data from the model class
        allData = intent.getParcelableExtra(Constants.SOURCE);

        // Get all fields we need to show them in that activity By Intent
        givenPoster = Constants.IMAGE_BASE_URL_NORMAL + allData.getmBackdrop();
        givenTitle = allData.getTitle();
        givenRating = allData.getUserRating();
        givenOverview = allData.getOverview();
        givenDate = allData.getReleaseDate();
//        Log.e("DetailsActivity", "Budget = " + details.getBudget());

        givenPoster = Constants.IMAGE_BASE_URL_NORMAL + allData.getmBackdrop();
//        Log.e("DetailsActivity", "Backdrop = " + allData.getmBackdrop());

        // Display the poster of the selected movie By Picasso library
        Picasso.with(this).load(givenPoster).into(poster);

        // Pass the given text by intent and display it in the TextView
        actionBar.setTitle(givenTitle);
        userRating.setText(givenRating);
        overview.setText(givenOverview);
        releaseDate.setText(givenDate);

        // Find a reference to the RecyclerView for the trailers
        recyclerTrailer = findViewById(R.id.recycler_view_trailer);
        // Set layout manager for RecyclerView
        LinearLayoutManager lmTrailer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        recyclerTrailer.setLayoutManager(lmTrailer);

        // Find a reference to the Results model class
        trailersResults = new ArrayList<>();

        // Find a reference to the RecyclerView for the reviews
        recyclerReview = findViewById(R.id.recycler_view_reviews);
        // Set layout manager for RecyclerView
        LinearLayoutManager lmReview = new LinearLayoutManager(this);
        recyclerReview.setLayoutManager(lmReview);

        // Find a reference to the Results model class
        trailersResults = new ArrayList<>();

        // Display the data related of trailers and reviews by getId method in the model class
        displayTrailers(allData.getId());
        displayReviews(allData.getId());

        // To save the state of CheckBox Favourite button (Check And UnChecked)
        StatePreferences = getSharedPreferences("ChkPrefs", MODE_PRIVATE);
        StatePrefsEditor = StatePreferences.edit();
        State = StatePreferences.getBoolean("CheckState", false);
        movId = StatePreferences.getInt("Movieid", 0);
        if (State && movId == allData.getId()) {
            save.setChecked(true);
        }
    }

    // Get the trailers by Retrofit library
    public void displayTrailers(int id) {

        Call<Details> call = APIClient.getInstance().getApi().get_Movie_Trailers(id, apiKey, "videos");
        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                if (response.body() != null) {
                    trailersResults = response.body().videos.getResults();

                    String budgetValue = response.body().getBudget();
                    budgetValue = formatNumber(parseDouble(budgetValue));
                    budget.setText(String.format("%s$", budgetValue));

                    String revenueValue = response.body().getRevenue();
                    revenueValue = formatNumber(parseDouble(revenueValue));
                    revenue.setText(String.format("%s$", revenueValue));

                    status.setText(response.body().getStatus());
                    voteCount.setText(response.body().getVoteCount());
                    popularity.setText(response.body().getPopularity());
                    language.setText(response.body().getLanguage());


                }
                trailerAdapter = new TrailerAdapter(DetailsActivity.this, trailersResults);

                if (trailerAdapter.getItemCount() == 0) {
                    tvTrailerLabel.setVisibility(View.GONE);
                    noTrailers.setVisibility(View.VISIBLE);

                }
                recyclerTrailer.setAdapter(trailerAdapter);
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Get the reviews by Retrofit library
    public void displayReviews(int id) {

        Call<Reviews> call = APIClient.getInstance().getApi().get_Movie_Reviews(id, apiKey);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {

                if (response.body() != null) {
                    reviewsResults = response.body().getResults();
                }
                reviewAdapter = new ReviewAdapter(DetailsActivity.this, reviewsResults);

                if (reviewAdapter.getItemCount() == 0) {
                    tvReviewsLabel.setVisibility(View.GONE);
                    noReviews.setVisibility(View.VISIBLE);
                }
                recyclerReview.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Save movies to the database
    @OnClick(R.id.ch_favou)
    public void onViewClicked() {
        if (save.isChecked()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Insert the selected move to the database
                    mDb.movieDao().insertMovie(allData);
                }
            });
            StatePrefsEditor.putBoolean("CheckState", true);
            StatePrefsEditor.putInt("Movieid", allData.getId());
            StatePrefsEditor.commit();

            Toast toast = Toast.makeText(DetailsActivity.this, "Saved", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        } else {

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Delete the selected move by it's position
                    mDb.movieDao().deleteMovie(allData);
                }
            });
            StatePrefsEditor.putBoolean("CheckState", false);
            StatePrefsEditor.putInt("Movieid", 0);
            StatePrefsEditor.commit();

            Toast toast = Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    // Add thousand separators in number
    private String formatNumber(double number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
}