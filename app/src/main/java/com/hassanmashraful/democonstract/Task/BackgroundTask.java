package com.hassanmashraful.democonstract.Task;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.democonstract.Content.SpinnerData;
import com.hassanmashraful.democonstract.app.AppConfig;
import com.hassanmashraful.democonstract.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hassan M.Ashraful on 12/24/2016.
 */

public class BackgroundTask {


    Context context;
    int pressBTN;
    ArrayList<SpinnerData> spinnerDatas = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    List<String> vehicle_json_id = new ArrayList<>();

    public BackgroundTask(Context context, int pressBTN) {
        this.context = context;
        this.pressBTN = pressBTN;
    }

    public List<String> getList() {
        Toast.makeText(context, AppConfig.URL_TRUCK + pressBTN, Toast.LENGTH_SHORT).show();
        String tag_string_req = "req_category_base_serial_model";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_TRUCK + pressBTN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("serial_model", "Response: " + response.toString());
                //int count = 0;
                //while (count < response.length()) {
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        String vehicle_id = jsonobject.getString("id");
                        String model = jsonobject.getString("model");
                        String serial_no = jsonobject.getString("serial_no");
                        SpinnerData spinnerData = new SpinnerData(vehicle_id, model, serial_no);
                        spinnerDatas.add(spinnerData);
                        strings.add(serial_no);
                        vehicle_json_id.add(vehicle_id);
                        Log.i("model_serial", vehicle_id + " " + serial_no + " " + model);
                    }
                        /*JSONObject jsonObject = response.getJSONObject(count);
                        SpinnerData spinnerData = new SpinnerData(jsonObject.getString("model"), jsonObject.getString("serial_no"));
                        spinnerDatas.add(spinnerData);
                        count++;*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return strings;

    }


}
