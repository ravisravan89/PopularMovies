package com.ravisravan.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ravisravankumar on 07/02/16.
 */
public class ReviewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ReviewsResponse reviewsResponse;
    ListView reviews_list_view;
    ReviewsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        String title = getIntent().getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.toolbar_reviews);
        toolbar.setTitle(title + " Reviews");
        toolbar.setTitleTextColor(R.color.white);
        setSupportActionBar(toolbar);
        reviews_list_view = (ListView)findViewById(R.id.reviews_list_view);
        reviewsResponse = (ReviewsResponse)getIntent().getSerializableExtra("reviewsObject");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new ReviewsListAdapter(reviewsResponse.getReviewObjectAL(),this);
        reviews_list_view.setAdapter(mAdapter);
        TextView titleTextView = (TextView) findViewById(R.id.title);
        int count = reviewsResponse.getTotalresults();
        String reviews = getResources().getQuantityString(R.plurals.reviews_qty,count,count);
        titleTextView.setText(reviews);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
