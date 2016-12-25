package com.hassanmashraful.democonstract;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hassanmashraful.democonstract.Activity.CategoryActivity;
import com.hassanmashraful.democonstract.Activity.LoginActivity;
import com.hassanmashraful.democonstract.Adapter.PagerAdapter;
import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.Fragment.FormFragment;
import com.hassanmashraful.democonstract.Fragment.FormFragmentTwo;
import com.hassanmashraful.democonstract.Task.BackgroundTask;
import com.hassanmashraful.democonstract.helper.SQLiteHandler;
import com.hassanmashraful.democonstract.helper.SessionManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private SQLiteHandler db;
    private SessionManager session;
    private int pressBTN;;

    List<String> list = new ArrayList<>();

    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();

    ViewPager viewpager;
    FormFragment formFragment;
    FormFragmentTwo formFragmentTwo;
    // Spinner element
    Spinner spinnerModel, spinnerSerial;

    private static short backStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting session
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pressBTN = 0;
            } else {
                pressBTN= extras.getInt("CATEGORY");
            }
        } else {
            pressBTN= (Integer) savedInstanceState.getSerializable("CATEGORY");
        }



        BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this, pressBTN);
        list = backgroundTask.getList();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        // Spinner element
        spinnerModel = (Spinner) findViewById(R.id.spinnerModel);
        loadSpinnerModelData();




        spinnerSerial = (Spinner) findViewById(R.id.spinnerSerial);
        loadSpinnerSerialData();

        // Spinner click listener
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        // Spinner click listener
        spinnerSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        viewpager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_SHORT).show();
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
                    formFragment = (FormFragment) padapter.getCurrentFragment();

                    formFragment.displayDialog();
                }
            }
        });


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
    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

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


    /**
     * Function to load the spinnerModel data from SQLite database
     */
    private void loadSpinnerModelData() {

        // Spinner Drop down elements




        /*
        list.add("First");
        list.add("Second");
        list.add("Third");
        list.add("Fourth");
        list.add("Five");
        list.add("Six");*/

        // Creating adapter for spinnerModel
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinnerModel
        spinnerModel.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }


    private void loadSpinnerSerialData() {

        // Spinner Drop down elements

        List<String> listT = new ArrayList<>();
        listT.add("Tools");
        listT.add("Fools");
        listT.add("Cools");
        listT.add("Do");
        listT.add("SSS");
        listT.add("VVVV");


        // Creating adapter for spinnerModel
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listT){
            @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            TextView text = (TextView)view.findViewById(android.R.id.text1);
            text.setTextColor(Color.BLUE);

            return view;
        }
    };



        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinnerModel
        spinnerSerial.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();



    }

    public void show() {
        for (int i = 0; i < spinnerDatas.size(); i++) {
            Log.v("DATA ", spinnerDatas.get(i).getModel());
            Toast.makeText(getApplicationContext(), spinnerDatas.get(0).getModel(), Toast.LENGTH_SHORT).show();
        }

    }




}
