package com.hassanmashraful.democonstract.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.democonstract.Adapter.CategoryAdapter;
import com.hassanmashraful.democonstract.Content.CategoryData;
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
import java.util.Map;

/**
 * Created by Hassan M.Ashraful on 11/26/2016.
 */

public class CategoryActivity extends AppCompatActivity {


    private SQLiteHandler db;
    private SessionManager session;
    String f_name;
    String l_name;
    String user_id;
    String email;
    String shift_id;
    String login_date;
    String login_time;

    private final String recyclerViewTitleText[] = {"Bulldozer",
            "Concretemixer",
            "Crane",
            "Dump Truck",
            "Excavator",
            "Flatbed Truck",
            "Front & back hoe Truck",
            "Front loader Truck",
            "Fuel Truck",
            "Road Roller",
            "Skid Steer Truck"
    };

    private final int recyclerViewImages[] = {
            R.drawable.bulldozer,
            R.drawable.concretemixer,
            R.drawable.crane,
            R.drawable.dumptruck,
            R.drawable.excavator,
            R.drawable.flatbed,
            R.drawable.frontbackhoe,
            R.drawable.frontloader,
            R.drawable.fuel,
            R.drawable.roadroller,
            R.drawable.skidsteertruck

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
        initRecyclerViews();
        // SqLite database handler
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
        user_id = user.get("id");
        email = user.get("email");
        shift_id = user.get("shift_id");
        login_time = user.get("login_at_time");
        login_date = user.get("login_at_date");


        /*BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_cat) {
                    // The tab with id R.id.tab_cat was selected,
                    // change your content to CategoryActivity.
                    // Launch Category activity
                    Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                    startActivity(intent);
                } else if (tabId == R.id.tab_msg) {

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

                                    Log.i("AppInfo", String.valueOf(checkoutContent.getText()));
                                    Toast.makeText(CategoryActivity.this, String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();
                                }
                            });
                            abuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            abuilder.show();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                } else if (tabId == R.id.tab_logout) {

                    // Logout
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);

                    builder.setTitle("Logout");
                    builder.setMessage("Do you want to logout?");
                    builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logoutUser();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            }
        });
*/

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }*/


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.checkout_msg) {

            //The tab with id R.id.tab_msg was selected,
            //Ask employee to leave checkout message and checkout for that day
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);

            builder.setTitle("Checking Out? Leave A Message");
            builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
            builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Send Checkout message
                            and
                            logout for that day*/

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
                                                Toast.makeText(CategoryActivity.this, String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();

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

                                                                // update started

                                                                /*String tag_string_req = "req_update_shift";
                                                                //insertion
                                                                StringRequest strReq = new StringRequest(Request.Method.PUT,
                                                                        AppConfig.URL_UPDATE_SHIFT + shift_id, new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        Log.i("update_shift", "Response: " + response.toString());

                                                                        try {
                                                                            JSONObject jObj = new JSONObject(response);

                                                                            // Now store the user in SQLite
                                                                            shift_id = jObj.getString("id");

                                                                            if (shift_id != null) {
                                                                                // Shift Updated successfully so Logout
                                                                                Log.i("update_shift", "shift id: " + shift_id);

                                                                                logoutUser();
                                                                            } else {
                                                                                Log.i("insert_shift", "shift was not Updated");
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
                                                                        Log.i("update_shift", "update_shift Error: " + error.getMessage());
                                                                        Toast.makeText(getApplicationContext(),
                                                                                error.getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }) {

                                                                    @Override
                                                                    protected Map<String, String> getParams() {

                                                                        // Posting parameters to insert_check_message url
                                                                        Calendar c = Calendar.getInstance();
                                                                        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
                                                                        String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                                                                        //int ampm= c.get(Calendar.AM_PM);

                                                                        String timestamp = date + " " + time;
                                                                        Log.i("time", date + " " + time);
                                                                        Map<String, String> params = new HashMap<String, String>();
                                                                        params.put("operator_id", user_id);
                                                                        params.put("start_time", login_time);
                                                                        params.put("end_time", time);
                                                                        params.put("shift_date", login_date);

                                                                        return params;
                                                                    }

                                                                };
                                                                // Adding request to request queue
                                                                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);*/

                                                                //end of update
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
                                                        Log.i("checkout_insert", "Insertion Error: " + error.getMessage());
                                                        Toast.makeText(getApplicationContext(),
                                                                error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {

                                                    @Override
                                                    protected Map<String, String> getParams() {

                                                        // Posting parameters to insert_check_message url
                                                        Calendar c = Calendar.getInstance();
                                                        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
                                                        String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
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

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()

                    {
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

        // Launching the login activity
        Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
