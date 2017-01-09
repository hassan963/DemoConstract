package com.hassanmashraful.democonstract.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.R;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FuelRecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView dateSW;
    TextView truckSW;
    TextView operatorTextView;
    EditText deptSW;
    CheckBox fuel_checkbox;
    CheckBox radiator_checkbox;
    CheckBox engineOil_checkbox;
    CheckBox hour_meterbox;
    CheckBox hydraulic_checkbox;


    private int pressBTN;
    private String pressCAT;
    String f_name;
    String l_name;
    String user_id;
    String email;
    String login_at_date;
    String login_at_time;
    String shift_id;
    String TRUCK_ID_SELECTED="";


    private SQLiteHandler db;
    SQLiteHandler dbOne;
    private SessionManager session;

    // Spinner element
    Spinner spinnerSerial;
    List<String> vehicle_json_id;
    List<String> list;
    List<String> ids;
    List<String> lables;
    ArrayList<SpinnerData> spinnerDatas;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Getting session
        db = new SQLiteHandler(getApplicationContext());
        dbOne = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        vehicle_json_id = new ArrayList<>();
        list = new ArrayList<>();

        dateSW = (TextView) findViewById(R.id.dateSW);
        truckSW = (TextView) findViewById(R.id.truckSW);
        operatorTextView = (TextView) findViewById(R.id.operatorTextView);
        deptSW = (EditText) findViewById(R.id.deptSW);
        fuel_checkbox = (CheckBox) findViewById(R.id.fuel_check);
        radiator_checkbox = (CheckBox) findViewById(R.id.radiator_check);
        engineOil_checkbox = (CheckBox) findViewById(R.id.engineOil_check);
        hour_meterbox = (CheckBox) findViewById(R.id.hour_meter);
        hydraulic_checkbox = (CheckBox) findViewById(R.id.hydraulic_check);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Spinner element
        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        spinnerDatas = new ArrayList<SpinnerData>();


        //get the pressed button from context
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                pressBTN = 0;
                pressCAT = "";
            } else {
                pressBTN = extras.getInt("CATEGORY");
                pressCAT = extras.getString("CATEGORY_NAME");
            }
        } else {
            pressBTN = (Integer) savedInstanceState.getSerializable("CATEGORY");
            pressCAT = (String) savedInstanceState.getSerializable("CATEGORY_NAME");
        }

        /*Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        Log.i("timestamp", ts);*/
        /*UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        Log.i("timestamp", "UUID One: " + idOne);
        Log.i("timestamp", "UUID Two: " + idTwo);*/


        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        f_name = user.get("f_name");
        l_name = user.get("l_name");
        user_id = user.get("id");
        email = user.get("email");
        login_at_date = user.get("login_at_date");
        login_at_time = user.get("login_at_time");
        shift_id = user.get("shift_id");

        Log.i("clicked", pressBTN + "");
        //db.deleteLabels();

        //getListFromWebAndInsertIntoDB();
        ids = db.getAllLabelIds(String.valueOf(pressBTN));

        // Posting parameters to insert_check_message url
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
        String hour = c.get(Calendar.HOUR) + "";
        if (hour.equals("0")) {
            hour = "00";
        }
        String time = hour + ":" + c.get(Calendar.MINUTE);
        String date_time = date + " " + time;
        //int ampm= c.get(Calendar.AM_PM);
        int am_pm = c.get(Calendar.AM_PM);
        //String ampm;
        if (am_pm == 1) {
            date_time = date_time + " PM";
        } else {
            date_time = date_time + " AM";
        }

        dateSW.setText(date_time);
        truckSW.setText(pressCAT);
        operatorTextView.setText(f_name);
        // Spinner click listener
        spinnerSerial.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_full_sad));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(FuelRecordActivity.this, "formSubmit clicked", Toast.LENGTH_SHORT).show();
                Log.i("formSubmit", "clicked");
                submitForm();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("Serial", lables.toString());
        Log.i("Serial", ids.toString());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FuelRecordActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    /*public void getListFromWebAndInsertIntoDB() {
        Toast.makeText(getApplicationContext(), AppConfig.URL_TRUCK + pressBTN, Toast.LENGTH_SHORT).show();
        String tag_string_req = "req_category_base_serial_model";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_TRUCK + pressBTN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("serial_model", "Response: " + response.toString());
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String vehicle_id = jsonobject.getString("id");
                        String model = jsonobject.getString("model");
                        String serial_no = jsonobject.getString("serial_no");
                        Log.i("serial_model", String.valueOf(pressBTN) + " - " + vehicle_id + " - " + serial_no);
                        Toast.makeText(FuelRecordActivity.this, "1st One " + String.valueOf(pressBTN) + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();


                        db.insertLabel(Integer.toString(pressBTN), vehicle_id, serial_no);
                        Log.i("labelInsert", String.valueOf(pressBTN) + " " + vehicle_id + " " + serial_no);
                        Toast.makeText(FuelRecordActivity.this, "2nd One " + String.valueOf(pressBTN) + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();

                        *//*SpinnerData spin = new SpinnerData(vehicle_id, model, serial_no);
                        spinnerDatas.add(spin);
                        *//**//**//**//*SpinnerData spinnerData = new SpinnerData(vehicle_id, model, serial_no);
                        spinnerDatas.add(spinnerData);
                        list.add(serial_no);
                        vehicle_json_id.add(vehicle_id);*//**//**//**//*
                        Log.i("model_serial", vehicle_id + " " + serial_no + " " + model);*//*
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }*/

    /**
     * Function to load the spinner data from SQLite database
     */
    private void loadSpinnerData() {
        // database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Spinner Drop down elements
        lables = db.getAllLabels(Integer.toString(pressBTN));
        Log.i("labels", "load spinner data " + lables.toString());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSerial.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

         /*
        * get selected vehicle serial
        * */
        TRUCK_ID_SELECTED = ids.get(position);
        // Showing selected spinner item


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void submitForm() {

        //id - generate a unique_id for whole form
        //date//
        //truck id from truck serial - selectedSerial//
        //department input//
        //shift//
        //operator//
        //hour meter//
        //fuel type//
        //status 0/1 means checked or unchecked


        /*
        * get hour meter
        * */
        String hour_meter;
        if (hour_meterbox.isChecked()) {
            hour_meter = "t";
        } else {
            hour_meter = "f";
        }


        /**
         * generate unique id for each form
         * **/

        Long tsLong = System.currentTimeMillis() / 1000;
        String idMill = tsLong.toString();
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();

        String id = idMill + output;
        Log.i("timestamp", "ID : " + id);

        /*
        * get date
        * */
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
        String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        //int ampm= c.get(Calendar.AM_PM);
        String timestamp = date + " " + time;
        Log.i("time", date + " " + time);




        /*
        * get department name from editText
        * */
        String deptName = deptSW.getText().toString();



        /*
        * get hour meter
        * */
        if (fuel_checkbox.isChecked()) {
            String fuel_check = "t";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "1", deptName, hour_meter, timestamp, fuel_check);
        } else {
            String fuel_check = "f";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "1", deptName, hour_meter, timestamp, fuel_check);
        }

        /*
        * get hour meter
        * */
        if (engineOil_checkbox.isChecked()) {
            String engineOil_check = "t";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "2", deptName, hour_meter, timestamp, engineOil_check);
        } else {
            String engineOil_check = "f";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "2", deptName, hour_meter, timestamp, engineOil_check);
        }
        /*
        * get hour meter
        * */
        if (radiator_checkbox.isChecked()) {
            String radiator_check = "t";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "3", deptName, hour_meter, timestamp, radiator_check);
        } else {
            String radiator_check = "f";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "3", deptName, hour_meter, timestamp, radiator_check);
        }
        /*
        * get hour meter
        * */
        if (hydraulic_checkbox.isChecked()) {
            String hydraulic_check = "t";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "4", deptName, hour_meter, timestamp, hydraulic_check);
        } else {
            String hydraulic_check = "f";
            insertFuelRecord(id, TRUCK_ID_SELECTED, "4", deptName, hour_meter, timestamp, hydraulic_check);
        }

        Intent intent = new Intent(FuelRecordActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    public void insertFuelRecord(final String forum_id, final String truck_id, final String fuel_type_id, final String department, final String hour_meter, final String time_stamp, final String status) {
        Log.i("formSubmit", forum_id + truck_id + user_id + fuel_type_id + department + shift_id + hour_meter + time_stamp + status);
        //Toast.makeText(FuelRecordActivity.this, "id " + forum_id + " truck:" + truck_id + " op:" + user_id + " fuel:" + fuel_type_id + " dept:" + department + " shift:" + shift_id + " hr:" + hour_meter + " ts:" + time_stamp + " ok:" + status, Toast.LENGTH_SHORT).show();


        pDialog.setMessage("Sending ...");
        showDialog();
        //insertion
        String tag_string_req = "req_insert_fuel";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_FUEL_RECORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("insert_fuel_record", "Response: " + response.toString());


                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);

                    // Now store the user in SQLite
                    String response_id = jObj.getString("id");

                    if (response_id != null) {

                        // Fuel record Inserted successfully
                        Toast.makeText(FuelRecordActivity.this, "Fuel Record Sent To Server", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("insert_shift", "shift was not inserted");
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
                Log.i("insert_shift", "insert_shift Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", forum_id);
                params.put("truck_id", truck_id);
                params.put("operator_id", user_id);
                params.put("fuel_type_id", fuel_type_id);
                params.put("department", department);
                params.put("shift_id", shift_id);
                params.put("hour_meter", hour_meter);
                params.put("timestamp", time_stamp);
                params.put("status", status);

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


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();

        // Launching the login activity
        Intent intent = new Intent(FuelRecordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
