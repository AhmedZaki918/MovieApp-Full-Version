package com.example.android.moviesapp.ui.adapter.viewholder;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.databinding.LayoutFavouriteBinding;
import com.example.android.moviesapp.data.model.AllData;
import com.example.android.moviesapp.ui.favourite.OnFavouriteClick;
import com.example.android.moviesapp.util.ViewUtils;

public class FavouriteViewHolder extends RecyclerView.ViewHolder {

    private static final String CLICK = "CLICK_ITEM";
    private static final String DELETE = "DELETE_ITEM";
    private final LayoutFavouriteBinding binding;
    private final OnFavouriteClick onFavouriteClick;
    private final Context context;


    public FavouriteViewHolder(LayoutFavouriteBinding binding,
                               OnFavouriteClick onFavouriteClick,
                               Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.onFavouriteClick = onFavouriteClick;
        this.context = context;
    }


    public void bind(AllData currentItem) {
        binding.tvMovieTitle.setText(currentItem.getTitle());
        // Build url of poster
        String finalUrl = Constants.IMAGE_BASE_URL_ORIGINAL + currentItem.getPoster();
        ViewUtils.setupGlide(context, finalUrl, binding.ivPoster);
        // Click listener on views
        itemView.setOnClickListener(v -> onFavouriteClick.onItemClick(CLICK, currentItem));
        binding.ivDelete.setOnClickListener(v -> onFavouriteClick.onItemClick(DELETE, currentItem));
    }
}