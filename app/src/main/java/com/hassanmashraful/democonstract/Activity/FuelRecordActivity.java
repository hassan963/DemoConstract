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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.MainActivity;
import com.hassanmashraful.democonstract.R;
import com.hassanmashraful.democonstract.Task.BackgroundTask;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FuelRecordActivity extends AppCompatActivity {
    TextView dateSW;

    private int pressBTN;
    String f_name;
    String l_name;
    String user_id;
    String email;
    String login_at_date;
    String login_at_time;
    String shift_id;
    ArrayAdapter<String> dataSerialAdapter;
    ArrayAdapter<String> dataModelAdapter;

    private SQLiteHandler db;
    private SessionManager session;

    // Spinner element
    Spinner spinnerSerial;
    List<String> list = new ArrayList<>();
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get the pressed button from context
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
        BackgroundTask backgroundTask = new BackgroundTask(FuelRecordActivity.this, pressBTN);
        list = backgroundTask.getList();


        // Spinner element
        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        loadSpinnerSerialData();
        // Spinner click listener
        spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnerModel.setSelection(list.get(position));
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                spinnerSerial.setSelection(spinnerSerial.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateSW = (TextView) findViewById(R.id.dateSW);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Posting parameters to insert_check_message url
        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);

        dateSW.setText(date);

    }

    /**
     * Function to load the spinnerModel data from SQLite database
     */
    private void loadSpinnerSerialData() {
        // Spinner Drop down elements
        /*list.add("Ford2018");
        list.add("Ford2017");
        list.add("FF");*/

        String selectedValue = "";

        // Creating adapter for spinnerModel
        dataSerialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);

        // Drop down layout style - list view with radio button
        dataSerialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerSerial.setAdapter(dataSerialAdapter);
        dataSerialAdapter.notifyDataSetChanged();
    }


    /*private void loadSpinnerSerialData() {
        // Spinner Drop down elements
        List<String> listT = new ArrayList<>();
        listT.add("Truck-456");
        listT.add("Truck-123456");
        listT.add("TT");

        // Creating adapter for spinnerModel
        dataSerialAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listT) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLUE);

                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataSerialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerSerial.setAdapter(dataSerialAdapter);
        dataSerialAdapter.notifyDataSetChanged();
    }*/

    public void show() {
        for (int i = 0; i < spinnerDatas.size(); i++) {
            Log.v("DATA ", spinnerDatas.get(i).getSerial());
            Toast.makeText(getApplicationContext(), spinnerDatas.get(0).getSerial(), Toast.LENGTH_SHORT).show();
        }

    }


}
