package com.lukas.android.ItemTracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.lukas.android.ItemTracker.data.ItemContract.ItemEntry;


public class ItemProvider extends ContentProvider {

    // URI matcher number for the content URI for all items
    private static final int ITEMS = 100;
    // URI matcher number for the content URI for a single item
    private static final int ITEMS_ID = 101;
    // URI matcher number for the content URI for all products
    private static final int PRODUCTS = 200;
    // URI matcher number for the content URI for a single product
    private static final int PRODUCTS_ID = 201;

    //create UriMatcher to match with the correct number
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // static initializer, runs first time class is called
    static {
        //the calls to addURI() go here, for all of the content URI patterns that the provides should recognize
        //paths added to UriMatcher so that the correct number can be returned when match is found
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY_ITEMS, ItemContract.PATH_ITEMS, ITEMS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY_ITEMS, ItemContract.PATH_ITEMS + "/#", ITEMS_ID);

        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY_PRODUCTS, ItemContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY_PRODUCTS, ItemContract.PATH_PRODUCTS + "/#", PRODUCTS_ID);
    }

    //database helper object
    private ItemDbHelper mDbHelper;

    //tag for log messages
    public static final String LOG_TAG = com.lukas.android.ItemTracker.data.ItemProvider.class.getSimpleName();

    //create the database helper
    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                    cursor = database.query(ItemEntry.TABLE_NAME_ITEMS, projection, selection, selectionArgs,
                            null, null, sortOrder);

                break;
            case ITEMS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ItemEntry.TABLE_NAME_ITEMS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCTS:
                cursor = database.query(ItemEntry.TABLE_NAME_PRODUCTS, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case PRODUCTS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ItemEntry.TABLE_NAME_PRODUCTS, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //set notification on the cursor so it can be updated only if we need to change it
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // return the cursor
        return cursor;
    }

    //insert new data into the provider with the given ContentValues
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            //we only need this one case because you can only insert 1 book at the time
            case ITEMS:
                return insertItem(uri, contentValues);
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        String name = values.getAsString(ItemEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }

        Integer expire = values.getAsInteger(ItemEntry.COLUMN_EXPIRE);
        if (expire < 1) {
            throw new IllegalArgumentException("Item requires a expire date");
        }

        Integer barcode = values.getAsInteger(ItemEntry.COLUMN_BARCODE);
        if (barcode > 99999999999L) {
            throw new IllegalArgumentException("Item requires valid barcode id");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ItemEntry.TABLE_NAME_ITEMS, null, values);
        //if id is -1 there is an error
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
    private Uri insertProduct(Uri uri, ContentValues values) {
        String name = values.getAsString(ItemEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer durability = values.getAsInteger(ItemEntry.COLUMN_DURABILITY);
        if (durability == 0) {
            throw new IllegalArgumentException("Product requires a durability");
        }

        Integer barcode = values.getAsInteger(ItemEntry.COLUMN_BARCODE);
        if (barcode > 99999999999L) {
            throw new IllegalArgumentException("Product requires valid barcode id");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ItemEntry.TABLE_NAME_PRODUCTS, null, values);
        //if id is -1 there is an error
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEMS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String name = values.getAsString(ItemEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }

        Integer expire = values.getAsInteger(ItemEntry.COLUMN_EXPIRE);
        if (expire < 1) {
            throw new IllegalArgumentException("Item requires a expire date");
        }

        Integer barcode = values.getAsInteger(ItemEntry.COLUMN_BARCODE);
        if (barcode > 99999999999L) {
            throw new IllegalArgumentException("Item requires valid barcode id");
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ItemEntry.TABLE_NAME_ITEMS, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String name = values.getAsString(ItemEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer durability = values.getAsInteger(ItemEntry.COLUMN_DURABILITY);
        if (durability == 0) {
            throw new IllegalArgumentException("Product requires a durability");
        }

        Integer barcode = values.getAsInteger(ItemEntry.COLUMN_BARCODE);
        if (barcode > 99999999999L) {
            throw new IllegalArgumentException("Product requires valid barcode id");
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ItemEntry.TABLE_NAME_PRODUCTS, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    //delete data from table
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME_ITEMS, selection, selectionArgs);
                break;
            case ITEMS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME_ITEMS, selection, selectionArgs);
                break;
            case PRODUCTS:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME_PRODUCTS, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME_PRODUCTS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        //if 1 or more rows were deleted, then notify all listeners that the data at the given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //return the number of rows deleted
        return rowsDeleted;
    }

    //returns the MIME type of data for the content URI
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemEntry.CONTENT_LIST_TYPE_ITEMS;
            case ITEMS_ID:
                return ItemEntry.CONTENT_ITEM_TYPE_ITEMS;
            case PRODUCTS:
                return ItemEntry.CONTENT_LIST_TYPE_PRODUCTS;
            case PRODUCTS_ID:
                return ItemEntry.CONTENT_ITEM_TYPE_PRODUCTS;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
