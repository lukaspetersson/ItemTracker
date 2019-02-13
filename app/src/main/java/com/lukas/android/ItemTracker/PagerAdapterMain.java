package com.lukas.android.ItemTracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PagerAdapterMain extends FragmentPagerAdapter {

    public PagerAdapterMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0: return ItemListFragment.newInstance(-1);
            case 1: return ItemListFragment.newInstance(0);
            case 2: return ItemListFragment.newInstance(1);
            case 3: return ItemListFragment.newInstance(2);
            case 4: return ItemListFragment.newInstance(3);
            case 5: return ItemListFragment.newInstance(4);
            case 6: return ItemListFragment.newInstance(5);
            default: return ItemListFragment.newInstance(0);
        }
    }

    @Override
    public int getCount() {
        return 7;
    }
}