package com.constructefile.democonstract.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hassan M.Ashraful on 11/26/2016.
 */

public class LoginActivity extends AppCompatActivity {
    String id;
    String f_name;
    String l_name;
    String user_email;
    String login_at_date;
    String login_at_time;
    String shift_id;
    private static final int REQUEST_PHONE_STATE = 1;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    //private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        //btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {

                    //checkForPhoneStatePermission();
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }


    private void checkForPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showPermissionMessage();

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PHONE_STATE);
                }

            } else {
                //... Permission has already been granted, obtain the UUID
                getDeviceUuId(LoginActivity.this);
            }

        } else {
            //... No need to request permission, obtain the UUID
            getDeviceUuId(LoginActivity.this);
        }
    }


    private void showPermissionMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Read phone state")
                .setMessage("This app requires the permission to read phone state to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this,
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
                    getDeviceUuId(LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getDeviceUuId(Activity context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phnID = telephonyManager.getDeviceId();
        //Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();
        //Log.i("imei", phnID);

        if (phnID.equals("357743070485838")) {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            Toast.makeText(context, " IMEI Matched", Toast.LENGTH_SHORT).show();
            checkLogin(email, password);
        } else {
            Toast.makeText(context, "Sorry...IMEI Number Did Not Matched!!! ", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                Log.i(TAG, "Login Response: " + response.toString());
                //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    char first = response.charAt(0);

                    if (first == '{') {
                        // Its an object so there is an error
                        JSONObject jObj = new JSONObject(response);
                        String error = jObj.getString("error");
                        if (error != null) {
                            Log.i(TAG, "Login Response: " + error);
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    } else if (first == '[') {
                        // Its an array so result is successful
                        // user successfully logged in
                        JSONArray jArray = new JSONArray(response);

                        // Create login session
                        session.setLogin(true);

                        for (int n = 0; n < jArray.length(); n++) {
                            JSONObject object = jArray.getJSONObject(n);

                            // Now store the user in SQLite
                            id = object.getString("id");
                            f_name = object.getString("first_name");
                            l_name = object.getString("last_name");
                            user_email = object.getString("email");


                            //insert data into shift table
                            String tag_string_req = "req_insert_shift";
                            //insertion
                            showDialog();
                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_INSERT_SHIFT, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.i("insert_shift", "Response: " + response.toString());
                                    hideDialog();
                                    try {
                                        JSONObject jObj = new JSONObject(response);

                                        // Now store the user in SQLite
                                        shift_id = jObj.getString("id");

                                        if (shift_id != null) {
                                            // Shift Inserted successfully so Launch main activity
                                            Log.i("insert_shift", "shift id: " + shift_id);


                                            Calendar c = Calendar.getInstance();
                                            /*String date = c.get(Calendar.YEAR) + ":" + c.get(Calendar.MONTH) + ":" + c.get(Calendar.DATE) + " ";
                                            String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);*/
                                            int am_pm = c.get(Calendar.AM_PM);
                                            String ampm;
                                            if (am_pm == 1) {
                                                ampm = " PM";
                                            } else {

                                                ampm = " AM";
                                            }
                                            // Inserting row in users table
                                            db.addUser(f_name, l_name, user_email, id, login_at_date, login_at_time, shift_id);
                                            getListFromWebAndInsertIntoDB();
                                            Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.i("insert_shift", "shift was not inserted");
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
                                    hideDialog();
                                    Log.i("insert_shift", "insert_shift Error: " + error.getMessage());
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

                                    login_at_date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
                                    login_at_time = hr + ":" + min + ":" + sec;
                                    //int ampm= c.get(Calendar.AM_PM);
                                    int am_pm = c.get(Calendar.AM_PM);
                                    //String ampm;
                                    /*if (am_pm == 1) {
                                        login_at_time = login_at_time + " PM";
                                    } else {
                                        login_at_time = login_at_time + " AM";
                                    }*/

                                    /*String timestamp = date + " " + time;
                                    Log.i("time", date + " " + time);*/
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("operator_id", id);
                                    params.put("start_time", login_at_time);
                                    params.put("end_time", login_at_time);
                                    params.put("shift_date", login_at_date);

                                    return params;
                                }

                            };

                            // Adding request to request queue
                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

                            //end of insertion
                        }
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.i(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getListFromWebAndInsertIntoDB() {
        // Toast.makeText(getApplicationContext(), AppConfig.URL_ALL_VEHICLES, Toast.LENGTH_SHORT).show();
        showDialog();
        String tag_string_req = "req_get_all_vehicles";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_ALL_VEHICLES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                hideDialog();
                Log.i("all_vehicles", "Response: " + response.toString());
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String vehicle_id = jsonobject.getString("id");
                        String model = jsonobject.getString("model");
                        String serial_no = jsonobject.getString("serial_no");
                        String vehicle_type_id = jsonobject.getString("vehicle_type_id");
                        Log.i("serial_model", vehicle_type_id + " - " + vehicle_id + " - " + serial_no);
                        //Toast.makeText(LoginActivity.this, "1st One " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();

                        db.insertLabel(vehicle_type_id, vehicle_id, serial_no);
                        Log.i("labelInsert", vehicle_type_id + " " + vehicle_id + " " + serial_no);
                        //Toast.makeText(LoginActivity.this, "labelInsert " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                error.printStackTrace();
            }
        });
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


    @Override
    public void onBackPressed() {
        //To EXIT the game
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
