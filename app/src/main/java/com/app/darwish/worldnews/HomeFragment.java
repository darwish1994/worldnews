package com.app.darwish.worldnews;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.app.darwish.worldnews.adapter.DrawableListAdapter;
import com.app.darwish.worldnews.adapter.myAdapter;
import com.app.darwish.worldnews.contentProviders.DbHelper;
import com.app.darwish.worldnews.contentProviders.NewsContract;
import com.app.darwish.worldnews.data.ScroleListData;
import com.app.darwish.worldnews.data.SourceItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {
    public final String ApiKey = "96c44530bf994e24b8691c407182a6c6";                //api user key
    public final String[] language = {"en", "fr", "de"};                            // all supported language
    public final String url = "https://newsapi.org/v1/sources";                     // url of getting news resources
    public final String[] countryList = {"us", " de", "gb", "in", "it", "au"};      // country list
    boolean end_flag = false;
    private int max, start = 0, length = 20;                                                     // item of resources that will appear
    ArrayList<SourceItem> news_channel = new ArrayList<>();                         //Array lis of news resources json
    private final String classTAG = getClass().getName();                           //get class name
    private myAdapter customAdapter;                                                // new instance of custom adapter
    private GridView gridView;                                                      // grid view that sources will be appear
    //////////////***********************************//////////////////////
    private DrawerLayout mDrawerLayout;                                             // layout that will be scroll
    private RelativeLayout mDrawerpan;
    private ListView mDrawerList;
    private DrawableListAdapter drawableListAdapter;
    private ArrayList<ScroleListData> scrollListData;


    //////////////////////////////////////////////////////////////
    public HomeFragment() {
        //  , entertainment, gaming, general, music, politics, science_and_nature, , technology
        scrollListData = new ArrayList<>();
        scrollListData.add(new ScroleListData(R.drawable.general, "general"));
        scrollListData.add(new ScroleListData(R.drawable.sport, "Sport"));
        scrollListData.add(new ScroleListData(R.drawable.business, "business"));
        scrollListData.add(new ScroleListData(R.drawable.entertainment, "entertainment"));
        scrollListData.add(new ScroleListData(R.drawable.gaming, "gaming"));
        scrollListData.add(new ScroleListData(R.drawable.music, "music"));
        scrollListData.add(new ScroleListData(R.drawable.politics, "politics"));
        scrollListData.add(new ScroleListData(R.drawable.science_and_ature, "science-and-nature"));
        scrollListData.add(new ScroleListData(R.drawable.technology, "technology"));


    }

    //////////////////////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data for share preferances
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int num_ch = Integer.parseInt(preferences.getString(getString(R.string.pref_channel_list_key), getString(R.string.pref_channel_list_deafoult_value)));
        max = num_ch;
        //length=num_ch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("language", language[0])
                .appendQueryParameter("apiKey", ApiKey)
                .appendQueryParameter("country", countryList[0])
                .build();

        fetchData(uri.toString());

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        updataLayout(news_channel, start, length);
        customAdapter = new myAdapter(getActivity(), R.layout.loading, news_channel);
        //customAdapter.notifyDataSetChanged();
        customAdapter = new myAdapter(getActivity(), R.layout.griditem, news_channel);
        gridView = (GridView) view.findViewById(R.id.newsResouresList);
        gridView.setAdapter(customAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (customAdapter.getItem(position) == null) {
                    updataLayout(news_channel, start, length);
                } else {
                SourceItem x = customAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(x);
            }
            }
        });

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        mDrawerpan = (RelativeLayout) view.findViewById(R.id.DrowPan);
        mDrawerList = (ListView) view.findViewById(R.id.DrowList);
        drawableListAdapter = new DrawableListAdapter(getActivity(), R.layout.scroleitem, scrollListData);
        mDrawerList.setAdapter(drawableListAdapter);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScroleListData listData = drawableListAdapter.getItem(position);
                Uri uri1 = Uri.parse(url).buildUpon().appendQueryParameter("language", language[0]).
                        appendQueryParameter("apiKey", ApiKey).appendQueryParameter("country", countryList[0])
                        .appendQueryParameter("category", listData.getTitle()).build();
                fetchData(uri1.toString());


            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updataLayout(news_channel, start, length);
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

//                        1 - fetch data from network
//                        2- sort data in array list
//                        3- update view of layout

                        news_channel = arrangeJasonString(response);
                        updataLayout(news_channel, start, length);
                        new DatabaseOpertation().execute(news_channel);

                    }
                }
                        , new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        // ImageView imageView = (ImageView) getView().findViewById(R.id.newssourceimage);

                        Toast.makeText(getActivity(), "please check network connection", Toast.LENGTH_LONG).show();
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

    public void updataLayout(ArrayList<SourceItem> x, int st, int end) {
        ArrayList<SourceItem> newAdd = new ArrayList<>();
        if (!x.isEmpty()) {
            newAdd.clear();
            int array_len = x.size();
            if (st == 0) {
                customAdapter.clear();
            }
            for (; st < end; st++) {
                if (st == array_len - 1) {
                    end_flag = true;
                    break;
                }
                newAdd.add(x.get(st));

            }
            customAdapter.addAll(newAdd);
            //customAdapter = new myAdapter(getActivity(), R.layout.griditem, newAdd);
            //customAdapter.notifyDataSetChanged();
            start = st;
            length = st + 30;
        }


    }


    public class DatabaseOpertation extends AsyncTask<ArrayList<SourceItem>, Void, Long> {


        @Override
        protected Long doInBackground(ArrayList<SourceItem>... params) {
            DbHelper helper = new DbHelper(getActivity());
            SQLiteDatabase liteDatabase = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            long result = 0;
            /*
            try {
                URL url=new URL("");
                HttpURLConnection URLConnection =(HttpURLConnection)url.openConnection();
                URLConnection.setRequestMethod("GET");
                URLConnection.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/

            for (ArrayList<SourceItem> c : params) {
                for (SourceItem c1 : c) {
                    //Log.v("ahmed darwish",c1.getUrl());

                    values.put(NewsContract.SoureTable.COLUME_SOURCE_ID, c1.getId());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_NAME, c1.getName());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_DESCRIPTION, c1.getDescription());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_CATOUGRY, c1.getCategory());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_URL, c1.getUrl());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_LANGUAGE, c1.getLanguage());
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_IMG_LOCATION, " ");
                    values.put(NewsContract.SoureTable.COLUME_SOURCE_CONUTRY, c1.getCountry());
                    result = liteDatabase.insert(NewsContract.SoureTable.tableName, null, values);

                }
            }
            liteDatabase.close();
            return result;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if (aLong > -1) {
                Toast.makeText(getActivity(), "data saved success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "errot on  saving data", Toast.LENGTH_LONG).show();
            }

        }
    }


}
