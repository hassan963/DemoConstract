package com.constructefile.democonstract.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.constructefile.democonstract.adapter.CategoryAdapter;
import com.constructefile.democonstract.content.CategoryData;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hassan M.Ashraful on 11/26/2016.
 */

public class CategoryActivity extends AppCompatActivity {


    private SQLiteHandler db;
    private SessionManager session;
    String server_user_id, date_started, email, l_name, f_name;

    private final String recyclerViewTitleText[] = {
            "Dozer",
            "Excavator",
            "Truck",
            "Back hoe",
            "Loader",
            "Company Truck",
            "Road Roller",
            "Skid Steer",
            "Crusher",
            "Screener"
    };

    private final int recyclerViewImages[] = {
            R.drawable.bulldozer,
            // R.drawable.concretemixer,
            //R.drawable.crane,
            // R.drawable.dumptruck,
            R.drawable.excavator,
            R.drawable.flatbed,
            R.drawable.frontbackhoe,
            R.drawable.frontloader,
            R.drawable.fuel,
            R.drawable.roadroller,
            R.drawable.skidsteertruck,
            R.drawable.crusher,
            R.drawable.screener

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }


        initRecyclerViews();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        //db.deleteLabels();
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

    }

    /*
    * getting recyclerview ready in this method calling by oncreate()
    * */
    private void initRecyclerViews() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<CategoryData> categoryDatas = prepareData();
        CategoryAdapter mAdapter = new CategoryAdapter(getApplicationContext(), categoryDatas);
        mRecyclerView.setAdapter(mAdapter);

    }

    /*
    * preparing static data for category adapter
    * */
    private ArrayList<CategoryData> prepareData() {

        ArrayList<CategoryData> categoryDatas = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            CategoryData categoryData = new CategoryData();
            categoryData.setrecyclerViewTitleText(recyclerViewTitleText[i]);
            categoryData.setrecyclerViewImage(recyclerViewImages[i]);
            categoryDatas.add(categoryData);
        }
        return categoryDatas;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();



        /*if (id == R.id.checkout_msg) {

            //The tab with id R.id.tab_msg was selected,
            //Ask employee to leave checkout message and checkout for that day
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);

            builder.setTitle("Checking Out? Leave A Message");
            builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
            builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            *//*Send Checkout message
                            and
                            logout for that day*//*

                            AlertDialog.Builder abuilder = new AlertDialog.Builder(CategoryActivity.this);

                            abuilder.setTitle("Checkout Message");
                            final EditText checkoutContent = new EditText(CategoryActivity.this);
                            abuilder.setView(checkoutContent);

                            abuilder.setPositiveButton("Send and Checkout", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //check if user input is null
                                            if (checkoutContent != null) {

                                                //if input not null then insert whats typed
                                                Log.i("AppInfo", String.valueOf(checkoutContent.getText()));
                                                //Toast.makeText(CategoryActivity.this, String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();

                                                String tag_string_req = "req_check_message";
                                                //insertion
                                                StringRequest strReq = new StringRequest(Request.Method.POST,
                                                        AppConfig.URL_INSERT_CHECKOUT_MESSAGE, new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("checkout_insert", "Response: " + response.toString());

                                                        try {
                                                            JSONObject jObj = new JSONObject(response);
                                                            // Now store the user in SQLite
                                                            String id = jObj.getString("id");
                                                            if (id != null) {
                                                                Toast.makeText(CategoryActivity.this, "Checkout Successfully.", Toast.LENGTH_SHORT).show();

                                                                *//*
                                                                * here comes the pain
                                                                * *//*

                                                                String tag_string_req = "req_update_shift";
                                                                //insertion
                                                                StringRequest strReq = new StringRequest(Request.Method.POST,
                                                                        AppConfig.URL_UPDATE_SHIFT + shift_id, new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        //Log.i("update_shift", "Response: " + response.toString());
                                                                        // Toast.makeText(CategoryActivity.this, "User ID" + user_id + "Shift ID" + shift_id + " " + login_at_time + response.toString(), Toast.LENGTH_SHORT).show();
                                                                        try {
                                                                            JSONObject jObj = new JSONObject(response);

                                                                            // Now store the user in SQLite
                                                                            String status = jObj.getString("status");

                                                                            if (status != null) {
                                                                                Toast.makeText(CategoryActivity.this, "Shift Ended Successfully.", Toast.LENGTH_SHORT).show();
                                                                                logoutUser();
                                                                            } else {
                                                                                Toast.makeText(CategoryActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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
                                                                        Log.i("update_shift", "Insertion Error: " + error.getMessage());
                                                                        Toast.makeText(getApplicationContext(),
                                                                                error.getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }) {

                                                                    @Override
                                                                    protected Map<String, String> getParams() {

                                                                        // Posting parameters to insert_check_message url
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
                                                                        String date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
                                                                        String time = hr + ":" + min + ":" + sec;
                                                                        //int ampm= c.get(Calendar.AM_PM);
                                                                        int am_pm = c.get(Calendar.AM_PM);
                                                                        //String ampm;
                                                                        *//*if (am_pm == 1) {
                                                                            time = time + " PM";
                                                                        } else {
                                                                            time = time + " AM";
                                                                        }*//*
                                                                        String timestamp = date + " " + time;
                                                                        Log.i("time", date + " " + time);
                                                                        Map<String, String> params = new HashMap<String, String>();
                                                                        params.put("operator_id", user_id);
                                                                        params.put("start_time", login_at_time);
                                                                        params.put("end_time", time);
                                                                        params.put("shift_date", login_at_date);
                                                                        params.put("_METHOD", "PUT");

                                                                        return params;
                                                                    }
                                                                };

                                                                // Adding request to request queue
                                                                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                                                                //end of insertion
                                                            } else {
                                                                Toast.makeText(CategoryActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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
                                                        Log.i("checkout_insert", "Insertion Error: " + error.getMessage());
                                                        Toast.makeText(getApplicationContext(),
                                                                error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {

                                                    @Override
                                                    protected Map<String, String> getParams() {

                                                        // Posting parameters to insert_check_message url
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
                                                        String date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
                                                        String time = hr + ":" + min + ":" + sec;
                                                        //int ampm= c.get(Calendar.AM_PM);

                                                        String timestamp = date + " " + time;
                                                        Log.i("time", date + " " + time);
                                                        Map<String, String> params = new HashMap<String, String>();
                                                        params.put("operator_id", user_id);
                                                        params.put("body", String.valueOf(checkoutContent.getText()));
                                                        params.put("timestamp", timestamp);

                                                        return params;
                                                    }
                                                };
                                                // Adding request to request queue
                                                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                                                //end of insertion
                                            }
                                        }
                                    }
                            );
                            abuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }
                            );
                            abuilder.show();
                        }
                    }
            );

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }
            );
            builder.show();
        } else if (id == R.id.profile) {

            // Launch profile activity
            Intent intent = new Intent(CategoryActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }*/

        //noinspection SimplifiableIfStatement
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
        db.deleteSupervisor();
        // Launching the login activity
        Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
