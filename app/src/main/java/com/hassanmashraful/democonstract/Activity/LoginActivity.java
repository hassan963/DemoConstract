package com.hassanmashraful.democonstract.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.hassanmashraful.democonstract.R;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

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
    String login_at;
    String shift_id;
/*
    private static final String TAG = "LoginActivity";
    @InjectView(R.id.user_id)
    EditText user_id;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //getSupportActionBar().hide();
        ButterKnife.inject(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        String user_id = this.user_id.getText().toString();
        String password = _passwordText.getText().toString();
        // TODO: Implement your own authentication logic here.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    *//*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*//*
            *//*@Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }*//*
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
        startActivity(i);
        finish(); //for this, cant back to login panel again
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;
        String user_id = this.user_id.getText().toString();
        String password = _passwordText.getText().toString();
        if (user_id.isEmpty()) {
            this.user_id.setError("enter a valid user id address");
            valid = false;
        } else {
            this.user_id.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        return valid;
    }*/


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

        // Link to Register Screen
        /*btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });*/

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
                Log.i(TAG, "Login Response: " + response.toString());
                //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                hideDialog();

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

                            Calendar c = Calendar.getInstance();
                            String date = c.get(Calendar.YEAR) + ":" + c.get(Calendar.MONTH) + ":" + c.get(Calendar.DATE) + " ";
                            String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                            int am_pm = c.get(Calendar.AM_PM);
                            String ampm;
                            if (am_pm == 1) {
                                ampm = " PM";
                            } else {

                                ampm = " AM";
                            }

                            login_at = date + time + ampm;

                            // Inserting row in users table
                            db.addUser(f_name, l_name, user_email, id, login_at);

                            //insert data into shift table

                            String tag_string_req = "req_insert_shift";
                            //insertion
                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                    AppConfig.URL_INSERT_SHIFT, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    Log.i("insert_shift", "Response: " + response.toString());

                                    try {
                                        JSONObject jObj = new JSONObject(response);

                                        // Now store the user in SQLite
                                        shift_id = jObj.getString("id");

                                        if (shift_id != null) {
                                            // Shift Inserted successfully so Launch main activity
                                            Log.i("insert_shift", "shift id: " + shift_id);
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
                                    Log.i("insert_shift", "insert_shift Error: " + error.getMessage());
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
                                    params.put("operator_id", id);
                                    params.put("start_time", time);
                                    params.put("end_time", time);
                                    params.put("shift_date", date);

                                    return params;
                                }

                            };

                            // Adding request to request queue
                            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

                            //end of insertion

                            // end of inserttion


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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
