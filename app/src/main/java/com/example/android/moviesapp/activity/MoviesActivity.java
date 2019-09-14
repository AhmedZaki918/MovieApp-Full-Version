package com.example.android.moviesapp.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.adapter.FavouriteAdapter;
import com.example.android.moviesapp.adapter.MoviesAdapter;
import com.example.android.moviesapp.database.MainViewModel;
import com.example.android.moviesapp.model.MovieData;
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
    private FavouriteAdapter favouriteAdapter;
    private GridLayoutManager layoutManager;
    List<MovieData> movieData;

    // For retrieve scroll position of the RecyclerView
    private static int index = -1;
    private static int top = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the List<MovieData>
        movieData = new ArrayList<>();

        // Find a reference to the {@link RecyclerView} in the layout
        recyclerView = findViewById(R.id.recycler_view);

        // Set layout manager and RecyclerView
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Call display user preferences method
        displayUserPreferences();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        recyclerView.setLayoutManager(new GridLayoutManager
                (this, newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2));
        recyclerView.setHasFixedSize(true);
        super.onConfigurationChanged(newConfig);
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

            Call<MovieData> call = APIClient.getInstance().getApi().get_top_rated(apiKey);
            call.enqueue(new Callback<MovieData>() {
                @Override
                public void onResponse(Call<MovieData> call, Response<MovieData> response) {

                    if (response != null) {
                        MovieData resultsData = response.body();
                        if (resultsData != null) {
                            movieData = resultsData.getResults();
                            mAdapter = new MoviesAdapter(MoviesActivity.this, movieData);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieData> call, Throwable t) {
                    Toast.makeText(MoviesActivity.this, getString(R.string.error_fetch), Toast.LENGTH_SHORT).show();
                }
            });

            // Check the user preferences to run the selected url
        } else if (sortBy.equals(getString(R.string.settings_sort_by_popularity_value))) {

            Call<MovieData> call = APIClient.getInstance().getApi().get_popular(apiKey);
            call.enqueue(new Callback<MovieData>() {
                @Override
                public void onResponse(Call<MovieData> call, Response<MovieData> response) {

                    if (response != null) {
                        MovieData resultsData = response.body();
                        if (resultsData != null) {
                            movieData = resultsData.getResults();
                            mAdapter = new MoviesAdapter(MoviesActivity.this, movieData);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieData> call, Throwable t) {
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

            // Set layout manager and RecyclerView
            layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            // Bind the Adapter to RecyclerView
            favouriteAdapter = new FavouriteAdapter(MoviesActivity.this);
            recyclerView.setAdapter(favouriteAdapter);

            setupViewModel();
        }
        return super.onOptionsItemSelected(item);
    }

    // The operation of the ViewModel
    public void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(MoviesActivity.this).get(MainViewModel.class);
        viewModel.getMoviesData().observe(MoviesActivity.this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                favouriteAdapter.setMovieData(movieData);
            }
        });
    }
}