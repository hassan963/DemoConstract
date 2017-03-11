package com.constructefile.democonstract.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.constructefile.democonstract.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan M.Ashraful on 3/11/2017.
 */

public class Injury_Report_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    Button stepOne, stepTwo;
    RelativeLayout stepOneLayout, stepTwoLayout;
    boolean isSettingsClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injury_report);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.primary)));
        }


        stepOne = (Button) findViewById(R.id.stepOne);
        stepTwo = (Button) findViewById(R.id.stepTwo);

        stepOneLayout = (RelativeLayout) findViewById(R.id.stepOneLayout);
        stepTwoLayout = (RelativeLayout) findViewById(R.id.stepTwoLayout);


        // Spinner element
        Spinner spinneremployeeWorks = (Spinner) findViewById(R.id.spinneremployeeWorks);
        Spinner spinnerNature = (Spinner) findViewById(R.id.spinnerNature);
        Spinner spinnerworkday = (Spinner) findViewById(R.id.spinnerworkday);

        // Spinner click listener
        spinneremployeeWorks.setOnItemSelectedListener(this);
        spinnerNature.setOnItemSelectedListener(this);
        spinnerworkday.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinneremployeeWorks.setAdapter(dataAdapter);


        // Creating adapter for spinner
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNature.setAdapter(data);

        // Creating adapter for spinner
        ArrayAdapter<String> dataWork = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataWork.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerworkday.setAdapter(dataWork);




        stepOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSettingsClicked == false) {
                    stepOne.setBackgroundResource(R.color.colorAccent);
                    stepTwoLayout.setVisibility(View.GONE);
                    stepOneLayout.setVisibility(View.VISIBLE);
                    isSettingsClicked = true;
                } else if (isSettingsClicked == true) {
                    stepOneLayout.setVisibility(View.GONE);

                    // _settings.setBackgroundResource(null);
                    isSettingsClicked = false;
                    stepOne.setBackgroundResource(R.color.colorPrimary);
                }

            }
        });


        stepTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isSettingsClicked == false) {
                    stepTwo.setBackgroundResource(R.color.colorAccent);
                    stepOneLayout.setVisibility(View.GONE);
                    stepTwoLayout.setVisibility(View.VISIBLE);
                    isSettingsClicked = true;
                } else if (isSettingsClicked == true) {
                    stepTwoLayout.setVisibility(View.GONE);

                    // _settings.setBackgroundResource(null);
                    isSettingsClicked = false;
                    stepTwo.setBackgroundResource(R.color.colorPrimary);
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    // Pirates are the best
                    Toast.makeText(this, "male", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioFemale:
                if (checked)
                    // Ninjas rule
                    Toast.makeText(this, "female", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // On selecting a spinner item
        String item = adapterView.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
