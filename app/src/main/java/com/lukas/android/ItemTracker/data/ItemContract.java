package com.lukas.android.ItemTracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {


    public static final String CONTENT_AUTHORITY_ITEMS = "com.lukas.android.items";
    public static final String CONTENT_AUTHORITY_PRODUCTS = "com.lukas.android.products";


    public static final Uri BASE_CONTENT_URI_ITEMS = Uri.parse("content://" + CONTENT_AUTHORITY_ITEMS);
    public static final Uri BASE_CONTENT_URI_PRODUCTS = Uri.parse("content://" + CONTENT_AUTHORITY_PRODUCTS);

    public static final String PATH_ITEMS = "items";
    public static final String PATH_PRODUCTS = "products";

    private ItemContract() {
    }

    public static abstract class ItemEntry implements BaseColumns {

        //The MIME type of the for a list of items
        public static final String CONTENT_LIST_TYPE_ITEMS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_ITEMS + "/" + PATH_ITEMS;
        //The MIME type of the for a single item
        public static final String CONTENT_ITEM_TYPE_ITEMS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_ITEMS + "/" + PATH_ITEMS;

        //The MIME type of the for a list of products
        public static final String CONTENT_LIST_TYPE_PRODUCTS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY_PRODUCTS + "/" + PATH_PRODUCTS;
        //The MIME type of the for a single product
        public static final String CONTENT_ITEM_TYPE_PRODUCTS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY_PRODUCTS + "/" + PATH_PRODUCTS;

        // access item data in provider with the content URI
        public static final Uri CONTENT_URI_ITEMS = Uri.withAppendedPath(BASE_CONTENT_URI_ITEMS, PATH_ITEMS);
        public static final Uri CONTENT_URI_PRODUCTS = Uri.withAppendedPath(BASE_CONTENT_URI_PRODUCTS, PATH_PRODUCTS);

        //column names for the item table
        public final static String TABLE_NAME_PRODUCTS = "products";
        public final static String TABLE_NAME_ITEMS = "items";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_EXPIRE = "expire";
        public final static String COLUMN_DURABILITY = "durability";
        public final static String COLUMN_BARCODE = "barcode";
    }
}
