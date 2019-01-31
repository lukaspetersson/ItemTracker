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


public class AdapterMain extends FragmentPagerAdapter {
    Context mContext;
    String[] mItems;
    LayoutInflater mInflater;



    public AdapterMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0: return ItemListFragment.newInstance(0);
            case 1: return ItemListFragment.newInstance(1);
            case 2: return ItemListFragment.newInstance(2);
            case 3: return ItemListFragment.newInstance(3);
            case 4: return ItemListFragment.newInstance(4);
            case 5: return ItemListFragment.newInstance(5);
            case 6: return ItemListFragment.newInstance(6);
            default: return ItemListFragment.newInstance(2);
        }
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

  /*  @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.item_list,container,false);

        LinearLayout mItemLayout =  view.findViewById(R.id.item_layout);
        TextView mItemTitle= view.findViewById(R.id.item_title);

        mItemTitle.setText(mItems[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }*/
}