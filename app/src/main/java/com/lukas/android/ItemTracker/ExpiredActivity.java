package com.lukas.android.ItemTracker;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;


import com.lukas.android.ItemTracker.data.ItemContract;

import java.util.ArrayList;

public class ExpiredActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListAdapterMain mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);
        setTitle(R.string.expired_title);

        mAdapter = new ListAdapterMain(this, null);

        ListView itemList = findViewById(R.id.expired_list);
        itemList.setAdapter(mAdapter);

        itemList.addHeaderView(new View(this));
        itemList.addFooterView(new View(this));

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri pickedUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI_ITEMS, id);
                Cursor cursor = getContentResolver().query(pickedUri, null, null, null, null);

                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    int expireColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_EXPIRE);
                    long expire = cursor.getLong(expireColumnIndex);

                    Intent openMain = new Intent(ExpiredActivity.this, MainActivity.class);
                    openMain.putExtra("expire", expire);
                    startActivity(openMain);
                }
                if(cursor != null){
                    cursor.close();
                }
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public @NonNull Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_EXPIRE,
                ItemContract.ItemEntry.COLUMN_BARCODE,
                ItemContract.ItemEntry.COLUMN_CROSSED
        };

        String selection =
                "strftime('%Y%m%d', " + ItemContract.ItemEntry.COLUMN_EXPIRE + " / 1000, 'unixepoch') < '" +
                        MainActivity.sameDayCheckerformatter.format(System.currentTimeMillis()) + "'"
                        + " AND " +ItemContract.ItemEntry.COLUMN_CROSSED+ "!=" + 1;

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI_ITEMS,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
