package com.example.android.moviesapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.data.model.MoviesResponse;
import com.example.android.moviesapp.ui.adapter.viewholder.MainViewHolder;
import com.example.android.moviesapp.databinding.LayoutMainBinding;
import com.example.android.moviesapp.util.OnAdapterClick;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {


    private final List<MoviesResponse> data;
    OnAdapterClick onAdapterClick;

    // Constructor for our MainAdapter
    public MainAdapter(List<MoviesResponse> data, OnAdapterClick onAdapterClick) {
        this.data = data;
        this.onAdapterClick = onAdapterClick;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutMainBinding
                .inflate(LayoutInflater.from(parent.getContext())), onAdapterClick,
                parent.getContext());
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}