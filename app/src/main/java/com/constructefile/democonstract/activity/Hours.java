package com.constructefile.democonstract.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.adapter.GetAdapter;
import com.constructefile.democonstract.adapter.GetHourAdapter;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;
import com.constructefile.democonstract.models.Gets;
import com.constructefile.democonstract.models.GetsHour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Hours extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {


    public CoordinatorLayout coordinatorLayout;
    public Button btnConnectivityCheck;
    public boolean isConnected;
    public RecyclerView recycler_get_hours;
    public List<GetsHour> getList;
    public GetHourAdapter adapterHour;
    public SwipeRefreshLayout swipeRefreshLayout;
    private SQLiteHandler db;
    private SessionManager session;
    //public String supervisor_first_name = "", supervisor_last_name = "", supervisor_full_name = "";
    public String operator_id, operator_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        operator_id = user.get("server_user_id");
        operator_name = user.get("f_name") + " " + user.get("l_name");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutHours);
        recycler_get_hours = (RecyclerView) findViewById(R.id.recycler_get_hours);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_hours);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_get_hours.setLayoutManager(linearLayoutManager);
        recycler_get_hours.setItemAnimator(new DefaultItemAnimator());
        getData();
    }


    public void getData() {
        if (checkConnectivity()) {
            swipeRefreshLayout.setRefreshing(true);
            getAllPosts();
        } else {
            showSnack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().setConnectivityReceiver(this);
    }

    public boolean checkConnectivity() {
        return ConnectivityReceiver.isConnected();
    }

    public void showSnack() {
        Snackbar.make(coordinatorLayout, getString(R.string.no_internet_connected), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }).setActionTextColor(Color.RED)
                .show();

    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        this.isConnected = inConnected;
    }

    public void getAllPosts() {
        String tag_string_req = "req_get_all_posts";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.GET_ALL_HOURS_OF_CURRENT_MONTH + operator_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("all_posts", "Response: " + response.toString());
                getList = new ArrayList<>();
                try {
                    JSONObject jObject = new JSONObject(response);
                    Iterator<String> keys = jObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        Log.v("**********", "**********");
                        Log.v("object_key", key);
                        //JSONObject innerJObject = jObject.getJSONObject(key);
                        JSONArray jArray = jObject.getJSONArray(key);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonobject = jArray.getJSONObject(i);

                            //String supervisorId = jsonobject.getString("supervisor_id");
                            //getSupervisorName(supervisorId);

                            GetsHour getshour = new GetsHour();
                            getshour.id = jsonobject.getString("id");
                            getshour.date = jsonobject.getString("date");
                            getshour.supervisor = jsonobject.getString("supervisor");
                            getshour.remarks = jsonobject.getString("remarks");
                            getshour.updatedOn = jsonobject.getString("updated_on");
                            getshour.name = operator_name;
                            getshour.job = jsonobject.getString("job");
                            getshour.clockInTime = jsonobject.getString("hours_start");
                            getshour.clockOutTIme = jsonobject.getString("hours_finish");
                            getshour.totalTime = jsonobject.getString("hours_total");
                            getList.add(getshour);
                        }
                        adapterHour = new GetHourAdapter(Hours.this, getList);
                        recycler_get_hours.setAdapter(adapterHour);
                        swipeRefreshLayout.setRefreshing(false);


                        /*Iterator<String> innerKeys = innerJObject.keys();
                        while (innerKeys.hasNext()) {
                            String innerKkey = keys.next();
                            String value = innerJObject.getString(innerKkey);
                            Log.v("key = " + key, "value = " + value);
                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        //String supervisorId = jsonobject.getString("supervisor_id");
                        //getSupervisorName(supervisorId);

                        Gets gets = new Gets();
                        gets.id = jsonobject.getString("id");
                        gets.date = jsonobject.getString("date");
                        gets.operator_id = jsonobject.getString("operator_id");
                        gets.supervisor_id = jsonobject.getString("supervisor_id");
                        gets.description = jsonobject.getString("description");
                        getList.add(gets);
                    }
                    adapter = new GetAdapter(DailyTask.this, getList);
                    recycler_get.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void parseJson(String response) {

    }

    public boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    @Override
    public void onRefresh() {
        getData();
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
}
