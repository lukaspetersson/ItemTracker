package com.lukas.android.ItemTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterAssortment extends ArrayAdapter<Product> {

    public ListAdapterAssortment(Context context, ArrayList<Product> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.assortment_layout, parent, false);
        }

        Product currentProduct = getItem(position);

        TextView nameView = listItemView.findViewById(R.id.assortment_name);
        nameView.setText(currentProduct.getName());

        TextView durabilityView = listItemView.findViewById(R.id.assortment_durability);
        durabilityView.setText(currentProduct.getDurability()+"");

        TextView barcodeView = listItemView.findViewById(R.id.assortment_barcode);
        barcodeView.setText(currentProduct.getBarcode()+"");

        return listItemView;
    }
}