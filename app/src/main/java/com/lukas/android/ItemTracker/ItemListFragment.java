package com.lukas.android.ItemTracker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

import com.lukas.android.ItemTracker.barcodereader.BarcodeItemActivity;
import com.lukas.android.ItemTracker.data.ItemContract;

import java.util.ArrayList;

public class ItemListFragment  extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListAdapterMain mAdapter;
    private ListView itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);

        mAdapter = new ListAdapterMain(getActivity(), null);

        itemList = v.findViewById(R.id.item_list);
        itemList.setAdapter(mAdapter);

        itemList.addHeaderView(new View(getContext()));
        itemList.addFooterView(new View(getContext()));

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri pickedUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI_ITEMS, id);

                //check if item already is in calendar
                Cursor cursor = getActivity().getContentResolver().query(pickedUri, null, null, null, null);

                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME);
                    String name = cursor.getString(nameColumnIndex);
                    int expireColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_EXPIRE);
                    long expire = cursor.getLong(expireColumnIndex);
                    int barcodeColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_BARCODE);
                    String barcode = cursor.getString(barcodeColumnIndex);
                    int crossedColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CROSSED);
                    int crossed = cursor.getInt(crossedColumnIndex);
                    cursor.close();

                    final ContentValues insertValues = new ContentValues();
                    insertValues.put(ItemContract.ItemEntry.COLUMN_NAME, name);
                    insertValues.put(ItemContract.ItemEntry.COLUMN_EXPIRE, expire);
                    insertValues.put(ItemContract.ItemEntry.COLUMN_BARCODE, barcode);

                    if(crossed == 0){
                        insertValues.put(ItemContract.ItemEntry.COLUMN_CROSSED, 1);
                        int rowsAffected = getActivity().getContentResolver().update(pickedUri, insertValues, null, null);
                    }else{
                        AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).create();
                        mAlertDialog.setTitle(getString(R.string.handle_item_title));
                        mAlertDialog.setMessage(getString(R.string.handle_item_subtitle));
                        mAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.restore),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        insertValues.put(ItemContract.ItemEntry.COLUMN_CROSSED, 0);
                                        int rowsAffected = getActivity().getContentResolver().update(pickedUri, insertValues, null, null);
                                    }
                                });
                        mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.delete),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        int rowsDeleted = getActivity().getContentResolver().delete(pickedUri, null, null);
                                    }
                                });
                            if(!mAlertDialog.isShowing()){
                                mAlertDialog.show();
                            }
                    }

                }
            }
        });

        long dayInMilis = MainActivity.currentDate + getArguments().getInt("day") * 86400000;
        Bundle bundle = new Bundle();
        bundle.putLong("displayDay", dayInMilis);

        getLoaderManager().restartLoader(0, bundle, this);

        return v;
    }

    public static ItemListFragment newInstance(int day) {

        ItemListFragment f = new ItemListFragment();
        Bundle b = new Bundle();
        b.putInt("day", day);
        f.setArguments(b);

        return f;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String selection =
                "strftime('%Y%m%d', " + ItemContract.ItemEntry.COLUMN_EXPIRE + " / 1000, 'unixepoch') = '" +
                        MainActivity.sameDayCheckerformatter.format(bundle.getLong("displayDay")) + "'";

        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME,
                ItemContract.ItemEntry.COLUMN_EXPIRE,
                ItemContract.ItemEntry.COLUMN_CROSSED,
                ItemContract.ItemEntry.COLUMN_BARCODE
        };

        return new CursorLoader(getActivity(),
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