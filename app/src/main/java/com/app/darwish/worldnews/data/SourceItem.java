package com.app.darwish.worldnews.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Darwish on 7/30/2017.
 */

public class SourceItem implements Parcelable {

   private String id ,name,description,url,category,language,country;

    public SourceItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        url = in.readString();
        category = in.readString();
        language = in.readString();
        country = in.readString();
        sortby = in.createStringArray();
    }

    public static final Creator<SourceItem> CREATOR = new Creator<SourceItem>() {
        @Override
        public SourceItem createFromParcel(Parcel in) {
            return new SourceItem(in);
        }

        @Override
        public SourceItem[] newArray(int size) {
            return new SourceItem[size];
        }
    };

    public SourceItem() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String[] getSortby() {
        return sortby;
    }

    public void setSortby(String[] sortby) {
        this.sortby = sortby;
    }

    private String [] sortby;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(category);
        dest.writeString(language);
        dest.writeString(country);
        dest.writeStringArray(sortby);
    }
}
