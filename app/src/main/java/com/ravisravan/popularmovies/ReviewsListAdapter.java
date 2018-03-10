package com.ravisravan.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by ravisravankumar on 07/02/16.
 */
public class ReviewsListAdapter extends BaseAdapter {
    ArrayList<ReviewsResponse.ReviewObject> reviewObjectArrayList;
    Context mContext;

    ReviewsListAdapter(ArrayList<ReviewsResponse.ReviewObject> reviewObjectArrayList,Context context) {
            this.reviewObjectArrayList = reviewObjectArrayList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return reviewObjectArrayList.size();
    }

    @Override
    public ReviewsResponse.ReviewObject getItem(int position) {
        return reviewObjectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_item_layout,parent,false);
        } else {
            view = convertView;
        }
        TextView author = (TextView)view.findViewById(R.id.author);
        TextView content = (TextView)view.findViewById(R.id.content);
        author.setText(getItem(position).getAuthor());
        content.setText(getItem(position).getContent());
        return view;
    }
}
