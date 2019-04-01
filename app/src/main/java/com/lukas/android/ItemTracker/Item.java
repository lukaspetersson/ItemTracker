package com.lukas.android.ItemTracker;

public class Item {

    private String mName;
    private long mExpire;
    private String mBarcode;
    private int mCrossed;


    public Item(String name, long expire, String barcode, int crossed) {
        mName = name;
        mExpire = expire;
        mBarcode = barcode;
        mCrossed = crossed;

    }

    public String getName() {
        return mName;
    }

    public long getExpire() {
        return mExpire;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public int getCrossed() {
        return mCrossed;
    }


}