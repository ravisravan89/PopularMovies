package com.ravisravan.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    final int OPTIMUMSIZE = 185;
    private static String TAG = MainActivityFragment.class.getSimpleName();
    ArrayList<MoviesResponse.MovieObject> movieObjectAL = new ArrayList<MoviesResponse.MovieObject>();
    PosterRecyclerAdapter posterRecyclerAdapter;
    RecyclerView posterRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        renderRecyclerGrid(view);
        return view;
    }

    private void renderRecyclerGrid(View view) {
        posterRecyclerView = (RecyclerView) view.findViewById(R.id.posterRecyclerView);
        posterRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int recyclerWidth = posterRecyclerView.getWidth();
                posterRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                posterRecyclerAdapter = new PosterRecyclerAdapter(getActivity(), movieObjectAL);
                posterRecyclerView.setHasFixedSize(true);
                posterRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), recyclerWidth / OPTIMUMSIZE));
                posterRecyclerView.setAdapter(posterRecyclerAdapter);
                return false;
            }
        });



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add this line on order for the fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Check for internet availability and make a call to the movie db api to
        //get the list of popular movies.
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            //Passing empty string will get movies list sorted by original title in ascending order.
            requestServer();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renderRecyclerGrid(getView());
    }

    //called when ever the fragment is visible again.
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //Clears the menu items already inflated in the activity.
        menu.clear();
        //Inflates the menu items from the menu file.
        inflater.inflate(R.menu.menu_main_frgment, menu);
    }

    public void onSortingOrderChanged(String newSorting) {
        if (!newSorting.equals(getString(R.string.sort_favourite))) {
            requestServer();
        } else {
//            Collections.sort(movieObjectAL, new Comparator<MoviesResponse.MovieObject>() {
//                @Override
//                public int compare(MoviesResponse.MovieObject obj1, MoviesResponse.MovieObject obj2) {
//                    boolean obj1IsFavourite = PreferenceManager.getDefaultSharedPreferences(getActivity())
//                            .getBoolean(String.valueOf(obj1.getId()), false);
//                    boolean obj2IsFavourite = PreferenceManager.getDefaultSharedPreferences(getActivity())
//                            .getBoolean(String.valueOf(obj2.getId()), false);
//                    int b1 = obj1IsFavourite ? 1 : 0;
//                    int b2 = obj2IsFavourite ? 1 : 0;
//                    return b2 - b1;
//                }
//            });

            ArrayList<MoviesResponse.MovieObject> favList = new ArrayList<MoviesResponse.MovieObject>();
            for(MoviesResponse.MovieObject movieObject : movieObjectAL){
                boolean isFavourite = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(String.valueOf(movieObject.getId()),false);
                if(isFavourite){
                    favList.add(movieObject);
                }
            }
            movieObjectAL.clear();
            movieObjectAL.addAll(favList);
            posterRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
        requestServer();
        return super.onOptionsItemSelected(item);
    }

    private void requestServer() {
        movieObjectAL.clear();
        String sortValue = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_order_key), "");
        if (sortValue.equals(getString(R.string.sort_favourite))) {
            sortValue = getString(R.string.sort_popularity);
        }
        final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        String api_key = "api_key";
        //TODO remove this key before uploading the project
        String api_value = "1234567890";
        String sort_key = "sort_by";

        //Build the url with the params and values.
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendQueryParameter(api_key, api_value)
                .appendQueryParameter(sort_key, sortValue).build();

        String URL = builtUri.toString();

        //pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        MoviesResponse moviesResponse = new Gson().fromJson(response.toString(), MoviesResponse.class);
                        int page = moviesResponse.getPage();
                        ArrayList<MoviesResponse.MovieObject> tempMovieList = moviesResponse.getMovieObjectsAl();
                        if (tempMovieList != null && tempMovieList.size() > 0) {
                            movieObjectAL.addAll(tempMovieList);
                            String sortValue = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_order_key), "");
                            if (sortValue.equals(getString(R.string.sort_favourite))) {
                                onSortingOrderChanged(sortValue);
                            } else {
                                posterRecyclerAdapter.notifyDataSetChanged();
                            }

                        } else{

                        }
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



    public interface OnMovieItemSelectedListener {
        public void onMovieItemClicked(MoviesResponse.MovieObject movieObject);
    }
}
