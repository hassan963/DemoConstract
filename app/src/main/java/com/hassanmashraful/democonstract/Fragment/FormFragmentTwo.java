package com.hassanmashraful.democonstract.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.democonstract.Adapter.FormOneAdapter;
import com.hassanmashraful.democonstract.Content.FormData;
import com.hassanmashraful.democonstract.R;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hassan M.Ashraful on 12/23/2016.
 */

public class FormFragmentTwo extends Fragment {


    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private ArrayList<FormData> formDatas = new ArrayList<>();
    private FormOneAdapter formOneAdapter;

    String timestamp;

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

        formDatas.add(new FormData("Accelerator or Direction Control Pedal - Functioning Smoothly", 25));
        formDatas.add(new FormData("Service Brake - Functioning Smoothly", 26));
        formDatas.add(new FormData("Parking Brake - Functioning Smoothly", 27));
        formDatas.add(new FormData("Steering Operation - Functioning Smoothly", 28));
        formDatas.add(new FormData("Drive Control - Forward/Reverse - Functioning Smoothly", 29));


        return formDatas;
    }


    public void postDATA() {

        for (int i = 0; i < formDatas.size(); i++) {
            save(i);
        }

    }


    public void save(final int i) {


        pDialog.setMessage("Sending Data ...");
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
                String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
                String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                //int ampm= c.get(Calendar.AM_PM);

                String timestamp = date + " " + time;
                Log.i("time", date + " " + time);
               /* Map<String, String> params = new HashMap<String, String>();
                params.put("operator_id", String.valueOf(8));
                params.put("body", String.valueOf(checkoutContent.getText()));
                params.put("timestamp", timestamp);*/


                Map<String, String> params = new HashMap<String, String>();
                /*
                * String.valueOf(20)
                *  String.valueOf(8)
                *   String.valueOf(7)
                *   String.valueOf(formDatas.get(0).getStatus())
                *    formDatas.get(0).getComment()
                *
                * */
                params.put("id", "rrrr");
                params.put("checklist_item_id", String.valueOf(formDatas.get(i).getId()));
                params.put("truck_id", 3 + "");
                params.put("shift_id", 2 + "");
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

    public void updateList(String s) {
        Toast.makeText(getActivity(), "Got from two B-)!", Toast.LENGTH_SHORT).show();
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
