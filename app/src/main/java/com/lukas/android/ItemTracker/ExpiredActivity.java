package com.lukas.android.ItemTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);
        setTitle(R.string.expired_title);

        mAdapter = new ListAdapterMain(this, null);

        itemList = findViewById(R.id.expired_list);
        itemList.setAdapter(mAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Item currentItem = mAdapter.getItem(position);

                Intent openMain = new Intent(ExpiredActivity.this, MainActivity.class);
                openMain.putExtra("expire", currentItem.getExpire());
                startActivity(openMain);*/

            }
        });

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_EXPIRE,
                ItemContract.ItemEntry.COLUMN_BARCODE
        };

        long today = System.currentTimeMillis();

        String selection = ItemContract.ItemEntry.COLUMN_EXPIRE + "<" + today;

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI_ITEMS,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
