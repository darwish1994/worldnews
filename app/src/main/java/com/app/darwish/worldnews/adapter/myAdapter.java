package com.app.darwish.worldnews.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.darwish.worldnews.R;
import com.app.darwish.worldnews.data.SourceItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Darwish on 7/30/2017.
 */

public class myAdapter extends ArrayAdapter<SourceItem>  {
    private ArrayList <SourceItem > sourceList;
    private Context newsconext;
    private int comeResorce;

    public myAdapter( Context context, int resource , ArrayList<SourceItem> objects) {
        super(context, resource, objects);
        sourceList=objects;
        newsconext=context;
        comeResorce = resource;


    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(newsconext);
            view = vi.inflate(R.layout.griditem, null);
        }

        SourceItem tempObject = sourceList.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.newssourceimage);
        //String xx= tempObject.getUrl().replace("http://","");

        String imgUrl="https://logo.clearbit.com/"+tempObject.getUrl()+"?size=370";

        //Log.v(getClass().getName(),tempObject.getUrl());

        Picasso.with(newsconext)
                .load(imgUrl)
                .into(imageView);

        return view;
    }

    /*
    public class DownloadImage extends AsyncTask <String ,Void , Void>{


        @Override
        protected Void doInBackground(String... params) {
            return null;
        }
    }
*/
}
