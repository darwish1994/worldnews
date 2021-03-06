package com.app.darwish.worldnews.contentProviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Darwish on 8/21/2017.
 */

public class NewsContract {
    //creat content provider
    //content Authority
    public static final String Content_Authority = "com.app.darwish.worldnews";
    //content base header
    public static final Uri Base_Content_uri = Uri.parse("content://" + Content_Authority);

    //uri for news resourse
    public static final String Path_News_sources = "sources";
    //uri for news articles
    public static final String Path_NEWS_ARTICLES = "articles";

    //create database for news resourses
    public static final class SoureTable implements BaseColumns {
        //content uri for sources table

        public static final Uri CONTENT_URI = Base_Content_uri.
                buildUpon().appendPath(Path_News_sources).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                        + "/" + Content_Authority + "/" +
                        Path_News_sources;
        public static final String CONTENT_item_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + Content_Authority + "/" +
                Path_News_sources;

        //table name
        public static final String tableName = "News_sources";
        //coluom name
        public static final String COLUME_SOURCE_ID = "S_id";
        public static final String COLUME_SOURCE_NAME = "S_name";
        public static final String COLUME_SOURCE_DESCRIPTION = "S_description";
        public static final String COLUME_SOURCE_URL = "S_url";
        public static final String COLUME_SOURCE_CATOUGRY = "S_catougry";
        public static final String COLUME_SOURCE_LANGUAGE = "S_language";
        public static final String COLUME_SOURCE_CONUTRY = "S_country";
        public static final String COLUME_SOURCE_SORTBY = "S_sortby";
        public static final String COLUME_SOURCE_IMG_LOCATION = "S_img_location";


    }


}
