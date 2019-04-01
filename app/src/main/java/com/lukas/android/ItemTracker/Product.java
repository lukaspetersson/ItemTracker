package com.lukas.android.ItemTracker;

public class Product {

    private String mName;
    private int mDurability;
    private String mBarcode;

    public Product(String name, int durability, String barcode) {
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
    public String getBarcode() {
        return mBarcode;
    }
}