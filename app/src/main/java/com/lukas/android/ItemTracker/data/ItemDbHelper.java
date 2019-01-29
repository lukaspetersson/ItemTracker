package com.lukas.android.ItemTracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lukas.android.ItemTracker.data.ItemContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {

    //name database file
    private static final String DATABASE_NAME = "bookshelf.db";

    //give unique version
    private static final int DATABASE_VERSION = 2;

    //create new instance of the database helper
    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creates the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create a String that contains the SQL statement to create the Books table
        //columns are made different types of values and extra specifications
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_AUTHOR + " TEXT, "
                + ItemEntry.COLUMN_STATUS + " INTEGER NOT NULL, "
                + ItemEntry.COLUMN_BEGIN_DATE + " INTEGER, "
                + ItemEntry.COLUMN_END_DATE + " INTEGER, "
                + ItemEntry.COLUMN_RATING + " INTEGER DEFAULT 0, "
                + ItemEntry.COLUMN_NOTES + " TEXT, "
                + ItemEntry.COLUMN_VISIBILITY + " INTEGER, "
                + ItemEntry.COLUMN_THUMBNAIL + " TEXT);";
        // execute the SQL statement
        db.execSQL(SQL_CREATE_BOOKS_TABLE);


    }

    //this is called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing, database still version 1
    }
}

