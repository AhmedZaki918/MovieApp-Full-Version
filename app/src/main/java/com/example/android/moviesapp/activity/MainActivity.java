package com.example.android.moviesapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.adapter.MainAdapter;
import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.network.APIClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.rv_main)
    RecyclerView rv;
    @BindView(R.id.tv_category_name)
    TextView movieCategory;
    @BindView(R.id.loading_indicator)
    ProgressBar pb;
    @BindView(R.id.tv_no_connection)
    TextView noConnection;
    @BindView(R.id.btn_retry)
    Button retry;

    /**
     * Initialize the variables
     */
    private MainAdapter mMainAdapter;
    private GridLayoutManager mLm;
    private List<AllData> mAllDataList;
    private NetworkInfo mNetworkInfo;

    // For retrieve scroll position of the RecyclerView
    private static int index = -1;
    private static int top = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Setup for the RecyclerView
        mLm = new GridLayoutManager(this, calculateNumberOfColumns());
        rv.setLayoutManager(mLm);
        rv.setHasFixedSize(true);
        mAllDataList = new ArrayList<>();

        // Calling the method
        displayUserPreferences();

        // Swipe to refresh the coming data
        swipeLayout.setOnRefreshListener(() -> {
            // First check the current state of the network
            checkConnection();

            // If there is a network connection, fetch data
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                // Set visibility on the views
                noConnection.setVisibility(View.GONE);
                retry.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

                // Calling the method
                displayUserPreferences();
                swipeLayout.setRefreshing(false);

            } else {
                // If there is no network connection
                setVisibility();
                rv.setVisibility(View.GONE);
                swipeLayout.setRefreshing(false);
            }
        });

        // Button to retry fetch data from the server in case of (Connection lost)
        retry.setOnClickListener(view -> {
            // First check the current state of the network
            checkConnection();

            // If there is a network connection, fetch data
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                // Set visibility on the views
                pb.setVisibility(View.VISIBLE);
                noConnection.setVisibility(View.GONE);
                retry.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                displayUserPreferences();

            } else {
                // If there is no network connection
                setVisibility();
                Toast.makeText(MainActivity.this, R.string.connection_lost, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        //read current RecyclerView position
        index = mLm.findFirstVisibleItemPosition();
        View v = rv.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - rv.getPaddingTop());
    }

    @Override
    public void onResume() {
        super.onResume();
        //set RecyclerView position
        if (index != -1) {
            mLm.scrollToPositionWithOffset(index, top);
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

        // 1- Check the user preferences to run MOST POPULAR
        if (sortBy.equals(getString(R.string.settings_sort_by_highest_rated_value))) {

            // Display MOST POPULAR on the TextView
            movieCategory.setText(R.string.settings_sort_by_popularity_label);

            Call<AllData> call = APIClient.getInstance().getApi().get_top_rated(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(@NonNull Call<AllData> call, @NonNull Response<AllData> response) {

                    // Set gone visibility for ProgressPar
                    pb.setVisibility(View.GONE);

                    AllData resultsData = response.body();
                    if (resultsData != null) {
                        mAllDataList = resultsData.getResults();
                        mMainAdapter = new MainAdapter(MainActivity.this, mAllDataList);
                        rv.setAdapter(mMainAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllData> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_fetch, Toast.LENGTH_SHORT).show();
                    setVisibility();
                }
            });

            // 2- Check the user preferences to run "HIGHEST RATED"
        } else if (sortBy.equals(getString(R.string.settings_sort_by_popularity_value))) {

            // Display HIGHEST RATED on the TextView
            movieCategory.setText(R.string.settings_sort_by_highest_rated_label);

            Call<AllData> call = APIClient.getInstance().getApi().get_popular(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(@NonNull Call<AllData> call, @NonNull Response<AllData> response) {

                    // Set gone visibility for ProgressPar
                    pb.setVisibility(View.GONE);

                    AllData resultsData = response.body();
                    if (resultsData != null) {
                        mAllDataList = resultsData.getResults();
                        mMainAdapter = new MainAdapter(MainActivity.this, mAllDataList);
                        rv.setAdapter(mMainAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllData> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_fetch, Toast.LENGTH_SHORT).show();
                    setVisibility();
                }
            });
        }

        // 3- Check the user preferences to run NOW PLAYING
        else if (sortBy.equals(getString(R.string.settings_sort_by_now_playing_value))) {

            // Display NOW PLAYING on the TextView
            movieCategory.setText(R.string.settings_sort_by_now_playing_label);

            Call<AllData> call = APIClient.getInstance().getApi().get_now_playing(apiKey);
            call.enqueue(new Callback<AllData>() {
                @Override
                public void onResponse(@NonNull Call<AllData> call, @NonNull Response<AllData> response) {

                    // Set gone visibility for ProgressPar
                    pb.setVisibility(View.GONE);

                    AllData resultsData = response.body();
                    if (resultsData != null) {
                        mAllDataList = resultsData.getResults();
                        mMainAdapter = new MainAdapter(MainActivity.this, mAllDataList);
                        rv.setAdapter(mMainAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllData> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_fetch, Toast.LENGTH_SHORT).show();
                    setVisibility();
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
            startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    // Check the state of internet connection
    private void checkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        assert connMgr != null;
        mNetworkInfo = connMgr.getActiveNetworkInfo();
    }

    // Set visibility in case of connection lost
    private void setVisibility() {
        pb.setVisibility(View.GONE);
        noConnection.setVisibility(View.VISIBLE);
        noConnection.setText(R.string.no_internet_connection);
        retry.setVisibility(View.VISIBLE);
    }
}