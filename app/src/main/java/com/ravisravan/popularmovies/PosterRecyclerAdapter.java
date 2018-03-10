package com.ravisravan.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ravisravankumar on 19/12/15.
 */
public class PosterRecyclerAdapter extends RecyclerView.Adapter<PosterRecyclerAdapter.PosterViewHolder> {

    String size = "w342";
    static Context context;
    ArrayList<MoviesResponse.MovieObject> movieObjectsAL;
    MainActivityFragment.OnMovieItemSelectedListener listener;

    public PosterRecyclerAdapter(Context context, ArrayList<MoviesResponse.MovieObject> movieObjectsAL) {
        this.context = context;
        this.movieObjectsAL = movieObjectsAL;
        listener = (MainActivityFragment.OnMovieItemSelectedListener)context;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_item, viewGroup, false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder posterViewHolder, final int i) {
        String baseUrl = "http://image.tmdb.org/t/p/";
        //String size = this.size;
        Picasso.with(context).load(baseUrl + size + "/" + movieObjectsAL.get(i).getPoster()).placeholder(R.drawable.no_poster).into(posterViewHolder.posterImageView);
        posterViewHolder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieItemClicked(movieObjectsAL.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieObjectsAL.size();
    }

    //A view holder class that is used to get control over the view of each recycler object.
    public static class PosterViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.poster_image);
        }
    }
}
