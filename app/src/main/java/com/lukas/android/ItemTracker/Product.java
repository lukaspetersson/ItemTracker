package com.lukas.android.ItemTracker;

public class Product {

    private String mName;
    private int mDurability;
    private long mBarcode;

    public Product(String name, int durability, long barcode) {
        mName = name;
        mDurability = durability;
        mBarcode = barcode;

    }

    public String getName() {
        return mName;
    }
    public int getDurability() {
        return mDurability;
    }
    public long getBarcode() {
        return mBarcode;
    }
}