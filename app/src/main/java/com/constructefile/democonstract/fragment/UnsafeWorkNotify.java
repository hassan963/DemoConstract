package com.constructefile.democonstract.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.app.AppConfig;
import com.constructefile.democonstract.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnsafeWorkNotify.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnsafeWorkNotify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnsafeWorkNotify extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinnerUnsafe;
    private EditText chatET;
    private Button chatSendBTN;
    private String superVisor;
    private List<String> superVisorList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UnsafeWorkNotify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnsafeWorkNotify.
     */
    // TODO: Rename and change types and number of parameters
    public static UnsafeWorkNotify newInstance(String param1, String param2) {
        UnsafeWorkNotify fragment = new UnsafeWorkNotify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unsafe_work_notify, container, false);

        spinnerUnsafe = (Spinner) view.findViewById(R.id.spinnerUnsafe);
        chatET = (EditText) view.findViewById(R.id.chatET);
        chatSendBTN = (Button) view.findViewById(R.id.chatSendBTN);

        getAllSuperVisor();

        superVisorList = new ArrayList<>();

        spinnerUnsafe.setOnItemSelectedListener(this);

        chatSendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(chatET.getText().toString());
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        superVisor = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // sending response to server NEAR MISS API
    public void saveData(final String text) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_INSERT_UNSAFE_WORK, new Response.Listener<String>() {
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
                params.put("operator_id", "9"); // will implement later
                params.put("supervisor_id", "9");  // not done yet
                params.put("message", text);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void getAllSuperVisor() {

        String tag_string_req = "req_get_all_vehicles";
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_GET_SUPERVISOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("all_vehicles", "Response: " + response.toString());
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        String first_name = jsonobject.getString("first_name");
                        String last_name = jsonobject.getString("last_name");
                        String id = jsonobject.getString("id");
                        //String email = jsonobject.getString("email");

                        superVisorList.add(id + " " + first_name + " " + last_name);

                    }

                    // Creating adapter for spinner
                    ArrayAdapter<String> data = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_text, superVisorList);
                    data.setDropDownViewResource(R.layout.spinner_drop_down);
                    spinnerUnsafe.setAdapter(data);

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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
