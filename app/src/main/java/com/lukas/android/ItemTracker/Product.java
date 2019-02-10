package com.lukas.android.ItemTracker;

public class Product {

    private String mTitle;
    private int mDurability;
    private int mBarcode;

    public Product(String title, int durability, int barcode) {
        mTitle = title;
        mDurability = durability;
        mBarcode = barcode;

    }

    public String getTitle() {
        return mTitle;
    }
    public int getDurability() {
        return mDurability;
    }
    public int getBarcode() {
        return mBarcode;
    }
}