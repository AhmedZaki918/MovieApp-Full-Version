package com.example.android.moviesapp.ui.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.databinding.LayoutReviewBinding;
import com.example.android.moviesapp.data.model.Reviews.ReviewsResults;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    LayoutReviewBinding binding;

    public ReviewViewHolder(LayoutReviewBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ReviewsResults currentItem) {
        binding.tvReviews.setText(currentItem.getContent());
        binding.tvAuthor.setText(currentItem.getAuthor());
    }
}