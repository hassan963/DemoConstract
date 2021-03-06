package com.constructefile.democonstract.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.adapter.FormOneAdapter;
import com.constructefile.democonstract.content.FormData;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<FormData> formDatas;// = new ArrayList<>();
    private FormOneAdapter formOneAdapter;

    String timestamp;

    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        View rootView = inflater.inflate(R.layout.fragment_recycle_form, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleListView);

        formOneAdapter = new FormOneAdapter(getFormData(), getActivity());
        recyclerView.setAdapter(formOneAdapter);
        formOneAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        return rootView;
    }

    /*
    * static data for category recyclerview
    * */

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

    public void displayDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Checking Out? Leave A Message");
        builder.setMessage("By chosing this option you are going to leave a message to the authority and also CHECKOUT for today...");
        builder.setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            /*Send Checkout message
                            and
                            logout for that day*/

                        AlertDialog.Builder abuilder = new AlertDialog.Builder(getContext());

                        abuilder.setTitle("Checkout Message");
                        final EditText checkoutContent = new EditText(getContext());
                        abuilder.setView(checkoutContent);

                        abuilder.setPositiveButton("Send and Checkout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //check if user input is null
                                        if (checkoutContent != null) {

                                            //if input not null then insert whats typed
                                            Log.i("AppInfo", String.valueOf(checkoutContent.getText()));
                                            Toast.makeText(getContext(), String.valueOf(checkoutContent.getText()), Toast.LENGTH_SHORT).show();

                                            String tag_string_req = "req_check_message";
                                            //insertion
                                            StringRequest strReq = new StringRequest(Request.Method.POST,
                                                    AppConfig.URL_INSERT_CHECKOUT_MESSAGE, new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String response) {
                                                    Log.i("checkout_insert", "Response: " + response.toString());

                                                    try {
                                                        JSONObject jObj = new JSONObject(response);
                                                    } catch (JSONException e) {
                                                        // JSON error
                                                        e.printStackTrace();
                                                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {

                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.i("checkout_insert", "Insertion Error: " + error.getMessage());
                                                    Toast.makeText(getContext(),
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
                                                    params.put("operator_id", String.valueOf(8));
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

    public void updateList(String s) {
        Toast.makeText(getActivity(), "Got!", Toast.LENGTH_SHORT).show();
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
