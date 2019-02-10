package com.lukas.android.ItemTracker;

public class Product {

    private String mName;
    private int mDurability;
    private int mBarcode;

    public Product(String name, int durability, int barcode) {
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
    public int getBarcode() {
        return mBarcode;
    }
}