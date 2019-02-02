package com.lukas.android.ItemTracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ExpiredActivity extends AppCompatActivity {

    private ListAdapterMain mAdapter;
    private ListView itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);
        setTitle(R.string.expired_title);

        mAdapter = new ListAdapterMain(this, new ArrayList<Item>());

        itemList = findViewById(R.id.expired_list);
        itemList.setAdapter(mAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Item currentItem = mAdapter.getItem(position);

                Intent openMain = new Intent(ExpiredActivity.this, MainActivity.class);
                openMain.putExtra("date", currentItem.getDate());
                startActivity(openMain);

            }
        });

        Item[] test = new Item[3];
        test[0] = new Item("first", 1549110410009L);
        test[1] = new Item("second", 1549310180009L);
        test[2] = new Item("first", 1549210440009L);

        mAdapter.clear();
        mAdapter.addAll(test);

    }
}
