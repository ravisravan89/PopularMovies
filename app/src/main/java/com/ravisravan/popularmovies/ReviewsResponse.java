package com.ravisravan.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ravisravankumar on 07/02/16.
 */
public class ReviewsResponse implements Serializable {

    @SerializedName("results")
    private ArrayList<ReviewObject> reviewObjectArrayList;

    @SerializedName("id")
    private long id;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("total_results")
    private int total_results;

    public ArrayList<ReviewObject> getReviewObjectAL() {
        return reviewObjectArrayList;
    }

    public long getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public int getTotalresults() {
        return total_results;
    }

    public class ReviewObject implements Serializable {
        @SerializedName("id")
        private String reviewId;

        @SerializedName("author")
        private String author;

        @SerializedName("content")
        private String content;

        @SerializedName("url")
        private String url;

        public String getReviewId() {
            return reviewId;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }
}
