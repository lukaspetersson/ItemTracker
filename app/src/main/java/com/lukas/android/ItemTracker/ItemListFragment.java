package com.lukas.android.ItemTracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListFragment  extends Fragment {

    private ListAdapterMain mAdapter;
    private ListView itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_list, container, false);

        mAdapter = new ListAdapterMain(getActivity(), new ArrayList<String>());

        itemList = v.findViewById(R.id.item_list);
        itemList.setAdapter(mAdapter);

        String[] test = {"test"+getArguments().getInt("day"), "test", "test"};

        //mAdapter.clear();
        mAdapter.addAll(test);

        return v;
    }

    public static ItemListFragment newInstance(int day) {

        ItemListFragment f = new ItemListFragment();
        Bundle b = new Bundle();
        b.putInt("day", day);
        f.setArguments(b);

        return f;
    }
}