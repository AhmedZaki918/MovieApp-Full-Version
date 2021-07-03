package com.example.android.moviesapp.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.adapter.Constants;
import com.example.android.moviesapp.databinding.LayoutMainBinding;
import com.example.android.moviesapp.model.AllData;
import com.example.android.moviesapp.util.OnAdapterClick;
import com.squareup.picasso.Picasso;

public class MainViewHolder extends RecyclerView.ViewHolder {

    LayoutMainBinding binding;
    OnAdapterClick onAdapterClick;

    // Our constructor
    public MainViewHolder(LayoutMainBinding binding, OnAdapterClick onAdapterClick) {
        super(binding.getRoot());
        this.binding = binding;
        this.onAdapterClick = onAdapterClick;
    }

    // Update views based on current item
    public void bind(AllData currentItem) {
        // Display views on screen
        binding.tvMovieTitle.setText(currentItem.getTitle());
        binding.tvRating.setText(currentItem.getUserRating());
        // To get poster url
        final String finalUrl = Constants.IMAGE_BASE_URL_ORIGINAL + currentItem.getPoster();
        // Display the image by Picasso library
        Picasso.get()
                .load(finalUrl)
                .into(binding.ivPoster);
        // Click listener on item
        itemView.setOnClickListener(v -> onAdapterClick.onItemClick(currentItem));
    }
}