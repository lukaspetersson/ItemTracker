package com.lukas.android.ItemTracker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    TextView[] DateNumber;
    TextView[] DateName;
    ImageView NextWeek;
    ImageView PreviousWeek;
    FloatingActionButton ScanFab;
    ViewPager ItemsList;

    PagerAdapterMain mAdapter;

    DateFormat nameFormatter;
    DateFormat numberFormatter;

    long currentDate;
    private final long MILI_IN_DAY = 86400000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreviousWeek = findViewById(R.id.previous_week);
        NextWeek = findViewById(R.id.next_week);
        ScanFab = findViewById(R.id.scan_fab);
        ItemsList = findViewById(R.id.items_pager);

        currentDate = System.currentTimeMillis();
        nameFormatter = new SimpleDateFormat("E");
        numberFormatter = new SimpleDateFormat("d");

        DateNumber = new TextView[7];
        DateName = new TextView[7];
        for(int i=0; i<7; i++){
            int numberId = getResources().getIdentifier("num" + i, "id", getPackageName());
            int nameId = getResources().getIdentifier("name" + i, "id", getPackageName());

            DateNumber[i] = findViewById(numberId);
            DateName[i] = findViewById(nameId);

            DateNumber[i].setText(numberFormatter.format(currentDate));
            DateName[i].setText(nameFormatter.format(currentDate));

        }

        mAdapter =  new PagerAdapterMain(getSupportFragmentManager());
        ItemsList.setAdapter(mAdapter);
        ItemsList.setCurrentItem(1);


        setDayDisplay();
    }

    public void openScan(View view) {

    }

    public void goToDay(View view){

    }

    public void goToNextWeek(View view) {
        currentDate += MILI_IN_DAY;
        setDayDisplay();
        ItemsList.setCurrentItem(ItemsList.getCurrentItem() + 1);
    }

    public void goToPreviousWeek(View view) {
        currentDate -= MILI_IN_DAY;
        setDayDisplay();
        ItemsList.setCurrentItem(ItemsList.getCurrentItem() - 1);
    }

    private void setDayDisplay() {
        DateFormat formatter = new SimpleDateFormat("d/M");

        //reload adapter
    }
}
