package com.ravisravan.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    LinearLayoutManager mLayoutManager;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView header;
    MoviesResponse.MovieObject movieObject;

    final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    //TODO remove this key before uploading the project
    String api_key_value = "12356789";
    final int PURPOSE_TRAILER = 1;
    final int PURPOSE_REVIEWS = 2;
    final String TRAILER_QUERY = "/videos?";
    final String REVIEW_QUERY = "/reviews?";


    private static String TAG = DetailsActivityFragment.class.getSimpleName();

    public DetailsActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        if (getArguments() != null) {
            movieObject = (MoviesResponse.MovieObject) getArguments().getSerializable("movieObject");
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                requestServer(TRAILER_QUERY, PURPOSE_TRAILER, movieObject.getId());
            } else {
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            bindDataToView(view);
            CheckBox isFavoriteCheckBox = (CheckBox) view.findViewById(R.id.isFavoriteCheckBox);
            isFavoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    preferences.edit().putBoolean(String.valueOf(movieObject.getId()), isChecked).commit();
                }
            });
            boolean isFavourite = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getBoolean(String.valueOf(movieObject.getId()), false);
            isFavoriteCheckBox.setChecked(isFavourite);
        }
        //view.findViewById(R.id.collapsing_toolbar);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        if (getArguments() != null) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.anim_toolbar);
            if (toolbar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                toolbar.hideOverflowMenu();
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.toolbar_color));
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.toolbar_color_dark));
        int titleColor = getResources().getColor(R.color.white);
        collapsingToolbar.setCollapsedTitleTextColor(titleColor);
        String baseUrl = "http://image.tmdb.org/t/p/";
        header = (ImageView) view.findViewById(R.id.header);
        if (getArguments() != null) {
            Picasso.with(getActivity()).load(baseUrl + "w500/" + movieObject.getBackDropPath()).placeholder(R.drawable.no_poster).into(header);
            Picasso.with(getActivity())
                    .load(baseUrl + "w500/" + movieObject.getBackDropPath())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    if (isAdded() && isVisible()) {
                                        int color = palette.getMutedColor(getResources().getColor(R.color.toolbar_color));
                                        collapsingToolbar.setContentScrimColor(color);
                                        collapsingToolbar.setStatusBarScrimColor(color);
                                        int titleColor = palette.getVibrantColor(getResources().getColor(R.color.white));
                                        collapsingToolbar.setCollapsedTitleTextColor(titleColor);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            collapsingToolbar.setTitle(movieObject.getTitle());
            TextView title = (TextView) getView().findViewById(R.id.title);
            if (title != null) {
                title.setText(movieObject.getTitle());
            }
        }

    }

    private void bindDataToView(View view) {
        //set release date
        setValuesToView(view.findViewById(R.id.release_date), getString(R.string.release_date), movieObject.getReleaseDate());
        //set original language
        setValuesToView(view.findViewById(R.id.original_language), getString(R.string.original_language), movieObject.getOriginalLanguage());
        //set overview
        setValuesToView(view.findViewById(R.id.overview), getString(R.string.overview), movieObject.getOverView());
        //set average ratigns
        setValuesToView(view.findViewById(R.id.average_rating), getString(R.string.average_rating), "Approx: " + movieObject.getVoteAverage());
        //set popularity
        setValuesToView(view.findViewById(R.id.popularity), getString(R.string.popularity), "Approx: " + movieObject.getPopularity());
        //set total rating
        setValuesToView(view.findViewById(R.id.total_rating), getString(R.string.total_rating), "Approx: " + movieObject.getVoteCount());
        //set show review
        setValuesToView(view.findViewById(R.id.reviews), getString(R.string.reviews), getString(R.string.read_reviews));
        view.findViewById(R.id.reviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    //Passing empty string will get movies list sorted by original title in ascending order.
                    requestServer(REVIEW_QUERY, PURPOSE_REVIEWS, movieObject.getId());
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setValuesToView(View view, String heading, String value) {
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView data = (TextView) view.findViewById(R.id.data);
        title.setText(heading);
        data.setText(value);
    }

    private void requestServer(String queryKey, final int purpose, long movieId) {
        //http://api.themoviedb.org/3/movie/273248/videos?api_key=f06d97e5a880bdb4d8f3cbaaa1e918e9
        final String GET_TRAILER_URL = BASE_URL + movieId + queryKey;
        //Build the url with the params and values.
        String api_key = "api_key";
        Uri builtUri = Uri.parse(GET_TRAILER_URL).buildUpon()
                .appendQueryParameter(api_key, api_key_value).build();
        String URL = builtUri.toString();
        //pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponseBasedOnPurpose(purpose, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });
        // add the request object to the queue to be executed
        RequestManager.getInstance(getActivity()).addToRequestQueue(req);
    }

    private void parseResponseBasedOnPurpose(int purpose, JSONObject response) {
        if (isAdded() && isVisible())
            switch (purpose) {
                case PURPOSE_TRAILER:
                    final VideoTrailersResponse trailerResponse = new Gson().fromJson(response.toString(), VideoTrailersResponse.class);
                    int trailers = trailerResponse.getTrailerObjectAL().size();
                    if (trailers == 0) {
                        ((TextView) getView().findViewById(R.id.trailers).findViewById(R.id.data)).setText(getString(R.string.no_trailers));
                    } else {
                        String trailersFound = getResources().getQuantityString(R.plurals.trailers_qty, trailers, trailers);
                        ((TextView) getView().findViewById(R.id.trailers).findViewById(R.id.data)).setText(trailersFound);
                        LinearLayout trailers_ll = (LinearLayout) getView().findViewById(R.id.trailers);
                        for (final VideoTrailersResponse.TrailerObject trailerObject : trailerResponse.getTrailerObjectAL()) {
                            View view = View.inflate(getActivity(), R.layout.trailer_item_view, null);
                            TextView textView = (TextView) view.findViewById(R.id.trailername);
                            textView.setText(trailerObject.getName());
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    watchYoutubeVideo(trailerObject.getKey());
                                }
                            });
                            trailers_ll.addView(view);
                        }
                    }
                    break;
                case PURPOSE_REVIEWS:
                    final ReviewsResponse reviewsResponse = new Gson().fromJson(response.toString(), ReviewsResponse.class);
                    int reviews = reviewsResponse.getTotalresults();
                    if (reviews > 0) {
                        Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                        intent.putExtra("title", movieObject.getTitle());
                        intent.putExtra("reviewsObject", reviewsResponse);
                        startActivity(intent);
                    } else {
                        ((TextView)getView().findViewById(R.id.reviews).findViewById(R.id.data)).setText(getString(R.string.no_reviews));
                    }
                    break;
            }
    }

    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }
}
