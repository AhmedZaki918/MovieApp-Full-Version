package com.example.android.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.activity.DetailsActivity;
import com.example.android.moviesapp.model.AllData;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * An {@link MainAdapter} knows how to create a list item layout for each movie
 * in the data source (a list of {@link AllData} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like GridView
 * to be displayed to the user.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private List<AllData> mAllDataList;

    // Constructor for our MainAdapter
    public MainAdapter(Context mContext, List<AllData> mAllDataList) {
        this.mContext = mContext;
        this.mAllDataList = mAllDataList;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  Id for the list item layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.layout_main;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the position of the current list item
        final AllData currentItem = mAllDataList.get(position);

        // Display views on screen
        holder.mTvTitle.setText(currentItem.getTitle());
        holder.mTvRate.setText(currentItem.getUserRating());

        // String variable to get poster url
        final String finalUrl = Constants.IMAGE_BASE_URL_ORIGINAL + currentItem.getPoster();

        // Display the image by Picasso library
        Picasso.with(mContext)
                .load(finalUrl)
                .into(holder.mIvPoster);

        // Set on click listener on the {movie poster} to go to Details Activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra(Constants.EXTRA_DATA, currentItem);
            mContext.startActivity(intent);
        });
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movies
     */
    @Override
    public int getItemCount() {
        return mAllDataList != null ? mAllDataList.size() : 0;
    }

    /**
     * Cache of the children views for a list item.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the views
        private ImageView mIvPoster;
        private TextView mTvTitle;
        private TextView mTvRate;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and ImageView and set an onClickListener to listen for clicks.
         *
         * @param itemView The View that you inflated in
         *                 {@link FavouriteAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        ViewHolder(View itemView) {
            super(itemView);
            // Find a reference for the views
            mIvPoster = itemView.findViewById(R.id.iv_poster);
            mTvTitle = itemView.findViewById(R.id.tv_movie_title);
            mTvRate = itemView.findViewById(R.id.tv_rating);
        }
    }
}