package com.example.android.moviesapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.model.Reviews.Results;

import java.util.List;


/**
 * An {@link ReviewAdapter} knows how to create a list item layout for each movie
 * in the data source (a list of {@link Results} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like GridView
 * to be displayed to the user.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    @SuppressWarnings("WeakerAccess")
    Context context;
    private List<Results> mResultsList;

    // Constructor for our ReviewAdapter
    public ReviewAdapter(Context context, List<Results> mResultsList) {
        this.context = context;
        this.mResultsList = mResultsList;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param i         Id for the list item layout
     * @return A new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.layout_review;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapter.ViewHolder(view);
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
        Results currentItem = mResultsList.get(position);

        // Display views on screen
        holder.mReadMore.setText(currentItem.getContent());
        holder.mTvAuthor.setText(currentItem.getAuthor());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movies
     */
    @Override
    public int getItemCount() {
        return mResultsList != null ? mResultsList.size() : 0;
    }

    /**
     * Cache of the children views for a list item.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the views
        private ReadMoreTextView mReadMore;
        private TextView mTvAuthor;
        TextView tvReviewLabel;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and Imageview and set an onClickListener to listen for clicks.
         *
         * @param itemView The View that you inflated in
         *                 {@link ReviewAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        ViewHolder(View itemView) {
            super(itemView);
            // Find a reference for the views
            mReadMore = itemView.findViewById(R.id.tv_reviews);
            mTvAuthor = itemView.findViewById(R.id.tv_author);
            tvReviewLabel = itemView.findViewById(R.id.tv_reviews_label);
        }
    }
}