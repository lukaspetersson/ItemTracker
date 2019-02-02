package com.lukas.android.ItemTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExpiredActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);
        setTitle(R.string.expired_title);
    }
}
