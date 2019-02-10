package com.lukas.android.ItemTracker.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {


    //Content authority is a name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.lukas.android.items";

    //use Content authority to create the base of all URI's which apps will use to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //URI´s path
    public static final String PATH_ITEMS = "items";

    // empty constructor to prevent someone from accidentally instantiating the contract class
    private ItemContract() {
    }

    public static abstract class ItemEntry implements BaseColumns {

        //The MIME type of the for a list of items
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        //The MIME type of the for a single item
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        // access item data in provider with the content URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

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
