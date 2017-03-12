package com.constructefile.democonstract.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.constructefile.democonstract.R;

import java.util.Calendar;

public class TimeSheet extends AppCompatActivity {

    private TimePicker timePicker1, timePicker2;
    private int hour, min;
    private Calendar calendar;
    TextView total_time;
    int hourOne, minOne, hourTwo, minTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        /*timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker1.getHour();
            min = timePicker1.getMinute ();
        }else {
            hour = timePicker1.getCurrentHour();
            min = timePicker1.getCurrentMinute ();
        }*/

        //  initiate the view's
        total_time = (TextView) findViewById(R.id.total_time);
        timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(false); // used to display AM/PM mode
        timePicker2.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();
                total_time.setText("Total Time :: " + hourOfDay + " : " + minute); // set the current time in text view
                hourOne = hourOfDay; minOne = minute;
            }
        });

        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();
                total_time.setText("Total Time :: " + hourOfDay + " : " + minute); // set the current time in text view
                hourTwo = hourOfDay; minTwo = minute;
            }
        });

    }

    public int getTime(int hourOfDay, int minute){

        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
