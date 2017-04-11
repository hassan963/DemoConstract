package com.constructefile.democonstract.Activity;

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
import com.constructefile.democonstract.Adapter.GetAdapter;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;
import com.constructefile.democonstract.models.Gets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyTask extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener {


    public CoordinatorLayout coordinatorLayout;
    public Button btnConnectivityCheck;
    public boolean isConnected;
    public RecyclerView recycler_get;
    public List<Gets> getList;
    public GetAdapter adapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    private SQLiteHandler db;
    private SessionManager session;

    public String operator_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);

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


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recycler_get = (RecyclerView) findViewById(R.id.recycler_get);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_get.setLayoutManager(linearLayoutManager);
        recycler_get.setItemAnimator(new DefaultItemAnimator());

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
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.GET_ALL_DAILY_TASK + operator_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("all_posts", "Response: " + response.toString());
                getList = new ArrayList<>();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

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
