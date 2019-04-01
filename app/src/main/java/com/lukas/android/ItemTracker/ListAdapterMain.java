package com.lukas.android.ItemTracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lukas.android.ItemTracker.data.ItemContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListAdapterMain extends CursorAdapter {

    public ListAdapterMain(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
    }

    @Override
    public void bindView(View listItemView, Context context, Cursor cursor) {

        TextView nameView = listItemView.findViewById(R.id.item_title);
        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME);
        nameView.setText(cursor.getString(nameColumnIndex));

        TextView barcodeView = listItemView.findViewById(R.id.item_barcode);
        int barcodeColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_BARCODE);
        barcodeView.setText(cursor.getString(barcodeColumnIndex));

        int crossColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CROSSED);
        int crossed = cursor.getInt(crossColumnIndex);
        if(crossed == 1){
            nameView.setPaintFlags(nameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            barcodeView.setPaintFlags(barcodeView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            listItemView.setBackgroundColor(Color.parseColor("#c3c5c9"));
        }else{
            nameView.setPaintFlags(0);
            barcodeView.setPaintFlags(0);
            listItemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }
}
