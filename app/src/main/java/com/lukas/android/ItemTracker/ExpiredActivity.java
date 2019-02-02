package com.lukas.android.ItemTracker;

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

        mAdapter = new ListAdapterMain(this, new ArrayList<String>());

        itemList = findViewById(R.id.expired_list);
        itemList.setAdapter(mAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.v("ExpiredActivity", "HHHHHHHHHHHHHHHh"+id+"HHH"+position);

            }
        });

        String[] test = {"test1", "test2", "test3"};

        mAdapter.clear();
        mAdapter.addAll(test);

    }
}
