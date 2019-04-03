package com.lukas.android.ItemTracker;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapterMain extends FragmentPagerAdapter {

    PagerAdapterMain(FragmentManager fm) {
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



    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 7;
    }
}