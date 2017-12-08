package com.app.darwish.worldnews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.darwish.worldnews.R;
import com.app.darwish.worldnews.data.ScroleListData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Darwish on 8/1/2017.
 */

public class DrawableListAdapter extends ArrayAdapter<ScroleListData> {

    private Context contextThere;
    private ArrayList<ScroleListData> scroleList;

    public DrawableListAdapter(Context context, int resource, ArrayList<ScroleListData> objects) {
        super(context, resource, objects);
        contextThere=context;
        scroleList=objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.scroleitem, null);
        }
        ScroleListData listData=scroleList.get(position);
        ImageView imageView=(ImageView)v.findViewById(R.id.listIcon);
        TextView textView=(TextView)v.findViewById(R.id.item_list_title);
        //Log.v(getClass().getName(),listData.getTitle());
        imageView.setImageResource(listData.getIcon());
        //Picasso.with(contextThere).load(listData.getIcon()).into(imageView);
        textView.setText(listData.getTitle());
        return v;
    }
}
