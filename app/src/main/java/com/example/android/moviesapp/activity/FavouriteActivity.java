package com.example.android.moviesapp.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapter.FavouriteAdapter;
import com.example.android.moviesapp.database.AppDatabase;
import com.example.android.moviesapp.database.AppExecutors;
import com.example.android.moviesapp.database.MainViewModel;


import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteActivity extends AppCompatActivity {


    @BindView(R.id.rv_favourite)
    RecyclerView rvFavourite;
    @BindView(R.id.tv_add_movie)
    TextView tvAddMovie;
    @BindView(R.id.tv_nothing_show)
    TextView tvNothingShow;

    // Initialize variables
    private FavouriteAdapter mFavouriteAdapter;
    GridLayoutManager layoutManager;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_favourite);
        ButterKnife.bind(this);

        // Find a reference to AppDatabase
        mDb = AppDatabase.getInstance(getApplicationContext());

        // Setup for the RecyclerView
        layoutManager = new GridLayoutManager(this, calculateNumberOfColumns());
        rvFavourite.setLayoutManager(layoutManager);
        rvFavourite.setHasFixedSize(true);
        mFavouriteAdapter = new FavouriteAdapter(FavouriteActivity.this);
        rvFavourite.setAdapter(mFavouriteAdapter);

        // Calling the method
        setupViewModel();
    }

    /**
     * Custom method to calculate number of columns for grid type recycler view
     *
     * @return the number of columns to display on screen.
     */
    private int calculateNumberOfColumns() {
        // Initialize 2 columns at first
        int columns = 2;
        String screenSize = getScreenSizeCategory();

        switch (screenSize) {
            case "small":
                columns -= 1;
                break;
            case "normal":
                // Do nothing
                break;
            case "large":
                columns += 2;
                break;
            case "xlarge":
                columns += 3;
                break;
        }
        // Case of landscape orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns *= 2;
        }
        return columns;
    }


    /**
     * Custom method to get screen size category
     *
     * @return current screen size
     */
    private String getScreenSizeCategory() {
        int screenLayout = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                // small screens are at least 426dp x 320dp
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                // normal screens are at least 470dp x 320dp
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                // large screens are at least 640dp x 480dp
                return "large";
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                // xlarge screens are at least 960dp x 720dp
                return "xlarge";
            default:
                return "undefined";
        }
    }

    // The operation of the ViewModel
    public void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(FavouriteActivity.this).get(MainViewModel.class);
        viewModel.getAllDataList().observe(FavouriteActivity.this, allData -> mFavouriteAdapter.setAllData(allData));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item1) {
            AppExecutors.getInstance().diskIO().execute(() -> mDb.movieDao().deleteAll());

            // Show messages to notify the user all items were deleted from the database
            tvAddMovie.setVisibility(View.VISIBLE);
            tvNothingShow.setVisibility(View.VISIBLE);
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}