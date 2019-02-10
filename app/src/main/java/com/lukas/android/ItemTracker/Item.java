package com.lukas.android.ItemTracker;

public class Item {

    private String mName;
    private long mExpire;
    private long mBarcode;


    public Item(String name, long expire, long barcode) {
        mName = name;
        mExpire = expire;
        mBarcode = barcode;

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


}