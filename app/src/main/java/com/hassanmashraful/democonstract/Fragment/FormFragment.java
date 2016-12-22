package com.hassanmashraful.democonstract.Fragment;

import android.app.AlertDialog;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hassanmashraful.democonstract.Activity.LoginActivity;
import com.hassanmashraful.democonstract.Adapter.FormOneAdapter;
import com.hassanmashraful.democonstract.Content.FormData;
import com.hassanmashraful.democonstract.MainActivity;
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
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<FormData> formDatas = new ArrayList<>();
    private FormOneAdapter formOneAdapter;

    String timestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycle_form, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleListView);

        formOneAdapter = new FormOneAdapter(getFormData(), getActivity(), FormFragment.this);
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
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));
        formDatas.add(new FormData("Safety........."));
        formDatas.add(new FormData("Leaks........."));
        formDatas.add(new FormData("Tires........."));

        return formDatas;
    }


    public void save() {

        String tag_string_req = "req_operational_check";
        //insertion
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_OPERATIONAL_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                params.put("checklist_item_id", 1 + "");
                params.put("truck_id", 3 + "");
                params.put("shift_id", 2 + "");
                params.put("status", 1 + "");
                params.put("maintenance", "For checking Only");
                params.put("timestamp", timestamp);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //end of insertion


        //String tag_string_req = "req_operational_check";

        //if input not null then insert whats typed


        //String tag_string_req = "req_check_message";
        //insertion


      /*
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_INSERT_OPERATIONAL_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("checkout_insert", "Response: " + response.toString());

                try {
                    JSONArray jObj = new JSONArray(response);

                    // Now store the user in SQLite
                    //String id = jObj.getString("id");

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("checkout_insert", "Insertion Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // Posting parameters to insert_check_message url
                Calendar c = Calendar.getInstance();
                String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
                String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                //int ampm= c.get(Calendar.AM_PM);

                String timestamp = date + " " + time;
                Log.i("time", date + " " + time);
                Map<String, String> params = new HashMap<String, String>();
                params.put("checklist_item_id", "9");
                params.put("truck_id", "8");
                params.put("shift_id", "7");
                params.put("status", String.valueOf(formDatas.get(0).getStatus()));
                params.put("maintenance", formDatas.get(0).getComment());
                params.put("timestamp", timestamp);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        Log.e("SAVING", formDatas.get(0).getComment());

        //end of insertion

    */


        /*

        Calendar c = Calendar.getInstance();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
        String time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        timestamp = date + " " + time;

        Volley.newRequestQueue(getContext()).add(
                new JsonRequest<JSONArray>(Request.Method.POST, AppConfig.URL_INSERT_OPERATIONAL_CHECK, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("checklist_item_id", String.valueOf(9));
                        params.put("truck_id", String.valueOf(8));
                        params.put("shift_id", String.valueOf(7));
                        params.put("status", String.valueOf(formDatas.get(0).getStatus()));
                        params.put("maintenance", formDatas.get(0).getComment());
                        params.put("timestamp", timestamp);
                        return params;
                    }

                    @Override
                    protected Response<JSONArray> parseNetworkResponse(
                            NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data,
                                    HttpHeaderParser
                                            .parseCharset(response.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                });
                */


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

                                                        // Now store the user in SQLite
                                                         /*   String id = jObj.getString("id");

                                                        if (id != null) {
                                                            Toast.makeText(CategoryActivity.this, "Checkout Successfully.", Toast.LENGTH_SHORT).show();
                                                            logoutUser();
                                                        } else {
                                                            Toast.makeText(CategoryActivity.this, "Unexpected Error! Try again later.", Toast.LENGTH_SHORT).show();
                                                        }
                                                        finish();*/
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


    public void testCODE() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        //params.put("operator_id", String.valueOf(8));
        // params.put("body", String.valueOf(checkoutContent.getText()));
        // params.put("timestamp", timestamp);


        params.put("checklist_item_id", String.valueOf(20));
        params.put("truck_id", String.valueOf(8));
        params.put("shift_id", String.valueOf(7));
        params.put("status", String.valueOf(formDatas.get(0).getStatus()));
        params.put("maintenance", formDatas.get(0).getComment());
        params.put("timestamp", timestamp);

        // CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, AppConfig.URL_INSERT_OPERATIONAL_CHECK, params, this.createRequestSuccessListener(), this.createRequestErrorListener());

        //requestQueue.add(jsObjRequest);
    }


}
