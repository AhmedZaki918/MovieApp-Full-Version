package com.example.android.moviesapp.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.adapter.MoviesAdapter;
import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesActivity extends AppCompatActivity {


    /**
     * Initialize the variables
     */
    private MoviesAdapter mAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<AllData> allData;
    private ProgressBar mProgressBar;
    private TextView movieCategory;
    // For retrieve scroll position of the RecyclerView
    private static int index = -1;
    private static int top = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieCategory = findViewById(R.id.tv_category_name);

        // Find a reference to the List<AllData>
        allData = new ArrayList<>();

        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = findViewById(R.id.recycler_view);

        //  Find a reference to ProgressBar
        mProgressBar = findViewById(R.id.loading_indicator);

        // Set layout manager and RecyclerView
        layoutManager = new GridLayoutManager(this, calculateNumberOfColumns(2));
        recyclerView.setLayoutManager(layoutManager);

        // Call display user preferences method
        displayUserPreferences();
    }

    // Custom method to calculate number of columns for grid type recycler view
    private int calculateNumberOfColumns(int base) {
        int columns = base;
        String screenSize = getScreenSizeCategory();

        if (screenSize.equals("small")) {
            if (base != 1) {
                columns = columns - 1;
            }
        } else if (screenSize.equals("normal")) {
            // Do nothing
        } else if (screenSize.equals("large")) {
            columns += 2;
        } else if (screenSize.equals("xlarge")) {
            columns += 3;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = (int) (columns * 2);
        }

        return columns;
    }

    // Custom method to get screen size category
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

    @Override
    public void onPause() {
        super.onPause();
        //read current RecyclerView position
        index = layoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        //set RecyclerView position
        if (index != -1) {
            layoutManager.scrollToPositionWithOffset(index, top);
        }
    }

    // Get the user preferences from shared preferences and display it by Retrofit library
    public void displayUserPreferences() {

        // String variable to store Api key
        String apiKey = Constants.Api_key;

        // The setup of shared preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // String variable to store the key and default value in shared preferences
        String sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        // Check the user preferences to run the selected url
        if (sortBy.equals(getString(R.string.settings_sort_by_highest_rated_value))) {

            movieCategory.setText(R.string.settings_sort_by_popularity_label);

            Call<AllData> call = APIClient.getInstance().getApi().get_top_rated(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(Call<AllData> call, Response<AllData> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response != null) {
                        AllData resultsData = response.body();
                        if (resultsData != null) {
                            allData = resultsData.getResults();
                            mAdapter = new MoviesAdapter(MoviesActivity.this, allData);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllData> call, Throwable t) {
                    Toast.makeText(MoviesActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
                }
            });

            // Check the user preferences to run the selected url
        } else if (sortBy.equals(getString(R.string.settings_sort_by_popularity_value))) {

            movieCategory.setText(R.string.settings_sort_by_highest_rated_label);

            Call<AllData> call = APIClient.getInstance().getApi().get_popular(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(Call<AllData> call, Response<AllData> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response != null) {
                        AllData resultsData = response.body();
                        if (resultsData != null) {
                            allData = resultsData.getResults();
                            mAdapter = new MoviesAdapter(MoviesActivity.this, allData);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllData> call, Throwable t) {
                    Toast.makeText(MoviesActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Check the user preferences to run the selected url
        else if (sortBy.equals(getString(R.string.settings_sort_by_now_playing_value))) {

            movieCategory.setText(R.string.settings_sort_by_now_playing_label);


            Call<AllData> call = APIClient.getInstance().getApi().get_now_playing(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(Call<AllData> call, Response<AllData> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response != null) {
                        AllData resultsData = response.body();
                        if (resultsData != null) {
                            allData = resultsData.getResults();
                            mAdapter = new MoviesAdapter(MoviesActivity.this, allData);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllData> call, Throwable t) {
                    Toast.makeText(MoviesActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.favourite_settings) {
            startActivity(new Intent(MoviesActivity.this, FavouriteActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}