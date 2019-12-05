package com.example.android.moviesapp.model;

/* **
 * An {@link MovieData} object contains information related to a movie.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(tableName = "movie")
public class MovieData implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private int id;

    /**
     * Name of the title
     */
    @SerializedName("original_title")
    private String mTitle;

    /**
     * Name of the poster
     */
    @SerializedName("poster_path")
    private String mPoster;

    /**
     * The backdrop of the poster
     */
    @SerializedName("backdrop_path")
    public String mBackdrop;


    /**
     * The overview
     */
    @SerializedName("overview")
    private String mOverview;

    /**
     * User & Rating
     */
    @SerializedName("vote_average")
    private String mUserRating;

    /**
     * Release date
     */
    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("results")
    private List<MovieData> results = null;

    /**
     * Constructs a new {@link MovieData} object.
     */
    public MovieData(String mTitle, String mPoster, String mOverview, String mUserRating, String mReleaseDate) {
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    @Ignore
    public MovieData() {
    }

    // Constructor used for parcel
    protected MovieData(Parcel in) {
        id = in.readInt();
        mTitle = in.readString();
        mPoster = in.readString();
        mBackdrop = in.readString();
        mOverview = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        results = in.createTypedArrayList(MovieData.CREATOR);
    }

    // Used when un-parceling our parcel (creating the object)
    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };


    // Get the results array
    public List<MovieData> getResults() {
        return results;
    }

    // Setters
    public void setResults(List<MovieData> results) {
        this.results = results;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Return hashcode of object
    @Override
    public int describeContents() {
        return 0;
    }

    // Write object values to parcel for storage
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mBackdrop);
        dest.writeString(mOverview);
        dest.writeString(mUserRating);
        dest.writeString(mReleaseDate);
        dest.writeTypedList(results);
    }

    // Get the original title
    public String getTitle() {
        return mTitle;
    }

    // Get the poster of the movie
    public String getPoster() {
        return mPoster;
    }


    // Get the overview of the movie
    public String getOverview() {
        return mOverview;
    }

    // Get the average vote for the users
    public String getUserRating() {
        return mUserRating;
    }

    // Get the release date of the movie
    public String getReleaseDate() {
        return mReleaseDate;
    }

    // Get the id of the movie
    public int getId() {
        return id;
    }

    public String getmBackdrop() {
        return mBackdrop;
    }
}