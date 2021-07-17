package com.example.android.moviesapp.util;

import com.example.android.moviesapp.data.local.Constants;
import com.example.android.moviesapp.databinding.LayoutTrailerBinding;
import com.example.android.moviesapp.data.model.Trailers.TrailersResults;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public final class SetupYoutubeThumbnail {

    public static void initialize(LayoutTrailerBinding binding, TrailersResults currentItem) {
        // Initialize the thumbnail image view, we need to pass Developer Key
        binding.videoThumbnailImageView.initialize(Constants.Api_key_youtube,
                new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(
                            YouTubeThumbnailView youTubeThumbnailView,
                            final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                        // When initialization is success, set the video id to thumbnail to load it
                        youTubeThumbnailLoader.setVideo(currentItem.getKey());

                        // Release the thumbnail loader
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(
                                new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                                    @Override
                                    public void onThumbnailLoaded(
                                            YouTubeThumbnailView youTubeThumbnailView, String s) {
                                        //when thumbnail loaded successfully release
                                        // the thumbnail loader as we are showing thumbnail
                                        // in adapter
                                        youTubeThumbnailLoader.release();
                                    }

                                    // When error occurs on thumbnail
                                    @Override
                                    public void onThumbnailError(
                                            YouTubeThumbnailView youTubeThumbnailView,
                                            YouTubeThumbnailLoader.ErrorReason errorReason) {
                                    }
                                });
                    }

                    // When initialization failed
                    @Override
                    public void onInitializationFailure(
                            YouTubeThumbnailView youTubeThumbnailView,
                            YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });
    }
}