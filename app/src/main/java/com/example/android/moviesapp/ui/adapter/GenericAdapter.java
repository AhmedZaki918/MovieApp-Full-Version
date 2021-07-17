package com.example.android.moviesapp.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;


public abstract class GenericAdapter<T, D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> list;
    public GenericAdapter(List<T> list) {
        this.list = list;
    }


    public abstract int getLayoutResId();
    public abstract void onBindData(T model, int position, D viewBinding);
    public abstract void onItemClick(T model, int position);


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResId(), parent, false);
        RecyclerView.ViewHolder holder = new ItemViewHolder(dataBinding);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        onBindData(list.get(position), position, (D) holder.itemView);
        holder.itemView.setOnClickListener(view -> onItemClick(list.get(position), position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        D viewBinding;

        public ItemViewHolder(ViewBinding binding) {
            super(binding.getRoot());
            viewBinding = (D) binding;
        }
    }
}