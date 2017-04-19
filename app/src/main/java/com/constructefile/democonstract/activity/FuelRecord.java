package com.constructefile.democonstract.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.content.SpinnerData;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelRecord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_submit;
    TextView dateSW;
    EditText jobText, fuelTank, fuelAmount;
    private int pressBTN;
    private String pressCAT;
    String TRUCK_ID_SELECTED = "", EQUIPMENT_ID_SELECTED = "", server_user_id, date_started, email, l_name, f_name, jobValue,
            fuelTankValue, fuelAmountValue, engineOilValue, hydraulicFluidValue,
            engineCoolantValue, transmissionOilValue;

    private SQLiteHandler db;
    SQLiteHandler dbOne;
    private SessionManager session;

    // Spinner element
    Spinner spinnerSerial, equipmentSerial, engineOilSerial, hydraulicFluidSerial, transmissionOilSerial, engineCoolantSerial;
    List<String> vehicle_json_id;
    List<String> list;
    List<String> ids, equipmentIds;
    List<String> lables, equipments;
    ArrayList<SpinnerData> spinnerDatas;
    private ProgressDialog pDialog;
    Calendar c;
    String date;
    int hour, minute, second;
    String date_time, time, hr, min, sec;
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_record2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
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

        Log.i("clicked", pressBTN + "");
        //db.deleteLabels();
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
        jobText = (EditText) findViewById(R.id.jobText);
        fuelTank = (EditText) findViewById(R.id.fuelTank);
        fuelAmount = (EditText) findViewById(R.id.fuelAmount);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        // Spinner element
        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        equipmentSerial = (Spinner) findViewById(R.id.equipmentSerial);
        engineOilSerial = (Spinner) findViewById(R.id.engineOilSerial);
        engineCoolantSerial = (Spinner) findViewById(R.id.engineCoolantSerial);
        hydraulicFluidSerial = (Spinner) findViewById(R.id.hydraulicFluidSerial);
        transmissionOilSerial = (Spinner) findViewById(R.id.transmissionOilSerial);

        List<String> typeOfEnginOil = new ArrayList<String>();
        typeOfEnginOil.add("High");
        typeOfEnginOil.add("Half");
        typeOfEnginOil.add("Low");

        List<String> typeOfHydraulicFluid = new ArrayList<String>();
        typeOfHydraulicFluid.add("High");
        typeOfHydraulicFluid.add("Half");
        typeOfHydraulicFluid.add("Low");

        List<String> typeOfTransmissionOil = new ArrayList<String>();
        typeOfTransmissionOil.add("High");
        typeOfTransmissionOil.add("Half");
        typeOfTransmissionOil.add("Low");

        List<String> typeOfEngineCoolant = new ArrayList<String>();
        typeOfEngineCoolant.add("High");
        typeOfEngineCoolant.add("Half");
        typeOfEngineCoolant.add("Low");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterforEngineOil = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfEnginOil);
        dataAdapterforEngineOil.setDropDownViewResource(R.layout.spinner_drop_down);
        engineOilSerial.setAdapter(dataAdapterforEngineOil);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterforHydraulicFluid = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfHydraulicFluid);
        dataAdapterforHydraulicFluid.setDropDownViewResource(R.layout.spinner_drop_down);
        hydraulicFluidSerial.setAdapter(dataAdapterforHydraulicFluid);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterforTransmissionOil = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfTransmissionOil);
        dataAdapterforTransmissionOil.setDropDownViewResource(R.layout.spinner_drop_down);
        transmissionOilSerial.setAdapter(dataAdapterforTransmissionOil);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterforEngineCoolant = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfEngineCoolant);
        dataAdapterforEngineCoolant.setDropDownViewResource(R.layout.spinner_drop_down);
        engineCoolantSerial.setAdapter(dataAdapterforEngineCoolant);

        spinnerDatas = new ArrayList<SpinnerData>();
        //getListFromWebAndInsertIntoDB();
        ids = db.getAllLabelIds(String.valueOf(pressBTN));
        equipmentIds = db.getAllEquipmentIds();
        // Loading spinner data from database
        loadSpinnerData();


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        f_name = user.get("f_name");
        l_name = user.get("l_name");
        server_user_id = user.get("server_user_id");
        email = user.get("email");
        date_started = user.get("date_started");

        getDateTime();

        spinnerSerial.setOnItemSelectedListener(this);
        equipmentSerial.setOnItemSelectedListener(this);
        engineCoolantSerial.setOnItemSelectedListener(this);
        engineOilSerial.setOnItemSelectedListener(this);
        hydraulicFluidSerial.setOnItemSelectedListener(this);
        transmissionOilSerial.setOnItemSelectedListener(this);
        Log.i("Serial", lables.toString());
        Log.i("Serial", ids.toString());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
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
        lables = db.getAllLabels(Integer.toString(pressBTN));
        Log.i("labels", "load spinner data " + lables.toString());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerSerial.setAdapter(dataAdapter);


        // Spinner Drop down elements
        equipments = db.getAllEquipments();
        Log.i("equipments", "load spinner data " + equipments.toString());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterEq = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, equipments);
        // Drop down layout style - list view with radio button
        dataAdapterEq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        equipmentSerial.setAdapter(dataAdapterEq);
    }


    public void getDateTime() {
        // Posting parameters to insert_check_message url
        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;
        date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
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
        time = hr + ":" + min + ":" + sec;
        date_time = date + " " + time;

        dateSW.setText(date_time);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {

            case R.id.engineCoolantSerial:
                // On selecting a spinner item
                engineCoolantValue = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.engineOilSerial:
                // On selecting a spinner item
                engineOilValue = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.hydraulicFluidSerial:
                // On selecting a spinner item
                hydraulicFluidValue = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.transmissionOilSerial:
                // On selecting a spinner item
                transmissionOilValue = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spinnerSerial:
                TRUCK_ID_SELECTED = ids.get(position);
                break;
            case R.id.equipmentSerial:
                EQUIPMENT_ID_SELECTED = equipmentIds.get(position);
                break;

            default:
                engineCoolantValue = adapterView.getItemAtPosition(position).toString();
                engineOilValue = adapterView.getItemAtPosition(position).toString();
                TRUCK_ID_SELECTED = adapterView.getItemAtPosition(position).toString();
                EQUIPMENT_ID_SELECTED = adapterView.getItemAtPosition(position).toString();
                hydraulicFluidValue = adapterView.getItemAtPosition(position).toString();
                transmissionOilValue = adapterView.getItemAtPosition(position).toString();
                break;

        }
        //Toast.makeText(getApplicationContext(), sOne + sTwo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void sendData() {
        fuelAmountValue = fuelAmount.getText().toString();
        fuelTankValue = fuelTank.getText().toString();
        jobValue = jobText.getText().toString();

        Log.i("fuel_record", EQUIPMENT_ID_SELECTED + " "+ TRUCK_ID_SELECTED + " " + hydraulicFluidValue + " " +
                transmissionOilValue + " "+ engineOilValue + " " + engineCoolantValue);
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
                        Toast.makeText(FuelRecord.this, "Fuel Record Sent To Server", Toast.LENGTH_LONG).show();
                        jobText.setText("");
                        fuelTank.setText("");
                        fuelAmount.setText("");
                    } else {
                        Log.i("insert_shift", "shift was not inserted");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.i("insert_shift", "insert_shift Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fuel_tank_no", fuelTankValue);
                params.put("fuel_amount", fuelAmountValue);
                params.put("engine_oil", engineOilValue);
                params.put("hydralic_fluid", hydraulicFluidValue);
                params.put("egine_coolant", engineCoolantValue);
                params.put("transmission_oil", transmissionOilValue);
                params.put("date", date_time);
                params.put("operator_id", server_user_id);
                params.put("job", jobValue);
                params.put("equipment_id", EQUIPMENT_ID_SELECTED);
                params.put("truck_id", TRUCK_ID_SELECTED);

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FuelRecord.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();

        // Launching the login activity
        Intent intent = new Intent(FuelRecord.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
