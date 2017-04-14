package com.constructefile.democonstract;

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
import com.constructefile.democonstract.activity.CategoryActivity;
import com.constructefile.democonstract.activity.LoginActivity;
import com.constructefile.democonstract.adapter.PagerAdapter;
import com.constructefile.democonstract.content.SpinnerData;
import com.constructefile.democonstract.fragment.FormFragment;
import com.constructefile.democonstract.fragment.FormFragmentTwo;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {


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

        // Spinner element
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);

        // Spinner click listener
        spinnerModel.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        viewpager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);

        menu1 = (FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        menu2 = (FloatingActionButton) findViewById(R.id.subFloatingMenu2);

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
                        frag1.postDATA(TRUCK_ID_SELECTED, user_id, shift_id);
                    } else if (viewpager.getCurrentItem() == 1) {
                        FormFragmentTwo frag2 = (FormFragmentTwo) viewpager.getAdapter().instantiateItem(viewpager, viewpager.getCurrentItem());
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
}
