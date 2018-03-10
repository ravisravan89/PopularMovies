package com.ravisravan.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ravisravankumar on 19/12/15.
 */
public class MoviesResponse implements Serializable {

    @SerializedName("results")
    private ArrayList<MovieObject> movieObjectsAl;

    @SerializedName("page")
    private int page;

    public ArrayList<MovieObject> getMovieObjectsAl() {
        return movieObjectsAl;
    }

    public int getPage() {
        return page;
    }

    public class MovieObject implements Serializable{
        @SerializedName("poster_path")
        private String poster;

        @SerializedName("adult")
        private boolean isAdult;

        @SerializedName("overview")
        private String overView;

        @SerializedName("release_date")
        private String releaseDate;

        @SerializedName("genre_ids")
        private int [] genre_ids;

        @SerializedName("id")
        private long id;

        @SerializedName("original_title")
        private String originalTitle;

        @SerializedName("original_language")
        private String originalLanguage;

        @SerializedName("title")
        private String title;

        @SerializedName("backdrop_path")
        private String backDropPath;

        @SerializedName("popularity")
        private float popularity;

        @SerializedName("vote_count")
        private long voteCount;

        @SerializedName("video")
        private boolean hasVideo;

        @SerializedName("vote_average")
        private float voteAverage;


        public String getPoster() {
            return poster;
        }

        public boolean isAdult() {
            return isAdult;
        }

        public String getOverView() {
            return overView;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public long getId() {
            return id;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public String getTitle() {
            return title;
        }

        public String getBackDropPath() {
            return backDropPath;
        }

        public float getPopularity() {
            return popularity;
        }

        public long getVoteCount() {
            return voteCount;
        }

        public boolean isHasVideo() {
            return hasVideo;
        }

        public float getVoteAverage() {
            return voteAverage;
        }
    }
}
