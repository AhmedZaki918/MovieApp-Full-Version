package com.example.android.moviesapp.ui.adapter.viewholder;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.databinding.LayoutMainBinding;
import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.util.OnAdapterClick;
import com.example.android.moviesapp.util.ViewUtils;

public class MainViewHolder extends RecyclerView.ViewHolder {

    LayoutMainBinding binding;
    OnAdapterClick onAdapterClick;
    Context context;

    // Our constructor
    public MainViewHolder(LayoutMainBinding binding, OnAdapterClick onAdapterClick, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.onAdapterClick = onAdapterClick;
        this.context = context;
    }

    // Update views based on current item
    public void bind(AllData currentItem) {
        binding.tvMovieTitle.setText(currentItem.getTitle());
        binding.tvRating.setText(currentItem.getUserRating());
        // To get poster url
        final String finalUrl = Constants.IMAGE_BASE_URL_ORIGINAL + currentItem.getPoster();
        ViewUtils.setupGlide(context, finalUrl, binding.ivPoster);
        // Click listener on item
        itemView.setOnClickListener(v -> onAdapterClick.onItemClick(currentItem));
    }
}