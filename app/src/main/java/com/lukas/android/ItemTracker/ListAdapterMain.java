package com.lukas.android.ItemTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterMain extends ArrayAdapter<String> {

    public ListAdapterMain(Context context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_layout, parent, false);
        }

        String currentItem = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.item_title);
        titleView.setText(currentItem);

        return listItemView;
    }
}
