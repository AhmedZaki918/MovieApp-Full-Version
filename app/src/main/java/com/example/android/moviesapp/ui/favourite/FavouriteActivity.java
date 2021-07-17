package com.example.android.moviesapp.ui.favourite;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.data.local.MovieDao;
import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.databinding.ActivityFavouriteBinding;
import com.example.android.moviesapp.ui.adapter.FavouriteAdapter;
import com.example.android.moviesapp.ui.details.DetailsActivity;
import com.example.android.moviesapp.util.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.view.View.GONE;

@AndroidEntryPoint
public class FavouriteActivity extends AppCompatActivity implements OnFavouriteClick {

    // Initialize variables
    private static final String CLICK = "CLICK_ITEM";
    private ActivityFavouriteBinding binding;

    @Inject
    MovieDao movieDao;
    @Inject
    CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        // Setup view binding
        binding = ActivityFavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FavouriteViewModel viewModel = new ViewModelProvider(this)
                .get(FavouriteViewModel.class);
        viewModel.getAllDataList().observe(FavouriteActivity.this, this::updateUi);
    }


    @Override
    public void onItemClick(String operation, AllData position) {
        if (operation.equals(CLICK)) {
            // Go to details activity
            startActivity(new Intent(this, DetailsActivity.class)
                    .putExtra(Constants.EXTRA_DATA, position));
        } else {
            // Delete the selected movie by it's position
            compositeDisposable.add(movieDao.deleteMovie(position)
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> ViewUtils.showSnackBar(binding.constraintLayout,
                            R.string.movie_removed)));
        }
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
            compositeDisposable.add(movieDao.deleteAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        deleteConfirmed();
                        binding.recyclerView.setVisibility(GONE);
                    }));

        } else if (id == android.R.id.home) {
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


    private void updateUi(List<AllData> data) {
        if (data.isEmpty()) deleteConfirmed();
        else {
            ViewUtils.setupRecyclerView(binding.recyclerView,
                    new GridLayoutManager(this, 2),
                    new FavouriteAdapter(data, this));
        }
    }


    // Show messages to notify the user all items were deleted
    private void deleteConfirmed() {
        binding.tvAddMovie.setVisibility(View.VISIBLE);
        binding.tvNothingShow.setVisibility(View.VISIBLE);
    }
}