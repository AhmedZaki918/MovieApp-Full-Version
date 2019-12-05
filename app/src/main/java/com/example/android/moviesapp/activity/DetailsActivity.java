package com.example.android.moviesapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.adapter.ReviewAdapter;
import com.example.android.moviesapp.adapter.TrailerAdapter;
import com.example.android.moviesapp.database.AppDatabase;
import com.example.android.moviesapp.database.AppExecutors;
import com.example.android.moviesapp.model.MovieData;
import com.example.android.moviesapp.model.Reviews.DetailsReview;
import com.example.android.moviesapp.model.Reviews.Reviews;
import com.example.android.moviesapp.model.Trailers.DetailsTrailer;
import com.example.android.moviesapp.model.Trailers.Trailers;
import com.example.android.moviesapp.network.APIClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_poster)
    ImageView poster;
    @BindView(R.id.tv_rating)
    TextView userRating;
    @BindView(R.id.tv_summary)
    TextView overview;
    @BindView(R.id.tv_release_date)
    TextView releaseDate;
    @BindView(R.id.ch_favou)
    CheckBox save;
    @BindView(R.id.textView5)
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


    // String variables to store the given value passed by the intent
    private String givenPoster;
    private String givenTitle;
    private String givenRating;
    private String givenOverview;
    private String givenDate;

    // Initialize RecyclerView, Adapter, Api key and Model classes
    private RecyclerView recyclerTrailer;
    private RecyclerView recyclerReview;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private List<DetailsTrailer> detailsTrailers;
    private List<DetailsReview> detailsReviews;
    private String apiKey = Constants.Api_key;

    // Obj from model class
    MovieData movieData;
    // Member variable for the Database
    private AppDatabase mDb;

    // variables for SharedPreferences
    private SharedPreferences StatePreferences;
    private SharedPreferences.Editor StatePrefsEditor;
    private Boolean State;
    private int movId;

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
        movieData = intent.getParcelableExtra(Constants.SOURCE);

        // Get all fields we need to show them in that activity By Intent
        givenPoster = Constants.IMAGE_BASE_URL_NORMAL + movieData.getmBackdrop();
        givenTitle = movieData.getTitle();
        givenRating = movieData.getUserRating();
        givenOverview = movieData.getOverview();
        givenDate = movieData.getReleaseDate();

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

        // Find a reference to the DetailsTrailer model class
        detailsTrailers = new ArrayList<>();

        // Find a reference to the RecyclerView for the reviews
        recyclerReview = findViewById(R.id.recycler_view_reviews);
        // Set layout manager for RecyclerView
        LinearLayoutManager lmReview = new LinearLayoutManager(this);
        recyclerReview.setLayoutManager(lmReview);

        // Find a reference to the DetailsReview model class
        detailsReviews = new ArrayList<>();

        // Display the data related of trailers and reviews by getId method in the model class
        displayTrailers(movieData.getId());
        displayReviews(movieData.getId());

        // To save the state of CheckBox Favourite button (Check And UnChecked)
        StatePreferences = getSharedPreferences("ChkPrefs", MODE_PRIVATE);
        StatePrefsEditor = StatePreferences.edit();
        State = StatePreferences.getBoolean("CheckState", false);
        movId = StatePreferences.getInt("Movieid", 0);
        if (State == true && movId == movieData.getId()) {
            save.setChecked(true);
        }
    }

    // Get the trailers by Retrofit library
    public void displayTrailers(int id) {

        Call<Trailers> call = APIClient.getInstance().getApi().get_Movie_Trailers(id, apiKey);
        call.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {

                detailsTrailers = response.body().getResults();
                trailerAdapter = new TrailerAdapter(DetailsActivity.this, detailsTrailers);
                recyclerTrailer.setAdapter(trailerAdapter);
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
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

                detailsReviews = response.body().getResults();
                reviewAdapter = new ReviewAdapter(DetailsActivity.this, detailsReviews);
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
                    mDb.movieDao().insertMovie(movieData);
                }
            });
            StatePrefsEditor.putBoolean("CheckState", true);
            StatePrefsEditor.putInt("Movieid", movieData.getId());
            StatePrefsEditor.commit();

            Toast toast = Toast.makeText(DetailsActivity.this, "Saved", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        } else {

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    // Delete the selected move by it's position
                    mDb.movieDao().deleteMovie(movieData);
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
}