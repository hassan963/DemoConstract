package com.constructefile.democonstract.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnsafeWork extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerUnsafe;
    private EditText chatET;
    private Button chatSendBTN;
    private String superVisor;
    private List<String> superVisorList;
    private SQLiteHandler db;
    private SessionManager session;
    String server_user_id, date_started, email, l_name, f_name, supervisor_selected = "";

    List<String> ids;
    List<String> lables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_unsafe_work_notify);
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

        spinnerUnsafe = (Spinner) findViewById(R.id.spinnerUnsafe);
        chatET = (EditText) findViewById(R.id.chatET);
        chatSendBTN = (Button) findViewById(R.id.chatSendBTN);
        superVisorList = new ArrayList<>();

        ids = db.getAllSupervisorIds();
        loadSpinnerData();
        spinnerUnsafe.setOnItemSelectedListener(this);
        chatSendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(chatET.getText().toString());
            }
        });
    }

    private void loadSpinnerData() {// database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Spinner Drop down elements
        lables = db.getAllSupervisor();
        Log.i("labels", "load spinner data " + lables.toString());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerUnsafe.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerUnsafe:
                supervisor_selected = ids.get(position);
                break;
            default:
                supervisor_selected = adapterView.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    // sending response to server NEAR MISS API
    public void saveData(final String text) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_UNSAFE_WORK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("operator_id", server_user_id);
                params.put("supervisor_id", supervisor_selected);
                params.put("message", text);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();
        db.deleteSupervisor();
        // Launching the login activity
        Intent intent = new Intent(UnsafeWork.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
