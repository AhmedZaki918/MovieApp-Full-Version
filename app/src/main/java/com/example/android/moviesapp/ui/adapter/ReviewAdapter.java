package com.example.android.moviesapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.ui.adapter.viewholder.ReviewViewHolder;
import com.example.android.moviesapp.databinding.LayoutReviewBinding;
import com.example.android.moviesapp.data.model.Reviews.ReviewsResults;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private final List<ReviewsResults> data;
    // Constructor for our ReviewAdapter
    public ReviewAdapter(List<ReviewsResults> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutReviewBinding
                .inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}