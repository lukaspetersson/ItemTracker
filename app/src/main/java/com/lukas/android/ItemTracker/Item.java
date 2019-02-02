package com.lukas.android.ItemTracker;

public class Item {

    private String mTitle;
    private long mDate;


    //make Book object
    public Item(String title, long date) {
        mTitle = title;
        mDate = date;

    }

    public String getTitle() {
        return mTitle;
    }

    public long getDate() {
        return mDate;
    }


}