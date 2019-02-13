package com.lukas.android.ItemTracker;

public class Item {

    private String mName;
    private long mExpire;
    private long mBarcode;
    private int mCrossed;


    public Item(String name, long expire, long barcode, int crossed) {
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

    public long getBarcode() {
        return mBarcode;
    }

    public int getCrossed() {
        return mCrossed;
    }


}