package com.constructefile.democonstract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.adapter.FormOneAdapter;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.content.FormData;
import com.constructefile.democonstract.activity.CategoryActivity;
import com.constructefile.democonstract.activity.LoginActivity;
import com.constructefile.democonstract.content.SpinnerData;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String previous_remarks, checklist_id;
    String server_user_id, date_started, email, l_name, f_name;
    private RecyclerView recyclerView;
    private ArrayList<FormData> formDatas;
    private FormOneAdapter formOneAdapter;
    private SQLiteHandler db;
    private SessionManager session;

    Button btn_send;
    List<String> list = new ArrayList<>();
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();
    private static short backStatus = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));
        // Progress dialog
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setCancelable(false);

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

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                previous_remarks = null;
                checklist_id = null;
            } else {
                previous_remarks = extras.getString("previous_remarks");
                checklist_id = extras.getString("response_id");
            }
        } else {
            previous_remarks = (String) savedInstanceState.getSerializable("previous_remarks");
            checklist_id = (String) savedInstanceState.getSerializable("response_id");
        }
        Log.i("clicked", previous_remarks + " " + checklist_id);
        recyclerView = (RecyclerView) findViewById(R.id.recycleListView);
        btn_send = (Button) findViewById(R.id.btn_send);
        formOneAdapter = new FormOneAdapter(getFormData(), getApplicationContext());
        recyclerView.setAdapter(formOneAdapter);
        formOneAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDATA();
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();
        db.deleteSupervisor();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    private ArrayList<FormData> getFormData() {
        formDatas = new ArrayList<>();
        formDatas.clear();
        formDatas.add(new FormData("Engine Oil", 1));
        formDatas.add(new FormData("All hoses", 2));
        formDatas.add(new FormData("All belts", 3));
        formDatas.add(new FormData("Overall Engine", 4));
        formDatas.add(new FormData("Hydraulic Fluid", 6));
        formDatas.add(new FormData("Swing Gear Oil", 7));
        formDatas.add(new FormData("Engine Coolant", 8));
        formDatas.add(new FormData("Radiator", 9));
        formDatas.add(new FormData("Air filter", 10));
        formDatas.add(new FormData("Batteries", 11));
        formDatas.add(new FormData("Fire Extinguisher", 12));
        formDatas.add(new FormData("Lights", 13));
        formDatas.add(new FormData("Mirrors", 14));
        formDatas.add(new FormData("Windshield", 15));
        formDatas.add(new FormData("Wipers/windshield fluid", 16));
        formDatas.add(new FormData("Tracks/under carriage", 17));
        formDatas.add(new FormData("Steps and handle bars", 18));
        formDatas.add(new FormData("Boom/Stick/Cylinders", 19));
        formDatas.add(new FormData("Bucket", 20));
        formDatas.add(new FormData("Over all Machine", 21));
        formDatas.add(new FormData("Indicators and gauges", 22));
        formDatas.add(new FormData("Horn, Back up alarm", 23));
        formDatas.add(new FormData("Seatbelt, seat and mounting", 24));
        formDatas.add(new FormData("Operator Manual", 25));
        formDatas.add(new FormData("Safety Warning", 26));
        formDatas.add(new FormData("Over all Machine", 27));
        return formDatas;
    }

    public void postDATA() {

        for (int i = 0; i < formDatas.size(); i++) {
            save(i);
        }

    }

    public void save(final int i) {

        pDialog.setMessage("Sending Data...");
        showDialog();
        String tag_string_req = "req_operational_check";
        //insertion
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_EQUIPMENT_CHECKLIST_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.i("operational_check", "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String id = jObj.getString("id");
                    if (id != null) {
                        Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        Log.i("checklist_check", "Inserted: " + id);
                    } else {
                        Log.i("checklist_check", " Not Inserted: Unexpected Error! Try again later.");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Log.i("operational_check", e.toString());
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.i("checkout_insert", "Insertion Error: " + error.getMessage());
                /*Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("equipment_checklist_id", checklist_id);
                params.put("checklist_id", String.valueOf(formDatas.get(i).getId()));
                params.put("status", String.valueOf(formDatas.get(i).getStatus()));
                params.put("comment", formDatas.get(i).getComment());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
