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
        public final static String TABLE_NAME = "items";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_AUTHOR = "author";
        public final static String COLUMN_END_DATE = "end_date";
        public final static String COLUMN_BEGIN_DATE = "begin_date";
        public final static String COLUMN_STATUS = "status";
        public final static String COLUMN_RATING = "rating";
        public final static String COLUMN_THUMBNAIL = "thumbnail";
        public final static String COLUMN_NOTES = "notes";
        public final static String COLUMN_VISIBILITY = "visibility";


        //Possible values for the status of item
        public static final int STATUS_TO_READ = 0;
        public static final int STATUS_READING = 1;
        public static final int STATUS_FINISHED = 2;

        //returns whether the status is either of the options
        public static boolean isValidStatus(int status) {
            if (status == STATUS_TO_READ || status == STATUS_READING || status == STATUS_FINISHED) {
                return true;
            }
            return false;
        }

    }
}
