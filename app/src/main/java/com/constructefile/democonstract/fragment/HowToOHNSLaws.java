package com.constructefile.democonstract.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.constructefile.democonstract.R;
import com.constructefile.democonstract.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HowToOHNSLaws.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HowToOHNSLaws#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HowToOHNSLaws extends Fragment {


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HowToOHNSLaws() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HowToOHNSLaws.
     */
    // TODO: Rename and change types and number of parameters
    public static HowToOHNSLaws newInstance(String param1, String param2) {
        HowToOHNSLaws fragment = new HowToOHNSLaws();
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

        View view = inflater.inflate(R.layout.fragment_how_to_ohnslaws, container, false);


        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                /*Toast.makeText(
                        getContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                return false;
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("General Requirements");
        listDataHeader.add("Guards");
        listDataHeader.add("Seat requirements and rider restrictions");
        listDataHeader.add("Seat Belts");
        listDataHeader.add("Operating requirements");
        listDataHeader.add("Tire servicing");
        listDataHeader.add("All-terrain vehicles");

        // Adding child data
        List<String> firstList = new ArrayList<String>();
        firstList.add("Application");
        firstList.add("Operation and maintenance");
        firstList.add("Competency of operators");
        firstList.add("Operator’s responsibility");
        firstList.add("Supervisor’s responsibility");
        firstList.add("Standards");
        firstList.add("Warning signal device-back up alarm");
        firstList.add("Warning signal device-extended component");
        firstList.add("Lights");
        firstList.add("Rear view mirrors");
        firstList.add("Window standards");
        firstList.add("Maintenance");
        firstList.add("Braking requirements");
        firstList.add("Supplementary steering");
        firstList.add("Steering wheel knobs");
        firstList.add("Safe starting");
        firstList.add("Escape from a cab");
        firstList.add("Controls");
        firstList.add("Load Handling attachments");

        List<String> secondList = new ArrayList<String>();
        secondList.add("Protective structures");
        secondList.add("Rollover protective structures");
        secondList.add("ROPS standards");
        secondList.add("ROPS certification");
        secondList.add("ROPS identification");
        secondList.add("Effect of ROPS on visibility");
        secondList.add("Shear hazards");
        secondList.add("Guarding moving parts");

        List<String> thirdList = new ArrayList<String>();
        thirdList.add("Operator’s seat");
        thirdList.add("Restricted access");
        thirdList.add("Rider restriction");


        List<String> thirdListDuplicate = new ArrayList<String>();
        thirdListDuplicate.add("Provision");
        thirdListDuplicate.add("Use");


        List<String> fourthList = new ArrayList<String>();
        fourthList.add("Start of shift inspection");
        fourthList.add("Securing tools and equipment");
        fourthList.add("Unattended equipment");
        fourthList.add("Securing elevated loads");
        fourthList.add("Assistance on grades");
        fourthList.add("Swinging equipment");
        fourthList.add("Clearance");
        fourthList.add("Boarding/leaving");
        fourthList.add("Obstructed view");
        fourthList.add("Pedestrian and equipment traffic");
        fourthList.add("Securing loads");
        fourthList.add("Restraint for cylindrical objects");
        fourthList.add("Lift truck loads");

        List<String> fifthList = new ArrayList<String>();
        fifthList.add("Training");
        fifthList.add("Equipment and procedures");


        List<String> sixthList = new ArrayList<String>();
        sixthList.add("Prohibited use");
        sixthList.add("Modifications");
        sixthList.add("Operator’s manual");
        sixthList.add("Use on sloping ground");
        sixthList.add("Operator training");
        sixthList.add("Personal protective equipment");


        listDataChild.put(listDataHeader.get(0), firstList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), secondList);
        listDataChild.put(listDataHeader.get(2), thirdList);
        listDataChild.put(listDataHeader.get(3), thirdListDuplicate); // Header, Child data
        listDataChild.put(listDataHeader.get(4), fourthList);
        listDataChild.put(listDataHeader.get(5), fifthList);
        listDataChild.put(listDataHeader.get(6), sixthList);
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
