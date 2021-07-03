package com.example.android.moviesapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.adapter.viewholder.MainViewHolder;
import com.example.android.moviesapp.databinding.LayoutMainBinding;
import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.util.OnAdapterClick;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {


    private final List<AllData> data;
    OnAdapterClick onAdapterClick;

    // Constructor for our MainAdapter
    public MainAdapter(List<AllData> mAllDataList, OnAdapterClick onAdapterClick) {
        this.data = mAllDataList;
        this.onAdapterClick = onAdapterClick;
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutMainBinding
                .inflate(LayoutInflater.from(parent.getContext())), onAdapterClick);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        // Get the position of the current list item
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}