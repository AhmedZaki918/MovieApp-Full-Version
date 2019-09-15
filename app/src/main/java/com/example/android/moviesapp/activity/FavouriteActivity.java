package com.example.android.moviesapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.adapter.FavouriteAdapter;
import com.example.android.moviesapp.database.AppDatabase;
import com.example.android.moviesapp.database.AppExecutors;
import com.example.android.moviesapp.database.MainViewModel;
import com.example.android.moviesapp.model.MovieData;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    // Initialize variables
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private GridLayoutManager layoutManager;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Find a reference to AppDatabase
        mDb = AppDatabase.getInstance(getApplicationContext());

        // Find a reference to the recyclerView
        recyclerView = findViewById(R.id.recycler_view_favo);

        // Set layout manager and RecyclerView
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Bind the Adapter to RecyclerView
        favouriteAdapter = new FavouriteAdapter(FavouriteActivity.this);
        recyclerView.setAdapter(favouriteAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<MovieData> movieData = favouriteAdapter.getMovieData();
                        // Call deleteMovie in the movieDao at that position
                        mDb.movieDao().deleteMovie(movieData.get(position));

                    }
                });

            }
        }).attachToRecyclerView(recyclerView);


        setupViewModel();
    }

    // The operation of the ViewModel
    public void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(FavouriteActivity.this).get(MainViewModel.class);
        viewModel.getMoviesData().observe(FavouriteActivity.this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                favouriteAdapter.setMovieData(movieData);
            }
        });
    }
}