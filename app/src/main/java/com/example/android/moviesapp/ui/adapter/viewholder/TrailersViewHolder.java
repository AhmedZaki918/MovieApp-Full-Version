package com.example.android.moviesapp.ui.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesapp.databinding.LayoutTrailerBinding;
import com.example.android.moviesapp.data.model.Trailers.TrailersResults;
import com.example.android.moviesapp.util.OnAdapterClick;
import com.example.android.moviesapp.util.SetupYoutubeThumbnail;

public class TrailersViewHolder extends RecyclerView.ViewHolder {

    LayoutTrailerBinding binding;
    OnAdapterClick onAdapterClick;

    public TrailersViewHolder(LayoutTrailerBinding binding, OnAdapterClick onAdapterClick) {
        super(binding.getRoot());
        this.binding = binding;
        this.onAdapterClick = onAdapterClick;
    }

    public void bind(TrailersResults currentItem) {
        SetupYoutubeThumbnail.initialize(binding, currentItem);
        itemView.setOnClickListener(v -> onAdapterClick.onItemClick(currentItem.getKey()));
    }
}