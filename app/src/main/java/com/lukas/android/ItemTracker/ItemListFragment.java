package com.lukas.android.ItemTracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemListFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);



        TextView tv = (TextView) v.findViewById(R.id.tvFragThird);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static ItemListFragment newInstance(Integer day) {

        //load day accordingly to what "day" is passed in

        ItemListFragment f = new ItemListFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}