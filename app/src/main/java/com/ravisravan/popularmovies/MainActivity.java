package com.ravisravan.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMovieItemSelectedListener {

    Toolbar toolbar;
    String mSortingOrder = "";
    boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(getString(R.string.title_main));
        toolbar.setTitleTextColor(R.color.white);
        setSupportActionBar(toolbar);
        mSortingOrder = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.sort_order_key), "");

        if (findViewById(R.id.movie_details) != null) {
            //The detail container view will be present only in the large screen layouts,
            //If this view is present then activty should be in two pane mode,
            //so this should be true.
            mTwoPane = true;
            //In two pane mode show detail view in this activity by adding or replacing details fragment.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details, new DetailsActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();

            }
            findViewById(R.id.movie_details).setVisibility(View.INVISIBLE);
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortingOrder = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.sort_order_key), "");
        if (sortingOrder != null && !mSortingOrder.equals(sortingOrder)) {
            MainActivityFragment mainActivityFragment = (MainActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            if (null != mainActivityFragment) {
                mainActivityFragment.onSortingOrderChanged(sortingOrder);
            }
//            DetailActivityFragment df = (DetailActivityFragment) getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
//            if (null != df) {
//                df.onLocationChanged(location);
//            }
            mSortingOrder = sortingOrder;
        }
    }


    @Override
    public void onMovieItemClicked(MoviesResponse.MovieObject movieObject) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            View view = findViewById(R.id.movie_details);
            if(view.getVisibility()==View.INVISIBLE) {
                findViewById(R.id.movie_details).setVisibility(View.VISIBLE);
            }
            Bundle args = new Bundle();

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            args.putSerializable("movieObject", movieObject);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent detailsIntent = new Intent(this, DetailsActivity.class);
            detailsIntent.putExtra("movieObject", movieObject);
            startActivity(detailsIntent);
        }
    }
}
