package com.constructefile.democonstract.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.Content.SpinnerData;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSheet extends AppCompatActivity {

    private TimePicker timePicker1, timePicker2;
    TextView total_time;
    private SQLiteHandler db;
    private SessionManager session;
    Spinner equipmentSerial;
    Spinner spinner_shift;
    List<String> ids;
    List<String> equipments;
    ArrayList<SpinnerData> spinnerDatas;
    private ProgressDialog pDialog;
    String equipment_id_selected = "";
    String shift = "";
    String operator_id, jobTextValue, descriptionTextValue, remarksTextValue, hourStart, hourFinish, hourTotal;
    String hourStartHour, hourStartMinute, hourFinishHour, hourFinishMinute;
    Button btn_submit, btn_show_total;
    EditText job, description_of_work, remarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        operator_id = user.get("server_user_id");
        job = (EditText) findViewById(R.id.job);
        remarks = (EditText) findViewById(R.id.remarks);
        description_of_work = (EditText) findViewById(R.id.description_of_work);
        btn_show_total = (Button) findViewById(R.id.btn_show_total);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTimeSheet();
            }
        });

        //getListFromWebAndInsertIntoDB();
        ids = db.getAllEquipmentIds();
        // Spinner element
        spinner_shift = (Spinner) findViewById(R.id.spinner_shift);
        equipmentSerial = (Spinner) findViewById(R.id.equipmentSerial);
        spinnerDatas = new ArrayList<SpinnerData>();

        // Loading spinner data from database
        loadSpinnerData();


        // Spinner Drop down elements for shift
        List<String> shifts = new ArrayList<String>();
        shifts.add("Day");
        shifts.add("Night");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shifts);

        // Drop down layout style - list view with radio button
        dataAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_shift.setAdapter(dataAdapters);

        //  initiate the view's
        total_time = (TextView) findViewById(R.id.total_time);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker2 = (TimePicker) findViewById(R.id.timePicker2);
        timePicker1.setIs24HourView(true); // used to display AM/PM mode
        timePicker2.setIs24HourView(true); // used to display AM/PM mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hourStartHour = timePicker1.getHour() + "";
            hourStartMinute = timePicker1.getMinute() + "";
            hourFinishHour = timePicker2.getHour() + "";
            hourFinishMinute = timePicker2.getMinute() + "";
            hourStart = hourStartHour + ":" + hourStartMinute + ":00";
            hourFinish = hourFinishHour + ":" + hourFinishMinute + ":00";
        }

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();

                String hr;
                String min;
                if (hourOfDay >= 0 && hourOfDay <= 9) {
                    hourStartHour = "0" + hourOfDay;
                } else {
                    hourStartHour = hourOfDay + "";
                }
                if (minute >= 0 && minute <= 9) {
                    hourStartMinute = "0" + minute;
                } else {
                    hourStartMinute = minute + "";
                }
                hourStart = hourStartHour + ":" + hourStartMinute + ":00";
            }
        });

        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
                Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();
                String hr;
                String min;
                if (hourOfDay >= 0 && hourOfDay <= 9) {
                    hourFinishHour = "0" + hourOfDay;
                } else {
                    hourFinishHour = hourOfDay + "";
                }
                if (minute >= 0 && minute <= 9) {
                    hourFinishMinute = "0" + minute;
                } else {
                    hourFinishMinute = minute + "";
                }
                hourFinish = hourFinishHour + ":" + hourFinishMinute + ":00";
            }
        });

        btn_show_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("hh:mm:ss");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = df.parse(hourStart);
                    date2 = df.parse(hourFinish);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diff = date2.getTime() - date1.getTime();

                long timeInSeconds = (diff / 1000);
                long hours, minutes, seconds;
                hours = timeInSeconds / 3600;
                timeInSeconds = timeInSeconds - (hours * 3600);
                minutes = timeInSeconds / 60;
                timeInSeconds = timeInSeconds - (minutes * 60);
                seconds = timeInSeconds;

                String hrs;
                String mins;
                String secs;
                if (hours >= 0 && hours <= 9) {
                    hrs = "0" + hours;
                } else {
                    hrs = hours + "";
                }
                if (minutes >= 0 && minutes <= 9) {
                    mins = "0" + minutes;
                } else {
                    mins = minutes + "";
                }
                if (seconds >= 0 && seconds <= 9) {
                    secs = "0" + seconds;
                } else {
                    secs = seconds + "";
                }
                hourTotal = hrs + ":" + mins + ":" + secs;
                total_time.setText(hourTotal);
            }
        });
        // Spinner click listener
        equipmentSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                equipment_id_selected = ids.get(position);
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + equipment_id_selected, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shift = parent.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + shift, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * Function to load the spinner data from SQLite database
     */
    private void loadSpinnerData() {
        // database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Spinner Drop down elements
        equipments = db.getAllEquipments();
        Log.i("equipments", "load spinner data " + equipments.toString());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, equipments);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        equipmentSerial.setAdapter(dataAdapter);
    }


    public int getTime(int hourOfDay, int minute) {

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


    public void submitTimeSheet() {
        remarksTextValue = remarks.getText().toString();
        descriptionTextValue = description_of_work.getText().toString();
        jobTextValue = job.getText().toString();

        // Inserting row in users table
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String hr;
        String min;
        String sec;


        if (hour >= 0 && hour <= 9) {
            hr = "0" + hour;
        } else {
            hr = hour + "";
        }
        if (minute >= 0 && minute <= 9) {
            min = "0" + minute;
        } else {
            min = minute + "";
        }
        if (second >= 0 && second <= 9) {
            sec = "0" + second;
        } else {
            sec = second + "";
        }

        String formSubmitDay = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
        String formSubmitTime = hr + ":" + min + ":" + sec;
        final String formSubmitDateTime = formSubmitDay + " " + formSubmitTime;

        //insertion

        pDialog.setMessage("Sending ...");
        showDialog();
        //insertion
        String tag_string_req = "req_insert_timesheet";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_TIMESHEET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("insert_time_sheet", "Response: " + response.toString());

                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);

                    // Now store the user in SQLite
                    String response_id = jObj.getString("id");

                    if (response_id != null) {

                        // Fuel record Inserted successfully
                        Toast.makeText(TimeSheet.this, "Time Sheet Sent To Server", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("insert_time_sheet", "time_sheet was not inserted");
                    }
                    finish();
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("insert_time_sheet", "time_sheet Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("operator_id", operator_id);
                params.put("date", formSubmitDateTime);
                params.put("equipment_id", equipment_id_selected);
                params.put("shift", shift);
                params.put("job", jobTextValue);
                params.put("description", descriptionTextValue);
                params.put("hours_start", hourStart);
                params.put("hours_finish", hourFinish);
                params.put("hours_total", hourTotal);
                params.put("remarks", remarksTextValue);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //end of insertion
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
