package com.app.darwish.worldnews.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.darwish.worldnews.R;
import com.app.darwish.worldnews.data.NewsData;
import com.app.darwish.worldnews.data.SourceItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darwish on 7/30/2017.
 */

public class NewsItemAdapter extends ArrayAdapter<NewsData> {
    private ArrayList<NewsData> sourceList;
    private Context newsconext;
    private int comeResourse;


    public NewsItemAdapter(Context context, int resource, ArrayList<NewsData> objects) {
        super(context, resource, objects);
        sourceList = objects;
        newsconext = context;
        comeResourse = resource;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        //View view=convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(newsconext);
            v = vi.inflate(R.layout.newsitem, null);
        }

        NewsData tempObject = sourceList.get(position);
        //***********************************************
        ImageView imageView = (ImageView) v.findViewById(R.id.newsImage);
        TextView title = (TextView) v.findViewById(R.id.newsTitle);
        TextView description = (TextView) v.findViewById(R.id.newsDescription);
        TextView authour = (TextView) v.findViewById(R.id.newsAuthor);
        TextView publishAt = (TextView) v.findViewById(R.id.newsPublishAt);

        //************load data into adapter
        Picasso.with(newsconext).load(tempObject.getUrlToImage()).into(imageView);
        title.setText(tempObject.getTitle());
        description.setText(tempObject.getDescription());
        authour.setText(tempObject.getAuthor());
        publishAt.setText(tempObject.getPublishedAt());

        return v;
    }

}
