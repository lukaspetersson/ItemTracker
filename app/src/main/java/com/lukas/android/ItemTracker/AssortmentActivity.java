package com.lukas.android.ItemTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AssortmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
        setTitle(R.string.assortment_title);
    }
}
