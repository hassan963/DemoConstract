package com.constructefile.democonstract.activity;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hassan M.Ashraful on 11/26/2016.
 */

public class LoginActivity extends AppCompatActivity {
    String id;
    String server_user_id;
    String f_name;
    String l_name;
    String user_email;
    String date_started;
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
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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
            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
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
                    if (checkAndRequestPermissions()) {
                        //checkForPhoneStatePermission();
                        // carry on the normal flow, as the case of  permissions  granted.
                        //checkLogin(email, password);
                        getDeviceUuId(LoginActivity.this);

                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }


            }
        });
    }






    //First one IMEI

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        String email = inputEmail.getText().toString().trim();
                        String password = inputPassword.getText().toString().trim();
                        checkLogin(email, password);
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                            showDialogOK("Read Storage and Write Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    public void getDeviceUuId(Activity context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phnID = telephonyManager.getDeviceId();
        //Toast.makeText(this, phnID, Toast.LENGTH_LONG).show();
        Log.i("imei", phnID);

        //357743070485838
        //if (phnID.equals("")) {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            //Toast.makeText(context, " IMEI Matched", Toast.LENGTH_SHORT).show();
            checkLogin(email, password);


        /*} else {
            Toast.makeText(context, "Sorry...IMEI Number Did Not Matched!!! ", Toast.LENGTH_SHORT).show();
        }*/
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
                        Log.i("login_status", String.valueOf(session.isLoggedIn()));

                        for (int n = 0; n < jArray.length(); n++) {
                            JSONObject object = jArray.getJSONObject(n);

                            // Now store the user in SQLite
                            server_user_id = object.getString("id");
                            id = object.getString("operator_no");
                            f_name = object.getString("first_name");
                            l_name = object.getString("last_name");
                            user_email = object.getString("email");
                            date_started = object.getString("date_started");


                            // Inserting row in users table
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
                            db.addUser(server_user_id, f_name, l_name, user_email, id, login_at_date, login_at_time, date_started);
                            getEquipmentFromWebAndInsertIntoDB();
                            getListFromWebAndInsertIntoDB();
                            getAllSuperVisorAndInsert();
                            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                            startActivity(intent);
                            finish();
                            /*
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
                                            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
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
                                    int am_pm = c.get(Calendar.AM_PM);
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
                            //end of shift insertion*/
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

    public void getEquipmentFromWebAndInsertIntoDB() {
        // Toast.makeText(getApplicationContext(), AppConfig.URL_ALL_VEHICLES, Toast.LENGTH_SHORT).show();
        showDialog();
        String tag_string_req = "req_get_all_equipments";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_ALL_EQUIPMENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.i("all_equipments", "Response: " + response.toString());
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String equipment_server_id = jsonobject.getString("id");
                        String name = jsonobject.getString("name");
                        Log.i("serial_equipment", equipment_server_id + " - " + name);
                        //Toast.makeText(LoginActivity.this, "1st One " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();
                        db.insertEquipment(equipment_server_id, name);
                        Log.i("labelInsert", equipment_server_id + " " + name);
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


    public void getAllSuperVisorAndInsert() {

        showDialog();
        String tag_string_req = "req_get_all_supervisor";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_GET_SUPERVISOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.i("all_supervisor", "Response: " + response.toString());
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String first_name = jsonobject.getString("first_name");
                                String last_name = jsonobject.getString("last_name");
                                String server_id = jsonobject.getString("id");
                                String full_name = first_name + " " + last_name;

                                Log.i("serial_supervisor", server_id + " - " + first_name + " - " + last_name);
                                //Toast.makeText(LoginActivity.this, "1st One " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();
                                db.insertSupervisor(server_id, full_name);
                                // Log.i("labelInsert", vehicle_type_id + " " + vehicle_id + " " + serial_no);
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
