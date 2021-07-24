package com.example.android.moviesapp.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.ui.favourite.FavouriteActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public final class ViewUtils {


    @SuppressWarnings("rawtypes")
    public static void setupRecyclerView(RecyclerView recyclerView,
                                         LinearLayoutManager layoutManager,
                                         RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @SuppressWarnings("rawtypes")
    public static void setupRecyclerView(RecyclerView recyclerView,
                                         GridLayoutManager layoutManager,
                                         RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    public static void showSnackBar(View view, int resource) {
        Snackbar.make(view, resource, Snackbar.LENGTH_SHORT).show();
    }


    public static void showSnackBar(View view, int resource, Context context) {
        Snackbar.make(view, resource, Snackbar.LENGTH_LONG)
                .setAction(R.string.see_list, view1 ->
                        context.startActivity(new Intent(context,
                                FavouriteActivity.class))).setActionTextColor(Color.CYAN).show();
    }


    public static String formatNumber(String number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(Double.parseDouble(number));
    }


    public static void setupGlide(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .into(view);
    }


    public static void startActivity(Context context, Class<?> cls, String name, Parcelable value) {
        context.startActivity(new Intent(context, cls)
                .putExtra(name, value));
    }
}