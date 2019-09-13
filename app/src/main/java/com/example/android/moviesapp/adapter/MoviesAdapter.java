package com.example.android.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.activity.DetailsActivity;
import com.example.android.moviesapp.model.MovieData;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * An {@link MoviesAdapter} knows how to create a list item layout for each movie
 * in the data source (a list of {@link MovieData} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like GridView
 * to be displayed to the user.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {


    Context context;
    private List<MovieData> movieData;

    // Constructor for our MoviesAdapter
    public MoviesAdapter(Context context, List<MovieData> movieData) {
        this.context = context;
        this.movieData = movieData;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  Id for the list item layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
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
        final MovieData currentItem = movieData.get(position);

        // String variable to get poster url
        final String finalUrl = Constants.IMAGE_BASE_URL + currentItem.getPoster();

        // Display the image by Picasso library
        Picasso.with(context).load(finalUrl)
                .into(holder.moviePoster);

        // Set on click listener on the {movie poster} to go to Details Activity
        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constants.SOURCE, currentItem);
                context.startActivity(intent);
            }
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
        return movieData != null ? movieData.size() : 0;
    }

    /**
     * Cache of the children views for a list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the view
        ImageView moviePoster;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and Imageview and set an onClickListener to listen for clicks.
         *
         * @param itemView The View that you inflated in
         *                 {@link FavouriteAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public ViewHolder(View itemView) {
            super(itemView);

            // Find a reference for the views
            moviePoster = itemView.findViewById(R.id.iv_poster);
        }
    }
}