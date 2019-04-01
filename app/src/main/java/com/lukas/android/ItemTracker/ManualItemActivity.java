package com.lukas.android.ItemTracker;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.widget.Toast;

import com.lukas.android.ItemTracker.barcodereader.BarcodeItemActivity;
import com.lukas.android.ItemTracker.data.ItemContract;
import com.lukas.android.ItemTracker.data.ItemDbHelper;

public class ManualItemActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SearchView searchField;

    private ListAdapterProduct mAdapter;

    private ManualItemActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual);
        setTitle(R.string.manual_item_title);

        if(getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
        }

        searchField = findViewById(R.id.search_view);
        ListView searchListView = findViewById(R.id.search_list);

        mAdapter = new ListAdapterProduct(this, null);
        searchListView.setAdapter(mAdapter);

        context = this;

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri currentProductUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI_PRODUCTS, id);
                Cursor data = getContentResolver().query(currentProductUri, null, null, null, null);
                if(data != null) {
                    if (data.moveToFirst()) {
                        int nameColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME);
                        final String name = data.getString(nameColumnIndex);
                        int durabilityColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_DURABILITY);
                        final int durability = data.getInt(durabilityColumnIndex);
                        int barcodeColumnIndex = data.getColumnIndex(ItemContract.ItemEntry.COLUMN_BARCODE);
                        final String barcode = data.getString(barcodeColumnIndex);
                        data.close();

                        ItemDbHelper mDbHelper = new ItemDbHelper(context);
                        SQLiteDatabase db = mDbHelper.getReadableDatabase();

                        String[] projection = {
                                ItemContract.ItemEntry._ID,
                                ItemContract.ItemEntry.COLUMN_BARCODE,
                                ItemContract.ItemEntry.COLUMN_CROSSED
                        };

                        String selection = ItemContract.ItemEntry.COLUMN_BARCODE + "=?" + " AND " + ItemContract.ItemEntry.COLUMN_CROSSED + "!=" + 1;
                        String[] selectionArgs = new String[]{barcode};

                        //check if item already is in calendar
                        Cursor cursor = db.query(ItemContract.ItemEntry.TABLE_NAME_ITEMS, projection,
                                selection, selectionArgs, null, null, null);

                        if (cursor.getCount() > 0) {
                            AlertDialog mAlertDialog;
                            mAlertDialog = new AlertDialog.Builder(ManualItemActivity.this).create();
                            mAlertDialog.setTitle(getString(R.string.duplicate_item_title));
                            mAlertDialog.setMessage(name + getString(R.string.duplicate_item_subtitle));
                            mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            addToCalendar(name, durability, barcode);
                                        }
                                    });
                            mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            if (!mAlertDialog.isShowing()) {
                                mAlertDialog.show();
                            }
                        } else {
                            addToCalendar(name, durability, barcode);
                        }
                        cursor.close();
                    }
                }
            }
        });

        getSupportLoaderManager().initLoader(0, null, context);

        setuUpSearch();
    }

    private void addToCalendar(String name, int durability, String barcode) {

        long expire = System.currentTimeMillis() + (durability*86400000);

        ContentValues insertValues = new ContentValues();
        insertValues.put(ItemContract.ItemEntry.COLUMN_NAME, name);
        insertValues.put(ItemContract.ItemEntry.COLUMN_EXPIRE, expire);
        insertValues.put(ItemContract.ItemEntry.COLUMN_BARCODE, barcode);
        insertValues.put(ItemContract.ItemEntry.COLUMN_CROSSED, 0);

        Uri uri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI_ITEMS, insertValues);

        if (uri == null) {
            Toast.makeText(this, getString(R.string.error),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.insert_product_successful),
                    Toast.LENGTH_SHORT).show();
        }

        Intent backToMain = new Intent(ManualItemActivity.this, MainActivity.class);
        startActivity(backToMain);
    }

    private void setuUpSearch(){
        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("text", query);
                getSupportLoaderManager().restartLoader(0, bundle, context);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Bundle bundle = new Bundle();
                bundle.putString("text", newText);
                getSupportLoaderManager().restartLoader(0, bundle, context);
                return false;
            }
        });
    }

    @Override
    public @NonNull Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_DURABILITY,
                ItemContract.ItemEntry.COLUMN_BARCODE
        };

        String selection = null;
        String[] selectionArgs = null;

        if(bundle != null){
            selection = ItemContract.ItemEntry.COLUMN_NAME + " like ?";
            selectionArgs = new String[]{"%" + bundle.getString("text") + "%"};
        }

        return new CursorLoader(this,
                ItemContract.ItemEntry.CONTENT_URI_PRODUCTS,
                projection,
                selection,
                selectionArgs,
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
