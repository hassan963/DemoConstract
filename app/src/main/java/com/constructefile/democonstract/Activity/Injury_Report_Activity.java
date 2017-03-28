package com.constructefile.democonstract.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.Task.VolleySingleton;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.constructefile.democonstract.R.id.dateIncident;

/**
 * Created by Hassan M.Ashraful on 3/11/2017.
 */

public class Injury_Report_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    Button stepOne, stepTwo;
    RelativeLayout stepOneLayout, stepTwoLayout;
    boolean isSettingsClicked = false;
    private FloatingActionButton fabInjury;

    private static Injury_Report_Activity sInstance;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private String sOne, sTwo, sThree, sFour, sFive, gender;
    EditText dateIncidentET, nameET, departmentET, ageET, jobTitleET, monthsEmploeeET, monthsJobET, locationET, timeET, witnessET;

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injury_report);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                    .getColor(R.color.primary)));
        }

        sInstance = this;

        fabInjury = (FloatingActionButton) findViewById(R.id.fabInjury);
        dateIncidentET = (EditText) findViewById(dateIncident); nameET = (EditText) findViewById(R.id.name);
        departmentET = (EditText) findViewById(R.id.department); ageET = (EditText) findViewById(R.id.age);
        jobTitleET = (EditText) findViewById(R.id.jobTitle); monthsEmploeeET = (EditText) findViewById(R.id.monthsEmploee);
        monthsJobET = (EditText) findViewById(R.id.monthsJob); locationET = (EditText) findViewById(R.id.location);
        timeET = (EditText) findViewById(R.id.time); witnessET = (EditText) findViewById(R.id.witness);


        stepOne = (Button) findViewById(R.id.stepOne);
        stepTwo = (Button) findViewById(R.id.stepTwo);

        stepOneLayout = (RelativeLayout) findViewById(R.id.stepOneLayout);
        stepTwoLayout = (RelativeLayout) findViewById(R.id.stepTwoLayout);


        // Spinner element
        Spinner spinneremployeeWorks = (Spinner) findViewById(R.id.spinneremployeeWorks);
        Spinner spinnerNature = (Spinner) findViewById(R.id.spinnerNature);
        Spinner spinnerworkday = (Spinner) findViewById(R.id.spinnerworkday);
        Spinner spinnerReport = (Spinner) findViewById(R.id.spinnerReport);
        Spinner spinnerReportMade = (Spinner) findViewById(R.id.spinnerReportMade);

        // Spinner click listener
        spinneremployeeWorks.setOnItemSelectedListener(this);
        spinnerNature.setOnItemSelectedListener(this);
        spinnerworkday.setOnItemSelectedListener(this);
        spinnerReport.setOnItemSelectedListener(this);
        spinnerReportMade.setOnItemSelectedListener(this);


        List<String> reportList = new ArrayList<String>();
        reportList.add("Death");
        reportList.add("Lost Time");
        reportList.add("Dr. Visit Only");
        reportList.add("First Aid Only");
        reportList.add("Near Miss");

        List<String> reportMade = new ArrayList<String>();
        reportMade.add("Employee");
        reportMade.add("Supervisor");
        reportMade.add("Team");
        reportMade.add("Other");

        // Spinner Drop down elements
        List<String> injuryNature = new ArrayList<String>();
        injuryNature.add("Abrasion, scrapes");
        injuryNature.add("Amputation");
        injuryNature.add("Broken bone");
        injuryNature.add("Bruise");
        injuryNature.add("Burn(heat)");
        injuryNature.add("Burn(chemical)");
        injuryNature.add("Concussion(to the head)");
        injuryNature.add("Crushing Injury");
        injuryNature.add("Cut, Iaceration, puncture");
        injuryNature.add("Hernia");
        injuryNature.add("Illness");
        injuryNature.add("Sprain, strain");
        injuryNature.add("Damage to a body system");
        injuryNature.add("Other");

        // Spinner Drop down elements
        List<String> employeeWorks = new ArrayList<String>();
        employeeWorks.add("Regular full time");
        employeeWorks.add("Regular part time");
        employeeWorks.add("Seasonal");
        employeeWorks.add("Temporary");

        // Spinner Drop down elements
        List<String> workday = new ArrayList<String>();
        workday.add("Entering or leaving work");
        workday.add("Doing normal work activities");
        workday.add("During meal period");
        workday.add("During break");
        workday.add("Working overtime");
        workday.add("Other");



        // Creating adapter for spinner
        ArrayAdapter<String> reprotSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reportList);

        // Drop down layout style - list view with radio button
        reprotSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerReport.setAdapter(reprotSpinner);


        // Creating adapter for spinner
        ArrayAdapter<String> reportMadeSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, reportMade);
        reportMadeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportMade.setAdapter(reportMadeSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> injuryNatureSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, injuryNature);
        injuryNatureSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNature.setAdapter(injuryNatureSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> employeeWorksSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employeeWorks);
        employeeWorksSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneremployeeWorks.setAdapter(employeeWorksSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> workdaySpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, workday);
        workdaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerworkday.setAdapter(workdaySpinner);


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

        fabInjury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String report = sOne, date = dateIncidentET.getText().toString(), made_by = sTwo, name = nameET.getText().toString(),
                        department_id = departmentET.getText().toString(), sex = gender, age = ageET.getText().toString(),
                        job_title = jobTitleET.getText().toString(), injury = sThree, work_time = sFour, employe_month = monthsEmploeeET.getText().toString(),
                        employe_month_job = monthsJobET.getText().toString(), location = locationET.getText().toString(), employe_work = sFive,
                        time = timeET.getText().toString(), witness_name = witnessET.getText().toString();

                saveData(report, date, made_by, name, sex, age, department_id, job_title, location, time, work_time, witness_name, injury, employe_work, employe_month, employe_month_job);



                Snackbar.make(view, "Sending data to server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                    gender = "male";
                break;
            case R.id.radioFemale:
                if (checked)
                    // Ninjas rule
                    gender = "female";
                break;
            default:
                gender = "unisex";
                break;
        }
    }

    // sending response to server INJURY REPORT API
    public void saveData(final String report, final String date, final String made_by, final String name, final String sex, final String age, final String department_id, final String job_title
            , final String location, final String time, final String work_time, final String witness_name, final String injury, final String employe_work, final String employe_month, final String employe_month_job){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_INJURY_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("report", report);
                params.put("date", date);
                params.put("made_by", made_by);
                params.put("name", name);
                params.put("sex", sex);
                params.put("age", age);
                params.put("department_id", "1");
                params.put("job_title", job_title);
                params.put("location", location);
                params.put("time", time);
                params.put("work_time", work_time);
                params.put("witness_name", witness_name);
                params.put("injury", injury);
                params.put("employe_work", employe_work);
                params.put("employe_month", employe_month);
                params.put("employe_month_job", employe_month_job);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        switch(adapterView.getId()) {

            case R.id.spinnerReport:
                // On selecting a spinner item
                sOne = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.spinnerReportMade:
                // On selecting a spinner item
                sTwo = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.spinnerNature:
                sThree = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.spinneremployeeWorks:
                sFour = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.spinnerworkday:
                sFive = adapterView.getItemAtPosition(position).toString();
                break;
            default:
                sOne = adapterView.getItemAtPosition(position).toString();
                sTwo = adapterView.getItemAtPosition(position).toString();
                sThree = adapterView.getItemAtPosition(position).toString();
                sFour = adapterView.getItemAtPosition(position).toString();
                sFive = adapterView.getItemAtPosition(position).toString();

        }

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + sOne+" "+sTwo+" "+sThree+" "+sFour+" "+sFive, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
