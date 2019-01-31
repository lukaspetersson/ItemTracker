package com.lukas.android.ItemTracker;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    TextView dayDisplay;
    ImageView previousDay;
    ImageView nextDay;
    FloatingActionButton scanFab;
    ViewPager itemsList;

    AdapterMain mAdapter;

    long currentDate;
    private final long MILI_IN_DAY = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayDisplay = findViewById(R.id.display_day);
        nextDay = findViewById(R.id.next_day);
        previousDay = findViewById(R.id.previous_day);
        scanFab = findViewById(R.id.scan_fab);
        itemsList = findViewById(R.id.items_pager);

        String[] test = {"hej", "då"};

        mAdapter = new AdapterMain(this, test);
        itemsList.setAdapter(mAdapter);

        currentDate = System.currentTimeMillis();
        setDayDisplay();
    }

    public void openScan(View view) {

    }

    public void goToNextDay(View view) {
        currentDate += MILI_IN_DAY;
        setDayDisplay();
    }

    public void goToPreviousDay(View view) {
        currentDate -= MILI_IN_DAY;
        setDayDisplay();
    }

    private void setDayDisplay() {
        DateFormat formatter = new SimpleDateFormat("d/M");
        dayDisplay.setText(formatter.format(currentDate));

        //reload adapter
    }
}
