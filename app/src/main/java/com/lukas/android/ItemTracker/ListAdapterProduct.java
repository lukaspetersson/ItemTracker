package com.lukas.android.ItemTracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.lukas.android.ItemTracker.data.ItemContract;

import java.util.ArrayList;

public class ListAdapterProduct extends CursorAdapter {

    ListAdapterProduct(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.assortment_layout, parent, false);
    }

    @Override
    public void bindView(View listItemView, Context context, Cursor cursor) {

        TextView nameView = listItemView.findViewById(R.id.assortment_name);
        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME);
        nameView.setText(cursor.getString(nameColumnIndex));

        TextView durabilityView = listItemView.findViewById(R.id.assortment_durability);
        int durabilityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_DURABILITY);
        String durabilityString = cursor.getInt(durabilityColumnIndex)+"";
        durabilityView.setText(durabilityString);

        TextView barcodeView = listItemView.findViewById(R.id.assortment_barcode);
        int barcodeColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_BARCODE);
        barcodeView.setText(cursor.getString(barcodeColumnIndex));


    }
}
