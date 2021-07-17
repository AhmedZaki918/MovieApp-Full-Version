package com.example.android.moviesapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Ignore;

import com.example.android.moviesapp.ui.adapter.viewholder.FavouriteViewHolder;
import com.example.android.moviesapp.databinding.LayoutFavouriteBinding;
import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.ui.favourite.OnFavouriteClick;

import java.util.List;

import javax.inject.Inject;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {

    private final List<AllData> data;
    OnFavouriteClick onFavouriteClick;

    // Constructor for our FavouriteAdapter
    @Inject
    public FavouriteAdapter(List<AllData> data, OnFavouriteClick onFavouriteClick) {
        this.data = data;
        this.onFavouriteClick = onFavouriteClick;
    }


    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new FavouriteViewHolder(LayoutFavouriteBinding
                .inflate(LayoutInflater.from(context)), onFavouriteClick, context);
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