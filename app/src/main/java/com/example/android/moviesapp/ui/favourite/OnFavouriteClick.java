package com.example.android.moviesapp.ui.favourite;

public interface OnFavouriteClick {
    <T> void onItemClick(String operation, T model);
}