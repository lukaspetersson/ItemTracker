package com.lukas.android.ItemTracker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;

import com.lukas.android.ItemTracker.data.ItemContract;

public class ManualItemActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SearchView searchField;
    private ListView bookListView;

    private ListAdapterProduct mAdapter;

    private ManualItemActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual);
        setTitle(R.string.manual_item_title);
        getSupportActionBar().setElevation(0);

        searchField = findViewById(R.id.search_view);
        bookListView = findViewById(R.id.search_list);

        mAdapter = new ListAdapterProduct(this, null);
        bookListView.setAdapter(mAdapter);

        context = this;

        setuUpSearch();
    }

    private void setuUpSearch(){
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSupportLoaderManager().restartLoader(0, null, context);
                return false;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_DURABILITY,
                ItemContract.ItemEntry.COLUMN_BARCODE
        };

        String selection = ItemContract.ItemEntry.COLUMN_BARCODE + " like '%?%'";
        String[] selectionArgs = new String[]{bundle.getString("name")};

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI_PRODUCTS,
                projection,
                selection,
                selectionArgs,
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
