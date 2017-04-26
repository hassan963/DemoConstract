package com.constructefile.democonstract.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.MainActivity;
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

public class EquipmentChechklist extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String TRUCK_ID_SELECTED = "", EQUIPMENT_ID_SELECTED = "";
    private SQLiteHandler db;
    private SessionManager session;
    private int pressBTN;
    List<String> list = new ArrayList<>();
    List<String> ids, equipmentIds;
    List<String> lables, equipments;
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();
    Spinner spinnerSerial, equipmentSerial;
    String server_user_id, date_started, email, l_name, f_name, jobValue, remarksValue;
    Calendar c;
    String date;
    int hour, minute, second;
    String date_time, time, hr, min, sec;
    int month;
    private ProgressDialog pDialog;
    Button btn_submit;
    TextView dateSW;
    EditText jobText, remarksText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_chechklist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        //Getting session
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        f_name = user.get("f_name");
        l_name = user.get("l_name");
        server_user_id = user.get("server_user_id");
        email = user.get("email");
        date_started = user.get("date_started");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                pressBTN = 0;
            } else {
                pressBTN = extras.getInt("CATEGORY");
            }
        } else {
            pressBTN = (Integer) savedInstanceState.getSerializable("CATEGORY");
        }
        Log.i("clicked", pressBTN + "");

        ids = db.getAllLabelIds(String.valueOf(pressBTN));

        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        equipmentSerial = (Spinner) findViewById(R.id.equipmentSerial);
        dateSW = (TextView) findViewById(R.id.dateSW);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        jobText = (EditText) findViewById(R.id.jobText);
        remarksText = (EditText) findViewById(R.id.remarksText);


        ids = db.getAllLabelIds(String.valueOf(pressBTN));
        equipmentIds = db.getAllEquipmentIds();
        // Loading spinner data from database
        loadSpinnerData();
        getDateTime();

        spinnerSerial.setOnItemSelectedListener(this);
        equipmentSerial.setOnItemSelectedListener(this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Toast.makeText(EquipmentChechklist.this, jobText.getText().toString() + "     " + remarksText.getText().toString(), Toast.LENGTH_SHORT).show();
                Log.i("value", jobText.getText().toString() + "     " + remarksText.getText().toString());*/
                sendDataAndContinue(jobText.getText().toString(), remarksText.getText().toString());
            }
        });
    }

    public void sendDataAndContinue(final String job, final String remarks) {
        /*remarksValue = remarksText.getText().toString();
        jobValue = jobText.getText().toString();*/

        pDialog.setMessage("Sending ...");
        showDialog();
        //insertion
        String tag_string_req = "req_insert_equipment_checklist";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_EQUIPMENT_CHECKLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("insert_equipment", "Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);

                    // Now store the user in SQLite
                    String response_id = jObj.getString("id");
                    String previous_remarks = jObj.getString("remakrs");

                    if (response_id != null) {
                        Toast.makeText(EquipmentChechklist.this, "One More Step", Toast.LENGTH_SHORT).show();

                        if (pressBTN == 2 || pressBTN == 9 || pressBTN == 10) {
                            Intent intent = new Intent(EquipmentChechklist.this, MainActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 1) {
                            Intent intent = new Intent(EquipmentChechklist.this, DozerActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 3) {
                            Intent intent = new Intent(EquipmentChechklist.this, TruckActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 4) {
                            Intent intent = new Intent(EquipmentChechklist.this, BackHoeActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 5) {
                            Intent intent = new Intent(EquipmentChechklist.this, LoaderActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 6) {
                            Intent intent = new Intent(EquipmentChechklist.this, CompanyTruckActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 7) {
                            Intent intent = new Intent(EquipmentChechklist.this, RoadRollerActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                        if (pressBTN == 8) {
                            Intent intent = new Intent(EquipmentChechklist.this, SkidSteerActivity.class);
                            intent.putExtra("response_id", response_id);
                            intent.putExtra("previous_remarks", previous_remarks);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.i("equipment_check", "equipment check was not inserted");
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
                params.put("date", date_time);
                params.put("equipment_id", EQUIPMENT_ID_SELECTED);
                params.put("operator_id", server_user_id);
                params.put("job", job);
                params.put("equipment_hours", "");
                params.put("remarks", remarks);
                params.put("mechanic_alert", "");
                params.put("details", "");
                params.put("truck_id", TRUCK_ID_SELECTED);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Function to load the spinner data from SQLite database
     */
    private void loadSpinnerData() {// database handler
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinnerSerial:
                TRUCK_ID_SELECTED = ids.get(position);
                break;
            case R.id.equipmentSerial:
                EQUIPMENT_ID_SELECTED = equipmentIds.get(position);
                break;

            default:
                TRUCK_ID_SELECTED = parent.getItemAtPosition(position).toString();
                EQUIPMENT_ID_SELECTED = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();
        db.deleteSupervisor();
        // Launching the login activity
        Intent intent = new Intent(EquipmentChechklist.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EquipmentChechklist.this, CategoryActivity.class);
        startActivity(intent);
        finish();
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
