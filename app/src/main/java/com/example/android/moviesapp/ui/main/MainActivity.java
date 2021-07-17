package com.example.android.moviesapp.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.data.local.UserPreferences;
import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.data.network.APIService;
import com.example.android.moviesapp.databinding.ActivityMainBinding;
import com.example.android.moviesapp.databinding.LayoutMainBinding;
import com.example.android.moviesapp.ui.adapter.GenericAdapter;
import com.example.android.moviesapp.ui.details.DetailsActivity;
import com.example.android.moviesapp.ui.favourite.FavouriteActivity;
import com.example.android.moviesapp.util.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.view.View.GONE;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {


    // Initialize the variables
    private static final String API_KEY = Constants.Api_key;
    private static final String KEY = Constants.KEY;
    private static final String POPULAR = Constants.POPULAR;
    private static final String HIGHEST = Constants.HIGHEST;
    private static final String NOW = Constants.NOW;
    private ActivityMainBinding binding;
    private UserPreferences userPreferences;
    private MainViewModel viewModel;

    @Inject
    APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        // Setup view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        // Retrieve data in shared preferences if it's available
        switchOnSort(userPreferences.retrieveData(Constants.KEY));
        viewModel.deliverResponse().observe(this, response -> updateUi(response.getResults()));
    }


//    @Override
//    public <T> void onItemClick(T data) {
//        startActivity(new Intent(this, DetailsActivity.class)
//                .putExtra(Constants.EXTRA_DATA, (Parcelable) data));
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular) {
            switchOnSort(POPULAR);
            userPreferences.saveData(KEY, POPULAR);

        } else if (id == R.id.highest) {
            switchOnSort(HIGHEST);
            userPreferences.saveData(KEY, HIGHEST);

        } else if (id == R.id.now) {
            switchOnSort(NOW);
            userPreferences.saveData(KEY, NOW);

        } else if (id == R.id.favourite_settings) {
            startActivity(new Intent(MainActivity.this,
                    FavouriteActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        binding.shimmerFrame.startShimmer();
        userPreferences = new UserPreferences(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }


    /**
     * Switch on selected sort and create api request of it
     *
     * @param type is a sort type of movies
     */
    private void switchOnSort(String type) {
        switch (type) {
            case POPULAR:
                viewModel.initRequest(apiService.getTopRated(API_KEY));
                binding.tvCategoryName.setText(R.string.popularity_label);
                break;

            case HIGHEST:
                viewModel.initRequest(apiService.getPopular(API_KEY));
                binding.tvCategoryName.setText(R.string.highest_label);
                break;

            case NOW:
            default:
                viewModel.initRequest(apiService.getNowPlaying(API_KEY));
                binding.tvCategoryName.setText(R.string.now_label);
                break;
        }
    }


    private void updateUi(List<AllData> data) {

        binding.shimmerFrame.stopShimmer();
        binding.shimmerFrame.setVisibility(GONE);
        binding.rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvMain.setHasFixedSize(true);

        binding.rvMain.setAdapter(new GenericAdapter<AllData, LayoutMainBinding>(data) {

            @Override
            public int getLayoutResId() {
                return R.layout.layout_main;
            }

            @Override
            public void onBindData(AllData model, int position, LayoutMainBinding binding) {
                binding.tvMovieTitle.setText(model.getTitle());
                binding.tvRating.setText(model.getUserRating());

                // To get poster url
                final String finalUrl = Constants.IMAGE_BASE_URL_ORIGINAL + model.getPoster();
                ViewUtils.setupGlide(MainActivity.this, finalUrl, binding.ivPoster);
            }

            @Override
            public void onItemClick(AllData model, int position) {
                startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                        .putExtra(Constants.EXTRA_DATA, model));
            }
        });


//        ViewUtils.setupRecyclerView(binding.rvMain, new GridLayoutManager(this, 2),
//                new MainAdapter(data, this));
    }
}