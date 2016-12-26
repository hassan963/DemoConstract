package com.hassanmashraful.democonstract.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.hassanmashraful.democonstract.Task.BackgroundTask;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FuelRecordActivity extends AppCompatActivity {
    TextView dateSW;
    TextView truckSW;
    TextView operatorTextView;
    EditText deptSW;

    private int pressBTN;
    private String pressCAT;
    String f_name;
    String l_name;
    String user_id;
    String email;
    String login_at_date;
    String login_at_time;
    String shift_id;
    String deptName;
    ArrayAdapter<String> dataSerialAdapter;
    ArrayAdapter<String> dataModelAdapter;

    private SQLiteHandler db;
    private SessionManager session;

    // Spinner element
    Spinner spinnerSerial;
    List<String> vehicle_json_id;
    List<String> list;
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vehicle_json_id = new ArrayList<>();
        list = new ArrayList<>();

        dateSW = (TextView) findViewById(R.id.dateSW);
        truckSW = (TextView) findViewById(R.id.truckSW);
        operatorTextView = (TextView) findViewById(R.id.operatorTextView);
        deptSW = (EditText) findViewById(R.id.deptSW);
        // Spinner element
        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);


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

        //Getting session
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

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
        getList();
        /*BackgroundTask backgroundTask = new BackgroundTask(FuelRecordActivity.this, pressBTN);
        list = backgroundTask.getList();*/


        loadSpinnerSerialData();
        // Spinner click listener
        spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnerModel.setSelection(list.get(position));
                /*Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                //spinnerSerial.setSelection();
                Log.i("selected truck", parent.getItemAtPosition(spinnerSerial.getSelectedItemPosition()).toString());*/
                Toast.makeText(getApplicationContext(), "Cliked!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// Posting parameters to insert_check_message url
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);

        dateSW.setText(date);
        truckSW.setText(pressCAT);
        operatorTextView.setText(f_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Function to load the spinnerModel data from SQLite database
     */
    private void loadSpinnerSerialData() {
        String selectedValue = "";

        // Creating adapter for spinnerModel
        dataSerialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);

        // Drop down layout style - list view with radio button
        dataSerialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerSerial.setAdapter(dataSerialAdapter);
        dataSerialAdapter.notifyDataSetChanged();
    }


    public void show() {
        for (int i = 0; i < spinnerDatas.size(); i++) {
            Log.v("DATA ", spinnerDatas.get(i).getSerial());
            Toast.makeText(getApplicationContext(), spinnerDatas.get(0).getSerial(), Toast.LENGTH_SHORT).show();
        }
    }


    public void getList() {
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
                        SpinnerData spinnerData = new SpinnerData(vehicle_id, model, serial_no);
                        spinnerDatas.add(spinnerData);
                        list.add(serial_no);
                        vehicle_json_id.add(vehicle_id);
                        Log.i("model_serial", vehicle_id + " " + serial_no + " " + model);
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

    }
}
