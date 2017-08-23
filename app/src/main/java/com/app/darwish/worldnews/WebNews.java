package com.app.darwish.worldnews;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.app.darwish.worldnews.data.NewsData;


public class WebNews extends Fragment {

    public WebNews() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_news, container, false);
        WebView webView=(WebView)view.findViewById(R.id.webView);
        try {
            Bundle arguments = getArguments();
            NewsData newsData = arguments.getParcelable("articlItem");
            webView.loadUrl(newsData.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(),e.getMessage());
            Toast.makeText(getActivity(),"please conect to internet",Toast.LENGTH_LONG).show();

        }

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack("articlItem",0);
    }

}
