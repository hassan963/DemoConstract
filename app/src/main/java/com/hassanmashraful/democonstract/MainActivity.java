package com.hassanmashraful.democonstract;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hassanmashraful.democonstract.Activity.CategoryActivity;
import com.hassanmashraful.democonstract.Activity.LoginActivity;
import com.hassanmashraful.democonstract.Adapter.PagerAdapter;
import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.Fragment.FormFragment;
import com.hassanmashraful.democonstract.Fragment.FormFragmentTwo;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener {


    String TRUCK_ID_SELECTED = "";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    private static final String TAG_BACK = "button_back";
    private static final String TAG_SEND = "button_send";


    String f_name;
    String l_name;
    String user_id;
    String email;
    String login_at_date;
    String login_at_time;
    String shift_id;

    ArrayAdapter<String> dataModelAdapter;

    private SQLiteHandler db;
    private SessionManager session;
    private int pressBTN;

    List<String> list = new ArrayList<>();
    List<String> ids;
    List<String> lables;

    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();
    FloatingActionButton menu1, menu2, menu3;
    ViewPager viewpager;
    FormFragment formFragment;
    FormFragmentTwo formFragmentTwo;
    // Spinner element
    Spinner spinnerModel, spinnerSerial;
    //PagerAdapter padapter;
    private static short backStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        user_id = user.get("id");
        email = user.get("email");
        login_at_date = user.get("login_at_date");
        login_at_time = user.get("login_at_time");
        shift_id = user.get("shift_id");


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

        /*BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this, pressBTN);
        list = backgroundTask.getList();*/

        //BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        // Spinner element
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);

        // Spinner click listener
        spinnerModel.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();
        //loadSpinnerModelData();


        /*spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        loadSpinnerSerialData();*/

        // Spinner click listener
        /*spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnerModel.setSelection(list.get(position));
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                spinnerModel.setSelection(spinnerModel.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        // Spinner click listener
        /*spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerModel.setSelection(position);
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        viewpager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);


        /*bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    // Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_SHORT).show();
                    show();
                } else if (tabId == R.id.tab_back) {
                    //oneFragment.save();
                    //backStatus = 1;
                    Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(i);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_post) {
                    formFragment = (FormFragment) padapter.getCurrentFragment();
                    formFragment.postDATA();

                    //formFragmentTwo = (FormFragmentTwo) padapter.getCurrentFragment();
                    //formFragmentTwo.postDATA();
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_logout) {
                    *//*formFragment = (FormFragment) padapter.getCurrentFragment();
                    formFragment.displayDialog();*//*


                    //The tab with id R.id.tab_msg was selected,
                    //Ask employee to leave checkout message and checkout for that day
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Checking Out? Leave A Message");
                    builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
                    builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                            *//*Send Checkout message
                            and
                            logout for that day*//*

                                    AlertDialog.Builder abuilder = new AlertDialog.Builder(MainActivity.this);

                                    abuilder.setTitle("Checkout Message");
                                    final EditText checkoutContent = new EditText(MainActivity.this);
                                    abuilder.setView(checkoutContent);

                                    abuilder.setPositiveButton("Send and Checkout", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //check if user input is null
                                                    if (checkoutContent != null) {

                                                        //if input not null then insert whats typed
                                                        Log.i("AppInfo", String.valueOf(checkoutContent.getText()));
                                                        //Toast.makeText(MainActivity.this, String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();

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
                                                                        Toast.makeText(MainActivity.this, "Checkout Successfully.", Toast.LENGTH_SHORT).show();

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
                                                                                //Toast.makeText(MainActivity.this, "User ID" + user_id + "Shift ID" + shift_id + " " + login_at_time + response.toString(), Toast.LENGTH_SHORT).show();
                                                                                try {
                                                                                    JSONObject jObj = new JSONObject(response);

                                                                                    // Now store the user in SQLite
                                                                                    String status = jObj.getString("status");

                                                                                    if (status != null) {
                                                                                        Toast.makeText(MainActivity.this, "Shift Ended Successfully.", Toast.LENGTH_SHORT).show();
                                                                                        logoutUser();
                                                                                    } else {
                                                                                        Toast.makeText(MainActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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
                                                                        Toast.makeText(MainActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
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


                }
            }
        });*/


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            /*if (getActionBar() != null){
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
*/

           /* toolbar.setNavigationIcon(R.drawable.up);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    onBackPressed();

                }

            });
*/





       /* if(getActionBar() != null){
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Title");
            setActionBar(toolbar);

            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setHomeAsUpIndicator(R.drawable.up);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
*/
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
            }
        });*/

       /* ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_menu);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setBackgroundDrawable(R.drawable.bg_round)
                .setPosition(5)
                .build();

        ImageView iconBack = new ImageView(this); // Create an icon
        iconBack.setImageResource(R.drawable.ic_return);
        ImageView iconSubmit = new ImageView(this); // Create an icon
        iconSubmit.setImageResource(R.drawable.ic_sent);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_round_small));
        SubActionButton buttonBack = itemBuilder.setContentView(iconBack).build();
        SubActionButton buttonSend = itemBuilder.setContentView(iconSubmit).build();


        buttonBack.setTag(TAG_BACK);
        buttonSend.setTag(TAG_SEND);
        buttonBack.setOnClickListener(this);
        buttonSend.setOnClickListener(this);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonBack)
                .addSubActionView(buttonSend)
                .attachTo(actionButton)
                .setEndAngle(2)
                .build(); */


        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        //menu3 = (FloatingActionButton)findViewById(R.id.subFloatingMenu3) ;

        menu1.setTag(TAG_SEND);
        menu2.setTag(TAG_BACK);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(MainActivity.this , "BackUp Icon clicked", Toast.LENGTH_LONG).show();
                if (v.getTag().equals(TAG_SEND)) {
                    //Toast.makeText(MainActivity.this, "SEND ", Toast.LENGTH_SHORT).show();
                    if (viewpager.getCurrentItem() == 0) {
                        FormFragment frag1 = (FormFragment) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                        //frag1.updateList("FFFFFFFFFFFFFFFFFFFFFFF");
                        //Toast.makeText(MainActivity.this, "SEND 1", Toast.LENGTH_SHORT).show();
                        //String truck_id = ids.get(spinnerModel.getSelectedItemPosition());
                        frag1.postDATA(TRUCK_ID_SELECTED, user_id, shift_id);
                    } else if (viewpager.getCurrentItem() == 1) {
                        FormFragmentTwo frag2 = (FormFragmentTwo) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                        //frag2.updateList("SSSSSSSSSSSSSSSSSSSSSSSS");
                        //Toast.makeText(MainActivity.this, "SEND 2", Toast.LENGTH_SHORT).show();
                        //String truck_id = ids.get(spinnerModel.getSelectedItemPosition());
                        frag2.postDATA(TRUCK_ID_SELECTED, user_id, shift_id);
                    }

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

        /*menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this , "Settings Icon clicked", Toast.LENGTH_LONG).show();

            }
        });*/

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        TRUCK_ID_SELECTED = ids.get(position);
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "You selected: " + label,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();

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
       /* switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }
        return super.onOptionsItemSelected(item);


    }*/
/*

    */

    /**
     * Function to load the spinnerModel data from SQLite database
     *//*
    private void loadSpinnerModelData() {
        // Spinner Drop down elements
        *//*list.add("Ford2018");
        list.add("Ford2017");
        list.add("FF");*//*

        String selectedValue = "";

        // Creating adapter for spinnerModel
        dataModelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);

        // Drop down layout style - list view with radio button
        dataModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerModel.setAdapter(dataModelAdapter);
        dataModelAdapter.notifyDataSetChanged();
    }*/


    /*private void loadSpinnerSerialData() {
        // Spinner Drop down elements
        List<String> listT = new ArrayList<>();
        listT.add("Truck-456");
        listT.add("Truck-123456");
        listT.add("TT");

        // Creating adapter for spinnerModel
        dataSerialAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listT) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLUE);

                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataSerialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerSerial.setAdapter(dataSerialAdapter);
        dataSerialAdapter.notifyDataSetChanged();
    }*/
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

/*
    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(), "SHOEBJJHB", Toast.LENGTH_SHORT).show();
        Log.i("***********************", "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        if (v.getTag().equals(TAG_BACK)) {
            //Toast.makeText(MainActivity.this, "Back ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        }
        if (v.getTag().equals(TAG_SEND)) {
            //Toast.makeText(MainActivity.this, "SEND ", Toast.LENGTH_SHORT).show();
            if (viewpager.getCurrentItem() == 0) {
                FormFragment frag1 = (FormFragment) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                //frag1.updateList("FFFFFFFFFFFFFFFFFFFFFFF");
                frag1.postDATA();
            } else if (viewpager.getCurrentItem() == 1) {
                FormFragmentTwo frag2 = (FormFragmentTwo) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
                //frag2.updateList("SSSSSSSSSSSSSSSSSSSSSSSS");
                frag2.postDATA();
            }

        }
    }*/
}
