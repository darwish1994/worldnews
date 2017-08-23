package com.app.darwish.worldnews.data;

/**
 * Created by Darwish on 8/1/2017.
 */

public class ScroleListData {

    private int icon;
    private String title;

    public ScroleListData(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
