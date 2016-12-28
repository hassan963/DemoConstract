package com.hassanmashraful.democonstract.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.democonstract.MainActivity;
import com.hassanmashraful.democonstract.R;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtLogin;

    private SQLiteHandler db;
    private SessionManager session;

    String f_name;
    String l_name;
    String user_id;
    String email;
    String login_at_date;
    String login_at_time;
    String shift_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtLogin = (TextView) findViewById(R.id.loginAt);

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
        login_at_date = user.get("login_at_date");
        login_at_time = user.get("login_at_time");
        shift_id = user.get("shift_id");

        // Displaying the user details on the screen
        txtName.setText("Name: " + f_name + " " + l_name);
        txtEmail.setText("Email: " + email);
        txtLogin.setText("Login At: " + login_at_date + " " + login_at_time);

        //TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String phnID = telephonyManager.getDeviceId();
        //Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();

        String identifier = null;
        TelephonyManager tm = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier .length() == 0)
            identifier = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(getApplicationContext(), identifier, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.checkout_msg) {

            //The tab with id R.id.tab_msg was selected,
            //Ask employee to leave checkout message and checkout for that day
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

            builder.setTitle("Checking Out? Leave A Message");
            builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
            builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Send Checkout message
                            and
                            logout for that day*/

                            AlertDialog.Builder abuilder = new AlertDialog.Builder(ProfileActivity.this);

                            abuilder.setTitle("Checkout Message");
                            final EditText checkoutContent = new EditText(ProfileActivity.this);
                            abuilder.setView(checkoutContent);

                            abuilder.setPositiveButton("Send and Checkout", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            //check if user input is null
                                            if (checkoutContent != null) {

                                                //if input not null then insert whats typed
                                                Log.i("AppInfo", String.valueOf(checkoutContent.getText()));
                                                //Toast.makeText(ProfileActivity.this, String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();

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
                                                                Toast.makeText(ProfileActivity.this, "Checkout Successfully.", Toast.LENGTH_SHORT).show();

                                                                /*
                                                                * here comes the pain
                                                                * */

                                                                String tag_string_req = "req_update_shift";
                                                                //insertion
                                                                StringRequest strReq = new StringRequest(Request.Method.POST,
                                                                        AppConfig.URL_UPDATE_SHIFT + shift_id, new Response.Listener<String>() {

                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        //Log.i("update_shift", "Response: " + response.toString());
                                                                        //Toast.makeText(ProfileActivity.this, "User ID" + user_id + "Shift ID" + shift_id + " " + login_at_time + response.toString(), Toast.LENGTH_SHORT).show();
                                                                        try {
                                                                            JSONObject jObj = new JSONObject(response);

                                                                            // Now store the user in SQLite
                                                                            String status = jObj.getString("status");

                                                                            if (status != null) {
                                                                                Toast.makeText(ProfileActivity.this, "Shift Ended Successfully.", Toast.LENGTH_SHORT).show();
                                                                                logoutUser();
                                                                            } else {
                                                                                Toast.makeText(ProfileActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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
                                                                        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
                                                                        String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                                                                        //int ampm= c.get(Calendar.AM_PM);
                                                                        int am_pm = c.get(Calendar.AM_PM);
                                                                        //String ampm;
                                                                        if (am_pm == 1) {
                                                                            time = time + " PM";
                                                                        } else {
                                                                            time = time + " AM";
                                                                        }
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
                                                                Toast.makeText(ProfileActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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


        } else if (id == R.id.category) {

            // Launch profile activity

            Intent intent = new Intent(ProfileActivity.this, CategoryActivity.class);
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
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
