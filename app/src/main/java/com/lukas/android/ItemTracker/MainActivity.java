package com.lukas.android.ItemTracker;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    TextView dayDisplay;
    ImageView previousDay;
    ImageView nextDay;
    FloatingActionButton scanFab;
    ViewPager itemsList;

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


    /*************************************************************************************************
     * Handle Swipe events on the list view
     **************************************************************************************************/

    /*        itemsList.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this, itemsList));

    public class OnSwipeTouchListener implements View.OnTouchListener {

       ListView list;
       private GestureDetector gestureDetector;
       private Context context;

       public OnSwipeTouchListener(Context ctx, ListView list) {
           gestureDetector = new GestureDetector(ctx, new GestureListener());
           context = ctx;
           this.list = list;
       }

       public OnSwipeTouchListener() {
           super();
       }

       @Override
       public boolean onTouch(View v, MotionEvent event) {
           return gestureDetector.onTouchEvent(event);
       }

       public void onSwipeRight(int pos) {
           goToPreviousDay(null);
       }

       public void onSwipeLeft(int pos) {
           goToNextDay(null);
       }
       private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

           private static final int SWIPE_THRESHOLD = 100;
           private static final int SWIPE_VELOCITY_THRESHOLD = 100;

           @Override
           public boolean onDown(MotionEvent e) {
               return true;
           }

           private int getPostion(MotionEvent e1) {
               return list.pointToPosition((int) e1.getX(), (int) e1.getY());
           }

           @Override
           public boolean onFling(MotionEvent e1, MotionEvent e2,
                                  float velocityX, float velocityY) {
               float distanceX = e2.getX() - e1.getX();
               float distanceY = e2.getY() - e1.getY();
               if (Math.abs(distanceX) > Math.abs(distanceY)
                       && Math.abs(distanceX) > SWIPE_THRESHOLD
                       && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                   if (distanceX > 0)
                       onSwipeRight(getPostion(e1));
                   else
                       onSwipeLeft(getPostion(e1));
                   return true;
               }
               return false;
           }

       }
   }
}*/
