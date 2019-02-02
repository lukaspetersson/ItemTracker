package com.lukas.android.ItemTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListAdapterMain extends ArrayAdapter<Item> {

    public ListAdapterMain(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_layout, parent, false);
        }

        Item currentItem = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.item_title);
        titleView.setText(currentItem.getTitle());

        TextView dateView = listItemView.findViewById(R.id.item_expire);
        DateFormat formatter = new SimpleDateFormat("d/M");
        String date = formatter.format(currentItem.getDate());
        dateView.setText(date);

        return listItemView;
    }
}
