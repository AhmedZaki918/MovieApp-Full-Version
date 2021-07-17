package com.example.android.moviesapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.ui.adapter.viewholder.TrailersViewHolder;
import com.example.android.moviesapp.databinding.LayoutTrailerBinding;
import com.example.android.moviesapp.data.model.Trailers.TrailersResults;
import com.example.android.moviesapp.util.OnAdapterClick;

import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailersViewHolder> {

    private final List<TrailersResults> data;
    OnAdapterClick onAdapterClick;
    // Constructor for our TrailerAdapter
    public TrailerAdapter(List<TrailersResults> data, OnAdapterClick onAdapterClick) {
        this.data = data;
        this.onAdapterClick = onAdapterClick;
    }


    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailersViewHolder(LayoutTrailerBinding
                .inflate(LayoutInflater.from(parent.getContext())), onAdapterClick);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}