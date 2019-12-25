package com.example.android.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.moviesapp.R;
import com.example.android.moviesapp.model.Trailers.Results;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

/**
 * An {@link TrailerAdapter} knows how to create a list item layout for each movie
 * in the data source (a list of {@link Results} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like GridView
 * to be displayed to the user.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    Context context;
    private List<Results> results;

    // Constructor for our TrailerAdapter
    public TrailerAdapter(Context context, List<Results> results) {
        this.context = context;
        this.results = results;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param i         Id for the list item layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.layout_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerAdapter.ViewHolder(view);
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
        final Results currentItem = results.get(position);

        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(Constants.Api_key_youtube, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(currentItem.getKey());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
//                        Toast.makeText(context, "Youtube Thumbnail Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
//                Toast.makeText(context, "Youtube Initialization Failure", Toast.LENGTH_SHORT).show();
            }
        });

        // Set on click listener on the trailer to go to youtube website
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the key of the trailer
                String Key = currentItem.getKey();

                // Go to the youtube website by the Intent
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + Key));
                intent.putExtra("VIDEO_ID", Key);
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
        return results != null ? results.size() : 0;
    }

    /**
     * Cache of the children views for a list item.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the views
        ConstraintLayout constraintLayout;
        YouTubeThumbnailView videoThumbnailImageView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and Imageview and set an onClickListener to listen for clicks.
         *
         * @param itemView The View that you inflated in
         *                 {@link TrailerAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public ViewHolder(View itemView) {
            super(itemView);

            // Find a reference for the views
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        }
    }
}