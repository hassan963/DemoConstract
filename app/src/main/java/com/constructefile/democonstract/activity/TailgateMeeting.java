package com.constructefile.democonstract.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TailgateMeeting extends AppCompatActivity {

    private File file;
    private Dialog dialog;
    private LinearLayout mContent;
    private View view;
    private TailgateMeeting.signature mSignature;
    private Bitmap bitmap;
    private SQLiteHandler db;
    private SessionManager session;
    // Creating Separate Directory for saving Generated Images
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/ConstructeFile/";
    private String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private String StoredPath = DIRECTORY + pic_name + ".JPG";
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MySign";

    TextView txt_conducted_by, txt_date, txt_safety_rep, txt_job, txt_contact, txt_phone, txt_master_point,
            txt_first_aid_loc, txt_first_aid_qualified, txt_permit_no, txt_hazard_assesment_no,
            txt_safety_topic, txt_discussion, txt_emergency_plan;
    Button btn_signature, btn_send, mClear, mGetSign, mCancel;
    String f_name, l_name, server_user_id, email, date_started;
    String tailgate_meeting_id, conducted_by, date, safety_rep, job, contact, phone, muster_point, first_aid_location,
            first_aid_qualified, permit_no, hazard_assessment_no, safety_topics, discussion, emergency_plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_meeting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        db = new SQLiteHandler(getApplicationContext());
        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();
        f_name = user.get("f_name");
        l_name = user.get("l_name");
        server_user_id = user.get("server_user_id");
        email = user.get("email");
        date_started = user.get("date_started");

        txt_conducted_by = (TextView) findViewById(R.id.conducted_by);
        txt_date = (TextView) findViewById(R.id.date);
        txt_safety_rep = (TextView) findViewById(R.id.safety_rep);
        txt_job = (TextView) findViewById(R.id.job);
        txt_contact = (TextView) findViewById(R.id.contact);
        txt_phone = (TextView) findViewById(R.id.phone);
        txt_master_point = (TextView) findViewById(R.id.master_point);
        txt_first_aid_loc = (TextView) findViewById(R.id.first_aid_loc);
        txt_first_aid_qualified = (TextView) findViewById(R.id.first_aid_qualified);
        txt_permit_no = (TextView) findViewById(R.id.permit_no);
        txt_hazard_assesment_no = (TextView) findViewById(R.id.hazard_assesment_no);
        txt_safety_topic = (TextView) findViewById(R.id.safety_topic);
        txt_discussion = (TextView) findViewById(R.id.discussion);
        txt_emergency_plan = (TextView) findViewById(R.id.emergency_plan);
        btn_signature = (Button) findViewById(R.id.btn_signature);
        btn_send = (Button) findViewById(R.id.btn_send);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        // Dialog Function
        dialog = new Dialog(TailgateMeeting.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sign_dialog_signature);
        dialog.setCancelable(true);
        Log.i("tailgate_api", AppConfig.GET_TODAY_TAILGATE_MEETING + server_user_id);
        getAllData();

        btn_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Function call for Digital Signature
                dialog_action();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sign = "";
                if (sharedpreferences.contains("signNear")) {
                    sign = sharedpreferences.getString("signNear", "");
                    Log.v("$$^%%^&%&^% SIGN", sign);
                }
                if (sign.equals("")) {
                    Snackbar.make(view, "Please sign the form", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    //sign
                    saveData(sign);
                    Snackbar.make(view, "Sending data to server..", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    sharedpreferences.edit().clear().apply();
                }
            }
        });
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

    private void getAllData() {

        // Toast.makeText(getApplicationContext(), AppConfig.URL_ALL_VEHICLES, Toast.LENGTH_SHORT).show();
        String tag_string_req = "req_get_all_data";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.GET_TODAY_TAILGATE_MEETING + server_user_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("all_hazard", "Response: " + response.toString());
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        tailgate_meeting_id = jsonobject.getString("id");
                        conducted_by = jsonobject.getString("conducted_by");
                        date = jsonobject.getString("date");
                        safety_rep = jsonobject.getString("safety_rep");
                        job = jsonobject.getString("job");
                        contact = jsonobject.getString("contact");
                        phone = jsonobject.getString("phone");
                        muster_point = jsonobject.getString("muster_point");
                        first_aid_location = jsonobject.getString("first_aid_location");
                        first_aid_qualified = jsonobject.getString("first_aid_qualified");
                        permit_no = jsonobject.getString("permit_no");
                        hazard_assessment_no = jsonobject.getString("hazard_assessment_no");
                        safety_topics = jsonobject.getString("safety_topics");
                        discussion = jsonobject.getString("discussion");
                        emergency_plan = jsonobject.getString("emergency_plan");
                        //Log.i("serial_equipment", equipment_server_id + " - " + name);
                        //Toast.makeText(LoginActivity.this, "1st One " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(LoginActivity.this, "labelInsert " + vehicle_type_id + " - " + vehicle_id + " - " + serial_no, Toast.LENGTH_SHORT).show();

                        txt_conducted_by.setText(conducted_by);
                        txt_date.setText(date);
                        txt_safety_rep.setText(safety_rep);
                        txt_job.setText(job);
                        txt_contact.setText(contact);
                        txt_phone.setText(phone);
                        txt_master_point.setText(muster_point);
                        txt_first_aid_loc.setText(first_aid_location);
                        txt_first_aid_qualified.setText(first_aid_qualified);
                        txt_permit_no.setText(permit_no);
                        txt_hazard_assesment_no.setText(hazard_assessment_no);
                        txt_safety_topic.setText(safety_topics);
                        txt_discussion.setText(discussion);
                        txt_emergency_plan.setText(emergency_plan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // sending response to server NEAR MISS API
    //final String sign
    public void saveData(final String sign) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPDATE_TODAY_TAILGATE_MEETING + tailgate_meeting_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TailgateMeeting.this, "Successfully Submitted Form To Web.", Toast.LENGTH_LONG).show();
                Log.i("update", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("conducted_by", conducted_by);
                params.put("date", date);
                params.put("safety_rep", safety_rep);
                params.put("job", job);
                params.put("contact", contact);
                params.put("phone", phone);
                params.put("muster_point", muster_point);
                params.put("first_aid_location", first_aid_location);
                params.put("first_aid_qualified", first_aid_qualified);
                params.put("permit_no", permit_no);
                params.put("hazard_assessment_no", hazard_assessment_no);
                params.put("safety_topics", safety_topics);
                params.put("discussion", discussion);
                params.put("emergency_plan", emergency_plan);
                params.put("signature", "");  // not done yet
                params.put("image", sign);
                params.put("operator_id", server_user_id);
                params.put("_METHOD", "PUT");
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

