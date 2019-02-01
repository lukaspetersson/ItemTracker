package com.lukas.android.ItemTracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListFragment  extends Fragment {

    private static ListAdapterMain mAdapter;
    private ListView itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);

        itemList = (ListView) v.findViewById(R.id.item_list);
        itemList.setAdapter(mAdapter);

        return v;
    }

    public static ItemListFragment newInstance(Integer day, Context context) {

        mAdapter = new ListAdapterMain(context, new ArrayList<String>());

        String[] test = {"hej", "då"+day};

        mAdapter.clear();
        mAdapter.addAll(test);

        return new ItemListFragment();
    }
}