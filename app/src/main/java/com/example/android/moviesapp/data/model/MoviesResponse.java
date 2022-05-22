package com.example.android.moviesapp.data.model;

/* **
 * An {@link MoviesResponse} object contains information related to a movie.
 */

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(tableName = "movie")
public class MoviesResponse implements Parcelable {

    //Initialization
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("original_title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPoster;

    @SerializedName("backdrop_path")
    public String mBackdrop;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("vote_average")
    private String mUserRating;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("results")
    private List<MoviesResponse> results = null;


    // Constructs a new {@link AllData} object
    public MoviesResponse(String mTitle, String mPoster, String mOverview, String mUserRating,
                          String mReleaseDate) {
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }


    // Default Constructor
    @Ignore
    public MoviesResponse() {
    }


    // Constructor used for parcel
    protected MoviesResponse(Parcel in) {
        id = in.readInt();
        mTitle = in.readString();
        mPoster = in.readString();
        mBackdrop = in.readString();
        mOverview = in.readString();
        mUserRating = in.readString();
        mReleaseDate = in.readString();
        results = in.createTypedArrayList(MoviesResponse.CREATOR);
    }


    // Used when un-parceling our parcel (creating the object)
    public static final Creator<MoviesResponse> CREATOR = new Creator<MoviesResponse>() {
        @Override
        public MoviesResponse createFromParcel(Parcel in) {
            return new MoviesResponse(in);
        }

        @Override
        public MoviesResponse[] newArray(int size) {
            return new MoviesResponse[size];
        }
    };


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


    // Getter
    public String getTitle() {
        return mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public int getId() {
        return id;
    }

    public List<MoviesResponse> getResults() {
        return results;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    // Setter
    public void setResults(List<MoviesResponse> results) {
        this.results = results;
    }

    public void setId(int id) {
        this.id = id;
    }
}