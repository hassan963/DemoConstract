package com.constructefile.democonstract;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.constructefile.democonstract.activity.CategoryActivity;
import com.constructefile.democonstract.activity.LoginActivity;
import com.constructefile.democonstract.adapter.PagerAdapter;
import com.constructefile.democonstract.content.SpinnerData;
import com.constructefile.democonstract.fragment.FormFragment;
import com.constructefile.democonstract.fragment.FormFragmentTwo;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String TRUCK_ID_SELECTED = "";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    private static final String TAG_BACK = "button_back";
    private static final String TAG_SEND = "button_send";
    String server_user_id, date_started, email, l_name, f_name;

    ArrayAdapter<String> dataModelAdapter;

    private RecyclerView recyclerView;
    private ArrayList<FormData> formDatas;
    private FormOneAdapter formOneAdapter;



    private SQLiteHandler db;
    private SessionManager session;
    private int pressBTN;

    List<String> list = new ArrayList<>();
    List<String> ids;
    List<String> lables;

    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();
    FloatingActionButton menu1, menu2, menu3;
    /*ViewPager viewpager;
    FormFragment formFragment;
    FormFragmentTwo formFragmentTwo;*/
    // Spinner element
    Spinner spinnerModel, spinnerSerial;
    //PagerAdapter padapter;
    private static short backStatus = 0;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                pressBTN = 0;
            } else {
                pressBTN = extras.getInt("CATEGORY");
            }
        } else {
            pressBTN = (Integer) savedInstanceState.getSerializable("CATEGORY");
        }
        Log.i("clicked", pressBTN + "");
        ids = db.getAllLabelIds(String.valueOf(pressBTN));

        // Spinner element
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);

        // Spinner click listener
        spinnerModel.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();


        /*viewpager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);*/

        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);

        menu1.setTag(TAG_SEND);
        menu2.setTag(TAG_BACK);

        recyclerView = (RecyclerView) findViewById(R.id.recycleListView);

        formOneAdapter = new FormOneAdapter(getFormData(), getApplicationContext());
        recyclerView.setAdapter(formOneAdapter);
        formOneAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this , "BackUp Icon clicked", Toast.LENGTH_LONG).show();
                if (v.getTag().equals(TAG_SEND)) {
                    //Toast.makeText(MainActivity.this, "SEND ", Toast.LENGTH_SHORT).show();
                   /* if (viewpager.getCurrentItem() == 0) {
                        FormFragment frag1 = (FormFragment) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                        frag1.postDATA(TRUCK_ID_SELECTED, server_user_id);
                    } else if (viewpager.getCurrentItem() == 0 && formNo.equals("2")) {
                        Fragment_Trench_Pipe frag2 = (Fragment_Trench_Pipe) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                        frag2.postDATA(TRUCK_ID_SELECTED, server_user_id);
                    }*/

                    postDATA(TRUCK_ID_SELECTED, server_user_id);
                }
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(TAG_BACK)) {
                    //Toast.makeText(MainActivity.this , " Alarm Icon clicked ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    /**
     * Function to load the spinner data from SQLite database
     */
    private void loadSpinnerData() {
        // database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Spinner Drop down elements
        lables = db.getAllLabels(Integer.toString(pressBTN));
        Log.i("labels", "load spinner data " + lables.toString());

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerModel.setAdapter(dataAdapter);
    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        TRUCK_ID_SELECTED = ids.get(position);
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "You selected: " + label,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }*/


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void show() {
        for (int i = 0; i < spinnerDatas.size(); i++) {
            Log.v("DATA ", spinnerDatas.get(i).getModel());
            Toast.makeText(getApplicationContext(), spinnerDatas.get(0).getModel(), Toast.LENGTH_SHORT).show();
        }

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

    public void postDATA(String truck_id, String user_id) {
/**
 * generate unique id for each form
 * **/
        Long tsLong = System.currentTimeMillis() / 1000;
        String idMill = tsLong.toString();
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();

        String id = idMill + output;
        Log.i("timestamp", "ID : " + id);

        for (int i = 0; i < formDatas.size(); i++) {
            save(i, id, truck_id, user_id);
        }

    }

    public void save(final int i, final String form_id, final String truck_id, final String user_id) {

        pDialog.setMessage("Sending Data...");
        showDialog();
        String tag_string_req = "req_operational_check";
        //insertion
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_OPERATIONAL_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                Log.i("operational_check", "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);

                    // Now store the user in SQLite
                    String id = jObj.getString("id");

                    if (id != null) {
                        Log.i("operational_check", "Inserted: " + id);
                    } else {
                        Log.i("operational_check", " Not Inserted: Unexpected Error! Try again later.");
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
                params.put("id", form_id);
                params.put("checklist_item_id", String.valueOf(formDatas.get(i).getId()));
                params.put("truck_id", truck_id);
                params.put("operator_id", user_id);
                params.put("status", String.valueOf(formDatas.get(i).getStatus()));
                params.put("maintenance", formDatas.get(i).getComment());
                params.put("timestamp", timestamp);

                //Toast.makeText(getActivity(),formDatas.get(i).getComment(),Toast.LENGTH_SHORT).show();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //end of insertion
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        TRUCK_ID_SELECTED = ids.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
