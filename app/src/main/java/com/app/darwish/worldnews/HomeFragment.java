package com.app.darwish.worldnews;

import android.app.ActionBar;
import android.net.Uri;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.darwish.worldnews.adapter.DrowableListAdapter;
import com.app.darwish.worldnews.adapter.myAdapter;
import com.app.darwish.worldnews.data.NewsData;
import com.app.darwish.worldnews.data.ScroleListData;
import com.app.darwish.worldnews.data.SourceItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {
    public final String ApiKey = "96c44530bf994e24b8691c407182a6c6";
    public final String[] language = {"en", "fr", "de"};
    public final String url = "https://newsapi.org/v1/sources";
    public final String[] countryList = {"us", " de", "gb", "in", "it", "au"};
    // max resulat
    private int max = 20;
    //Array lis of news resources json
    ArrayList<SourceItem> news_channel = new ArrayList<>();
    //get class name
    private final String classTAG = getClass().getName();
    /***inslise grid adapter ****/
    private myAdapter customAdapter;

    private GridView gridView;
    //////////////***********************************//////////////////////
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerpan;
    private ListView mDrawerList;
    private DrowableListAdapter drowableListAdapter;
    private ArrayList<ScroleListData> scrolelistDatas;


    //////////////////////////////////////////////////////////////
    public HomeFragment() {
        //  , entertainment, gaming, general, music, politics, science_and_nature, , technology
        scrolelistDatas = new ArrayList<>();
        scrolelistDatas.add(new ScroleListData(R.drawable.general, "general"));
        scrolelistDatas.add(new ScroleListData(R.drawable.sport, "Sport"));
        scrolelistDatas.add(new ScroleListData(R.drawable.business, "business"));
        scrolelistDatas.add(new ScroleListData(R.drawable.entertainment, "entertainment"));
        scrolelistDatas.add(new ScroleListData(R.drawable.gaming, "gaming"));
        scrolelistDatas.add(new ScroleListData(R.drawable.music, "music"));
        scrolelistDatas.add(new ScroleListData(R.drawable.politics, "politics"));
        scrolelistDatas.add(new ScroleListData(R.drawable.science_and_ature, "science-and-nature"));
        scrolelistDatas.add(new ScroleListData(R.drawable.technology, "technology"));

    }

    //////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Uri uri = Uri.parse(url).buildUpon().
                appendQueryParameter("language", language[0]).
                appendQueryParameter("apiKey", ApiKey).appendQueryParameter("country", countryList[0]).build();

        fetchData(uri.toString());

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        customAdapter = new myAdapter(getActivity(), R.layout.griditem, news_channel);

        gridView = (GridView) view.findViewById(R.id.newsResouresList);
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SourceItem x = customAdapter.getItem(position);
                ((Callback)getActivity()).onItemSelected(x);


            }
        });

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        mDrawerpan = (RelativeLayout) view.findViewById(R.id.DrowPan);
        mDrawerList = (ListView) view.findViewById(R.id.DrowList);
        drowableListAdapter = new DrowableListAdapter(getActivity(), R.layout.scroleitem, scrolelistDatas);
        mDrawerList.setAdapter(drowableListAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScroleListData listData = drowableListAdapter.getItem(position);
                Uri uri1=Uri.parse(url).buildUpon().appendQueryParameter("language", language[0]).
                        appendQueryParameter("apiKey", ApiKey).appendQueryParameter("country", countryList[0])
                        .appendQueryParameter("category",listData.getTitle()).build();
                fetchData(uri1.toString());


            }
        });

        return view;
    }




    public interface Callback {
        void onItemSelected(SourceItem news_Channel);
    }

    public void fetchData(String url) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //*********************
                        /* 1 - fetch data from network
                        2- sort data in array list
                        3- update view of layout
                         */

                        news_channel = arrangeJasonString(response);
                        updataLayout(news_channel);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        ImageView imageView = (ImageView) getView().findViewById(R.id.newssourceimage);

                        Log.e(classTAG, error.getMessage());

                    }
                });

        queue.add(jsObjRequest);


    }

    public ArrayList<SourceItem> arrangeJasonString(JSONObject Parent) {

        final String id = "id";
        final String name = "name";
        final String description = "description";
        final String url = "url";
        final String category = "category";
        final String language = "language";
        final String country = "country";
        final String sortBysAvailable = "sortBysAvailable";

        ///***************create array list for carry data
        ArrayList<SourceItem> packet = new ArrayList<>();
        //get data from jsonobject and set it into array list

        try {
            JSONArray sources = Parent.getJSONArray("sources");
            JSONObject newsData;
            for (int i = 0; i < sources.length(); i++) {
                newsData = sources.getJSONObject(i);
                SourceItem newsource = new SourceItem();
                newsource.setId(newsData.getString(id));
                newsource.setName(newsData.getString(name));
                newsource.setUrl(newsData.getString(url));
                newsource.setCategory(newsData.getString(category));
                newsource.setDescription(newsData.getString(description));
                newsource.setLanguage(newsData.getString(language));
                JSONArray sort = newsData.getJSONArray(sortBysAvailable);
                String sorttype[] = new String[sort.length()];
                for (int x = 0; x < sort.length(); x++) {
                    sorttype[x] = sort.getString(x);
                }
                newsource.setSortby(sorttype);

                packet.add(newsource);
                if (i >= max) {
                    break;
                }
            }
            return packet;

        } catch (Exception e) {
            Log.e(classTAG, "json error", e);
            return null;


        }

    }

    public void updataLayout(ArrayList<SourceItem> x) {
        if (!x.isEmpty()) {
            customAdapter.clear();

            for (SourceItem c : x) {

                customAdapter.add(c);

            }
        }


    }




}
