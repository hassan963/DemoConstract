package com.constructefile.democonstract.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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


    private File file;
    private Dialog dialog;
    private LinearLayout mContent;
    private View view;
    private signature mSignature;
    private Bitmap bitmap;

    private Toolbar toolbar;
    private Button signatureInjury, mClear, mGetSign, mCancel;
    private RadioGroup radioSexGroup;

    // Creating Separate Directory for saving Generated Images
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/ConstructeFile/";
    private String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private String StoredPath = DIRECTORY + pic_name + ".JPG";

    private SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MySign" ;

    private String sOne = "", sTwo = "", sThree = "", sFour = "", sFive = "", gender="male";
    EditText dateIncidentET, nameET, departmentET, ageET, jobTitleET, monthsEmploeeET, monthsJobET, locationET, timeET, witnessET;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injury_report);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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

        radioSexGroup = (RadioGroup)findViewById(R.id.radioSex);


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
        ArrayAdapter<String> reprotSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_text, reportList);

        // Drop down layout style - list view with radio button
        reprotSpinner.setDropDownViewResource(R.layout.spinner_drop_down);

        // attaching data adapter to spinner
        spinnerReport.setAdapter(reprotSpinner);


        // Creating adapter for spinner
        ArrayAdapter<String> reportMadeSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_text, reportMade);
        reportMadeSpinner.setDropDownViewResource(R.layout.spinner_drop_down);
        spinnerReportMade.setAdapter(reportMadeSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> injuryNatureSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_text, injuryNature);
        injuryNatureSpinner.setDropDownViewResource(R.layout.spinner_drop_down);
        spinnerNature.setAdapter(injuryNatureSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> employeeWorksSpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_text, employeeWorks);
        employeeWorksSpinner.setDropDownViewResource(R.layout.spinner_drop_down);
        spinneremployeeWorks.setAdapter(employeeWorksSpinner);

        // Creating adapter for spinner
        ArrayAdapter<String> workdaySpinner = new ArrayAdapter<String>(this, R.layout.spinner_item_text, workday);
        workdaySpinner.setDropDownViewResource(R.layout.spinner_drop_down);
        spinnerworkday.setAdapter(workdaySpinner);

        // Setting ToolBar as ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setBackgroundColor(Color.parseColor("#e1660f"));

        // Button to open signature panel
        signatureInjury = (Button) findViewById(R.id.signatureInjury);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        // Dialog Function
        dialog = new Dialog(Injury_Report_Activity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sign_dialog_signature);
        dialog.setCancelable(true);

        signatureInjury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Function call for Digital Signature
                dialog_action();

            }
        });


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

                //String report = sOne;
                String date = dateIncidentET.getText().toString();
                //String made_by = sTwo;
                String name = nameET.getText().toString();
                String department_id = departmentET.getText().toString();
                String  age = ageET.getText().toString();
                String job_title = jobTitleET.getText().toString();
                //String  injury = sThree; String work_time = sFour;
                String employe_month = monthsEmploeeET.getText().toString();
                String employe_month_job = monthsJobET.getText().toString(); String location = locationET.getText().toString(); //String employe_work = sFive;
                String time = timeET.getText().toString(); String witness_name = witnessET.getText().toString();

                String sign = "";

                if (sharedpreferences.contains("signInjury")) {
                    sign = sharedpreferences.getString("signInjury", "");
                    Log.v("$$^%%^&%&^% SIGN", sign);

                    String sex = sharedpreferences.getString("sex", "");
                    String report = sharedpreferences.getString("sONE","");
                    String made_by = sharedpreferences.getString("sTWO","");
                    String injury = sharedpreferences.getString("sTHREE","");
                    String work_time = sharedpreferences.getString("sFOUR","");
                    String employe_work = sharedpreferences.getString("sFIVE","");

                            saveData(report, date, made_by, name, age, sex, department_id, job_title, location, time, work_time, witness_name, injury, employe_work, employe_month, employe_month_job, sign);

                    Snackbar.make(view, "Sending data to server..", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    dateIncidentET.setText(""); dateIncidentET.setText(""); nameET.setText(""); departmentET.setText(""); ageET.setText(""); jobTitleET.setText(""); monthsEmploeeET.setText("");
                    monthsJobET.setText(""); locationET.setText("");
                    sharedpreferences.edit().clear().apply(); timeET.setText(""); witnessET.setText("");

                }

                if (sign.equals("")){
                    Snackbar.make(view, "Please sign the form", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
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

        SharedPreferences.Editor editor = sharedpreferences.edit();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    // Pirates are the best
                editor.putString("sex", "male");
                editor.apply();
                break;
            case R.id.radioFemale:
                if (checked)
                    // Ninjas rule
                 editor.putString("sex", "female");
                editor.apply();
                break;
            default:
                editor.putString("sex", "unisex");
                editor.apply();
                break;
        }
    }

    // sending response to server INJURY REPORT API
    public void saveData(final String report, final String date, final String made_by, final String name, final String age, final String sex, final String department_id, final String job_title
            , final String location, final String time, final String work_time, final String witness_name, final String injury, final String employe_work, final String employe_month, final String employe_month_job, final String sign){

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
                params.put("image", sign);
                return checkParams(params);
            }

            private Map<String, String> checkParams(Map<String, String> map){
                for (Map.Entry<String, String> pairs : map.entrySet()) {
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        SharedPreferences.Editor editor = sharedpreferences.edit();

        switch(adapterView.getId()) {

            case R.id.spinnerReport:
                // On selecting a spinner item
                editor.putString("sONE", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;

            case R.id.spinnerReportMade:
                // On selecting a spinner item
                editor.putString("sTWO", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;

            case R.id.spinnerNature:
                editor.putString("sTHREE", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;

            case R.id.spinneremployeeWorks:
                editor.putString("sFOUR", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;

            case R.id.spinnerworkday:
                editor.putString("sFIVE", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;
            default:
                editor.putString("sONE", adapterView.getItemAtPosition(position).toString());
                editor.putString("sTWO", adapterView.getItemAtPosition(position).toString());
                editor.putString("sTHREE", adapterView.getItemAtPosition(position).toString());
                editor.putString("sFOUR", adapterView.getItemAtPosition(position).toString());
                editor.putString("sFIVE", adapterView.getItemAtPosition(position).toString());
                editor.apply();
                break;

        }

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + sOne+" "+sTwo+" "+sThree+" "+sFour+" "+sFive, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    // Function for Digital Signature
    public void dialog_action() {

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                // Calling the same class
                recreate();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                // Calling the same class
                recreate();
            }
        });
        dialog.show();
    }


    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);


                // Convert the output file to Image such as .jpeg
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgBytes = byteArrayOutputStream.toByteArray();
                String signString = Base64.encodeToString(imgBytes, Base64.DEFAULT);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("signInjury", signString);
                editor.apply();

                // Convert the output file to Image such as .jpeg
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }


}
