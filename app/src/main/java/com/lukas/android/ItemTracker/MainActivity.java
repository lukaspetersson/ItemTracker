package com.lukas.android.ItemTracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lukas.android.ItemTracker.barcodereader.BarcodeCaptureActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView[] DateNumber;
    TextView[] DateName;
    LinearLayout[] DateLable;
    ImageView NextWeek;
    ImageView PreviousWeek;
    FloatingActionButton ScanFab;
    ViewPager ItemsList;

    PagerAdapterMain mAdapter;

    DateFormat nameFormatter;
    DateFormat numberFormatter;
    long currentDate;

    private final long MILIS_IN_DAY = 86400000;
    private final int DAYS_IN_WEEK = 7;
    private final int START_DAY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.calendar_title);

        getSupportActionBar().setElevation(0);

        PreviousWeek = findViewById(R.id.previous_week);
        NextWeek = findViewById(R.id.next_week);
        ScanFab = findViewById(R.id.scan_fab);
        ItemsList = findViewById(R.id.items_pager);

        Intent fromExpire = getIntent();
        long date = fromExpire.getLongExtra("date", System.currentTimeMillis());
        if (date != 0) {
            currentDate = date;
        }else{
            currentDate = System.currentTimeMillis();
        }
        nameFormatter = new SimpleDateFormat("E");
        numberFormatter = new SimpleDateFormat("d");

        setUpDateBar();
        setUpPager();
    }

    private void setUpDateBar(){
        DateNumber = new TextView[DAYS_IN_WEEK];
        DateName = new TextView[DAYS_IN_WEEK];
        DateLable = new LinearLayout[DAYS_IN_WEEK];
        for(int i=0; i<DAYS_IN_WEEK; i++){
            int numberId = getResources().getIdentifier("num" + i, "id", getPackageName());
            int nameId = getResources().getIdentifier("name" + i, "id", getPackageName());
            int labelId = getResources().getIdentifier("date" + i, "id", getPackageName());

            DateNumber[i] = findViewById(numberId);
            DateName[i] = findViewById(nameId);
            DateLable[i] = findViewById(labelId);

            DateNumber[i].setText(numberFormatter.format(currentDate + MILIS_IN_DAY*(i-START_DAY)));
            DateName[i].setText(nameFormatter.format(currentDate+ MILIS_IN_DAY*(i-START_DAY)));

            DateLable[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = getResources().getResourceEntryName(view.getId());
                    int day = Integer.parseInt(id.substring(id.length()-1));
                    ItemsList.setCurrentItem(day);
                }
            });
        }
    }

    private void setUpPager(){
        mAdapter =  new PagerAdapterMain(getSupportFragmentManager());
        ItemsList.setAdapter(mAdapter);
        ItemsList.setCurrentItem(START_DAY);
        DateNumber[START_DAY].setTextColor(getResources().getColor(R.color.colorAccent));
        DateLable[START_DAY].setBackground(ContextCompat.getDrawable(this, R.drawable.date_selected));
        final Context context = this;

        ItemsList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                for(int i=0; i<DAYS_IN_WEEK; i++){
                    DateNumber[i].setTextColor(Color.parseColor("#ffffff"));
                    DateLable[i].setBackground(null);
                }
                DateLable[position].setBackground(ContextCompat.getDrawable(context, R.drawable.date_selected));
                DateNumber[position].setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });
    }

    public void openScan(View view) {
        Intent openScan = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
        startActivity(openScan);
    }

    public void goToNextWeek(View view) {
        currentDate += MILIS_IN_DAY*DAYS_IN_WEEK;
        setUpDateBar();
        //reload adater
    }

    public void goToPreviousWeek(View view) {
        currentDate -= MILIS_IN_DAY*DAYS_IN_WEEK;
        setUpDateBar();
        //reload adater
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(1==1){
            menu.findItem(R.id.action_expire).setIcon(R.drawable.error_active_24);
        }else{
            menu.findItem(R.id.action_expire).setIcon(R.drawable.baseline_error_outline_white_24);
        }
        return true;
    }

    //if menu option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_time) {
            currentDate = System.currentTimeMillis();
            ItemsList.setCurrentItem(START_DAY);
            setUpDateBar();
            return true;
        }else if (id == R.id.action_expire) {
            Intent openExpired = new Intent(MainActivity.this, ExpiredActivity.class);
            startActivity(openExpired);
            return true;
        }else if (id == R.id.action_assortment) {
            Intent openAssortment = new Intent(MainActivity.this, AssortmentActivity.class);
            startActivity(openAssortment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
