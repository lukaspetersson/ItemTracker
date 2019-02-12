package com.lukas.android.ItemTracker;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lukas.android.ItemTracker.data.ItemContract;

public class AssortmentActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    ListAdapterAssortment mAssortmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
        setTitle(R.string.assortment_title);

        ListView assormentList = findViewById(R.id.assortment_list);

        mAssortmentAdapter = new ListAdapterAssortment(this, null);
        assormentList.setAdapter(mAssortmentAdapter);


        //makes added books clickable
        assormentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent openProduct = new Intent(AssortmentActivity.this, ProductActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI_PRODUCTS, id);
                openProduct.setData(currentBookUri);
                startActivity(openProduct);
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assortment_menu, menu);
        return true;
    }

    //if menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent openProduct = new Intent(AssortmentActivity.this, ProductActivity.class);
            startActivity(openProduct);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_DURABILITY,
                ItemContract.ItemEntry.COLUMN_BARCODE
                };

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI_PRODUCTS,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAssortmentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAssortmentAdapter.swapCursor(null);
    }
}
