package com.constructefile.democonstract.activity;

/**
 * Created by Hassan M.Ashraful on 3/11/2017.
 */

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
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class NearMiss_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private Button btn_get_sign, mClear, mGetSign, mCancel;

    private TextView dateNear;
    private Spinner typeNear, typeConcern;
    private EditText eventET, jobET;

    private Button sendBTN;

    private String sOne, sTwo, timestamp, date;

    private File file;
    private Dialog dialog;
    private LinearLayout mContent;
    private View view;
    private signature mSignature;
    private Bitmap bitmap;

    // Creating Separate Directory for saving Generated Images
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/ConstructeFile/";
    private String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private String StoredPath = DIRECTORY + pic_name + ".JPG";

    private SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MySign" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearmiss);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        typeNear = (Spinner) findViewById(R.id.typeNear);
        typeConcern = (Spinner) findViewById(R.id.typeConcern);
        sendBTN = (Button) findViewById(R.id.sendBTN);
        jobET = (EditText) findViewById(R.id.jobET);
        eventET = (EditText) findViewById(R.id.eventET);
        dateNear = (TextView) findViewById(R.id.dateNear);

        typeNear.setOnItemSelectedListener(this);
        typeConcern.setOnItemSelectedListener(this);

        List<String> typeOfMiss = new ArrayList<String>();
        typeOfMiss.add("Near Miss");
        typeOfMiss.add("Safety Concern");
        typeOfMiss.add("Safety Suggestion");
        typeOfMiss.add("Other");

        List<String> typeOfConcern = new ArrayList<String>();
        typeOfConcern.add("Unsafe act");
        typeOfConcern.add("Unsafe Equipment");
        typeOfConcern.add("Unsafe use of equipment");
        typeOfConcern.add("Unsafe condition");
        typeOfConcern.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfMiss);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_drop_down);

        // attaching data adapter to spinner
        typeNear.setAdapter(dataAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> data = new ArrayAdapter<String>(this, R.layout.spinner_item_text, typeOfConcern);
        data.setDropDownViewResource(R.layout.spinner_drop_down);
        typeConcern.setAdapter(data);

        // Setting ToolBar as ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back); // just setNavigationIcon
        toolbar.setBackgroundColor(Color.parseColor("#e1660f"));
        //this.navigationBack();

        // Button to open signature panel
        btn_get_sign = (Button) findViewById(R.id.signature);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        // Dialog Function
        dialog = new Dialog(NearMiss_Activity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sign_dialog_signature);
        dialog.setCancelable(true);

        getDateTime(); dateNear.setText(date);


        btn_get_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Function call for Digital Signature
                dialog_action();

            }
        });

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sign = "";

                if (sharedpreferences.contains("signNear")) {
                    sign = sharedpreferences.getString("signNear", "");
                    Log.v("$$^%%^&%&^% SIGN", sign);
                }

                if (sign.equals("")){
                    Snackbar.make(view, "Please sign the form", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else {
                    saveData(jobET.getText().toString(), sOne, sTwo, eventET.getText().toString(), sign);

                    Snackbar.make(view, "Sending data to server..", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    jobET.setText("");
                    eventET.setText("");
                    sharedpreferences.edit().clear().apply();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        switch(adapterView.getId()) {

            case R.id.typeNear:
                // On selecting a spinner item
                sOne = adapterView.getItemAtPosition(position).toString();
                break;

            case R.id.typeConcern:
                // On selecting a spinner item
                sTwo = adapterView.getItemAtPosition(position).toString();
                break;

            default:
                sOne = adapterView.getItemAtPosition(position).toString();
                sTwo = adapterView.getItemAtPosition(position).toString();
                break;

        }
        Toast.makeText(getApplicationContext(), sOne+sTwo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getDateTime(){
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
        date = c.get(Calendar.YEAR) + "-" + month + "-" + c.get(Calendar.DATE);
        String time = hr + ":" + min + ":" + sec;

        timestamp = date + " " + time;
    }

    // sending response to server NEAR MISS API
    public void saveData(final String job, final String nearmiss_type, final String concern_type, final String description, final String sign){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_NEAR_MISS, new Response.Listener<String>() {
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

                params.put("job", job);
                params.put("nearmiss_type", nearmiss_type);
                params.put("concern_type", concern_type);
                params.put("date", timestamp);
                params.put("description", description);
                params.put("operator_id", "9"); // will implement later
                params.put("signature", sign);  // not done yet
                params.put("image", sign);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

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

                editor.putString("signNear", signString);
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
