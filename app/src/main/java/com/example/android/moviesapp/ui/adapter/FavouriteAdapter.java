package com.example.android.moviesapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.data.model.MoviesResponse;
import com.example.android.moviesapp.databinding.LayoutFavouriteBinding;
import com.example.android.moviesapp.ui.adapter.viewholder.FavouriteViewHolder;
import com.example.android.moviesapp.ui.favourite.OnFavouriteClick;

import java.util.List;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {

    private final List<MoviesResponse> data;
    OnFavouriteClick onFavouriteClick;

    // Constructor for our FavouriteAdapter
    public FavouriteAdapter(List<MoviesResponse> data, OnFavouriteClick onFavouriteClick) {
        this.data = data;
        this.onFavouriteClick = onFavouriteClick;
    }


    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutFavouriteBinding
                .inflate(LayoutInflater.from(parent.getContext())), onFavouriteClick,
                parent.getContext());
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}