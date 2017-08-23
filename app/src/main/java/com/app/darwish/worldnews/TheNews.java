package com.app.darwish.worldnews;


import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.darwish.worldnews.adapter.DrowableListAdapter;
import com.app.darwish.worldnews.adapter.NewsItemAdapter;
import com.app.darwish.worldnews.data.NewsData;
import com.app.darwish.worldnews.data.ScroleListData;
import com.app.darwish.worldnews.data.SourceItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TheNews extends Fragment {

    public final String ApiKey = "96c44530bf994e24b8691c407182a6c6";
    public final String[] language = {"en", "fr", "de"};
    public final String url = "https://newsapi.org/v1/articles";
    // max resulat
    private int max = 30;
    //tpe of sort
    private String sortby;
    //get class name
    private final String classTAG = getClass().getName();
    //instialise my adapter
    private NewsItemAdapter newsItemAdapter;
    //inslize listview
    private ListView listView;
    //inslise arra list of news item
    private ArrayList<NewsData> listnewsDatas = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerpan;
    private ListView mDrawerList;
    private DrowableListAdapter drowableListAdapter;
    private ArrayList<ScroleListData> scrolelistDatas;


    public TheNews() {
        // Required empty public constructor
        scrolelistDatas = new ArrayList<>();
        scrolelistDatas.add(new ScroleListData(R.drawable.sport, "Sport"));
        scrolelistDatas.add(new ScroleListData(R.drawable.business, "business"));
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack("home", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_the_news, container, false);
        //generate bundle
        Bundle arguments = getArguments();
        SourceItem sourceItem;

        try {
            if (arguments != null) {
                sourceItem = arguments.getParcelable("JsonObject");
                //Toast.makeText(getActivity(),sourceItem.getUrl(),Toast.LENGTH_LONG).show();

                if ((sourceItem.getSortby()[1]).length() > 0) {
                    sortby = sourceItem.getSortby()[1];
                } else {
                    sortby = sourceItem.getSortby()[0];
                }

                Uri uri = Uri.parse(url).buildUpon().appendQueryParameter("source", sourceItem.getId())
                        .appendQueryParameter("sortBy", sortby).
                                appendQueryParameter("apiKey", ApiKey).build();
                fetchData(uri.toString());

            }

            newsItemAdapter = new NewsItemAdapter(getActivity(), R.layout.newsitem, listnewsDatas);
            listView = (ListView) v.findViewById(R.id.newsList);
            listView.setAdapter(newsItemAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsData newsData = newsItemAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("articlItem", newsData);
                    WebNews webNews = new WebNews();
                    webNews.setArguments(bundle);
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.replaceWithWeb, (Fragment) webNews).addToBackStack("articles").commit();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(classTAG, e.getMessage());

        }

        return v;
    }


    public void fetchData(final String url) {

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
                        listnewsDatas = arrangeJasonString(response);
                        updataLayout(listnewsDatas);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //ImageView imageView = (ImageView) getView().findViewById(R.id.newssourceimage);

                        Log.e(classTAG, error.getMessage());

                    }
                });

        queue.add(jsObjRequest);


    }

    public ArrayList<NewsData> arrangeJasonString(JSONObject Parent) {

        String source = "source";
        String sortBy = "sortBy";
        final String author = "author";
        final String description = "description";
        final String title = "title";
        final String url = "url";
        final String urlToImage = "urlToImage";
        final String publishedAt = "publishedAt";

        ///***************create array list for carry data
        ArrayList<NewsData> packet = new ArrayList<>();
        //get data from jsonobject and set it into array list
        try {
            source = Parent.getString(source);
            sortBy = Parent.getString(sortBy);

            JSONArray sources = Parent.getJSONArray("articles");
            JSONObject Data;
            for (int i = 0; i < sources.length(); i++) {
                Data = sources.getJSONObject(i);

                NewsData newsData = new NewsData();

                newsData.setSource(source);
                newsData.setSortby(sortBy);
                newsData.setAuthor(Data.getString(author));
                newsData.setDescription(Data.getString(description));
                newsData.setTitle(Data.getString(title));
                newsData.setUrl(Data.getString(url));
                newsData.setUrlToImage(Data.getString(urlToImage));
                newsData.setPublishedAt(Data.getString(publishedAt));

                packet.add(newsData);
                if (i >= max) {
                    break;
                }
            }
            return packet;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(classTAG, e.getMessage());
            return null;

        }

    }

    public void updataLayout(ArrayList<NewsData> x) {
        if (!x.isEmpty()) {
            newsItemAdapter.clear();

            for (NewsData c : x) {

                newsItemAdapter.add(c);

            }
        }


    }

    public interface Callback {
        void linlSelected(NewsData webPage);
    }

}
