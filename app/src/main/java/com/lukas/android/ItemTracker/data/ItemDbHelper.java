package com.lukas.android.ItemTracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lukas.android.ItemTracker.data.ItemContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {

    //name database file
    private static final String DATABASE_NAME = "ItemTracker.db";

    //give unique version
    private static final int DATABASE_VERSION = 1;

    //create new instance of the database helper
    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creates the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME_ITEMS + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_EXPIRE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_BARCODE + " INTEGER NOT NULL);";

        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME_PRODUCTS + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_DURABILITY + " INTEGER NOT NULL, "
                + ItemEntry.COLUMN_BARCODE + " INTEGER NOT NULL);";



        // execute the SQL statement
        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
        db.execSQL(SQL_CREATE_ITEMS_TABLE);


    }

    //this is called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing, database still version 1
    }
}

