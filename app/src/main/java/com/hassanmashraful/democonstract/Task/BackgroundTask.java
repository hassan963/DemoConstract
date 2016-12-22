package com.hassanmashraful.democonstract.Task;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hassan M.Ashraful on 12/22/2016.
 */

public class BackgroundTask {

    Context context;
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();

    public BackgroundTask(Context context) {
        this.context = context;
    }

    public ArrayList<SpinnerData> getList() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, AppConfig.URL_TRUCK, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int count = 0;

                while (count < response.length()) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(count);
                        SpinnerData spinnerData = new SpinnerData(jsonObject.getString("model"), jsonObject.getString("serial_no"));
                        spinnerDatas.add(spinnerData);
                        count++;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

        return spinnerDatas;

    }


}
