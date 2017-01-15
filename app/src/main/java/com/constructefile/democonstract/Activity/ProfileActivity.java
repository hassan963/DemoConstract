package com.constructefile.democonstract.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.constructefile.democonstract.R;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import java.util.HashMap;

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
    private static final int REQUEST_PHONE_STATE = 1;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private TelephonyManager mTelephonyManager;

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
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtLogin = (TextView) findViewById(R.id.loginAt);


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

        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phnID = telephonyManager.getDeviceId();
        Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();
        Log.i("imei", phnID);*/
/*
<<<<<<< Updated upstream
        //TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String phnID = telephonyManager.getDeviceId();
        //Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();


=======
        *//*TelephonyManager telephonyManager = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
        String phnID = telephonyManager.getDeviceId();
        //Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();
        Log.i("imei", phnID);*//*
>>>>>>> Stashed changes*/

        /*String identifier = null;
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(getApplicationContext(), identifier, Toast.LENGTH_LONG).show();*/


        //check for imei
        //checkForPhoneStatePermission();
        //checkForPhoneStatePermission();

    }

    private void checkForPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showPermissionMessage();

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PHONE_STATE);
                }

            } else {
                //... Permission has already been granted, obtain the UUID
                getDeviceUuId(ProfileActivity.this);
            }

        } else {
            //... No need to request permission, obtain the UUID
            getDeviceUuId(ProfileActivity.this);
        }
    }


    private void showPermissionMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Read phone state")
                .setMessage("This app requires the permission to read phone state to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                REQUEST_PHONE_STATE);
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PHONE_STATE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // .. Can now obtain the UUID
                    getDeviceUuId(ProfileActivity.this);
                } else {
                    Toast.makeText(ProfileActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getDeviceUuId(Activity context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phnID = telephonyManager.getDeviceId();
        Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();
        Log.i("imei", phnID);

        /*Str
        String UUID = "";
        String android_id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        final TelephonyManager tm = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null) {
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            UUID = deviceUuid.toString();
            return UUID;
        }*/
        //return UUID;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
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
        /*if (id == R.id.checkout_msg) {

            //The tab with id R.id.tab_msg was selected,
            //Ask employee to leave checkout message and checkout for that day
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

            builder.setTitle("Checking Out? Leave A Message");
            builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
            builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            *//*Send Checkout message
                            and
                            logout for that day*//*

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


        } else */
        if (id == R.id.category) {

            // Launch profile activity

            Intent intent = new Intent(ProfileActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
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

        // Launching the login activity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
