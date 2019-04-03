package com.lukas.android.ItemTracker;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.content.ContentUris;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.AdapterView;

import com.lukas.android.ItemTracker.data.ItemContract;

public class ItemListFragment  extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListAdapterMain mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);

        mAdapter = new ListAdapterMain(getActivity(), null);

        ListView itemList = v.findViewById(R.id.item_list);
        itemList.setAdapter(mAdapter);

        itemList.addHeaderView(new View(getContext()));
        itemList.addFooterView(new View(getContext()));

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri pickedUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI_ITEMS, id);


                Cursor cursor = null;
                if (getActivity() != null) {
                    cursor = getActivity().getContentResolver().query(pickedUri, null, null, null, null);

                }
                //check if item already is in calendar
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
                        if(getActivity() != null){
                            getActivity().getContentResolver().update(pickedUri, insertValues, null, null);
                        }
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
                                        getActivity().getContentResolver().update(pickedUri, insertValues, null, null);
                                    }
                                });
                        mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.delete),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getActivity().getContentResolver().delete(pickedUri, null, null);
                                    }
                                });
                            if(!mAlertDialog.isShowing()){
                                mAlertDialog.show();
                            }
                    }

                }
            }
        });
        long dayInMilis = 0;
        if(getArguments() != null){
            dayInMilis = MainActivity.currentDate + getArguments().getInt("day") * 86400000;

        }
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
    public @NonNull Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}