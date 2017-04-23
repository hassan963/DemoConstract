package com.constructefile.democonstract.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.constructefile.democonstract.R;
import com.constructefile.democonstract.adapter.ExpandableListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HowToOHNSLaws.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HowToOHNSLaws#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HowToOHNSLaws extends Fragment {


    View rootView;
    ExpandableListView expListView;
    private String[] groups;
    private String[][] children;


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

        rootView = inflater.inflate(R.layout.fragment_howto, container, false);

        prepareListData();
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        expListView = (ExpandableListView) view.findViewById(R.id.expListView);
        expListView.setAdapter(new ExpandableListAdapter(getActivity(), groups, children));
        expListView.setGroupIndicator(null);
    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        groups = new String[]{"GeneralRequirements:", "Guards", "Seat requirements and rider restrictions",
                "Seat Belts", "Operating requirements", "Tire servicing", "All-terrain vehicles"};

        children = new String[][]{
                {"Application:\n" +
                        "Operation and maintenance:\n" +
                        "Competency of operators:\n" +
                        "Operator’s responsibility:\n" +
                        "Supervisor’s responsibility:\n" +
                        "Standards:\n" +
                        "Warning signal device-back up alarm:\n" +
                        "Warning signal device-extended component:\n" +
                        "Lights:\n" +
                        "Rear view mirrors:\n" +
                        "Window standards:\n" +
                        "Maintenance:\n" +
                        "Braking requirements:\n" +
                        "Supplementary steering:\n" +
                        "Steering wheel knobs:\n" +
                        "Safe starting:\n" +
                        "Escape from a cab:\n" +
                        "Controls:\n" +
                        "Load Handling attachments:\n"},
                {"Protective structures:\n" +
                        "Rollover protective structures:\n" +
                        "ROPS standards:\n" +
                        "ROPS certification:\n" +
                        "ROPS identification:\n" +
                        "Effect of ROPS on visibility:\n" +
                        "Shear hazards:\n" +
                        "Guarding moving parts:\n"},
                {"Operator’s seat:\n" +
                        "Restricted access:\n" +
                        "Rider restriction\n"},
                {"Provision:\n" +
                        "Use:\n"},
                {"Start of shift inspection:\n" +
                        "Securing tools and equipment:\n" +
                        "Unattended equipment:\n" +
                        "Securing elevated loads:\n" +
                        "Assistance on grades:\n" +
                        "Swinging equipment:\n" +
                        "Clearance:\n" +
                        "Boarding/leaving:\n" +
                        "Obstructed view:\n" +
                        "Pedestrian and equipment traffic:\n" +
                        "Securing loads:\n" +
                        "Restraint for cylindrical objects:\n" +
                        "Lift truck loads:\n"},
                {"Training:\n" +
                        "Equipment and procedures:\n"},
                {"Prohibited use:\n" +
                        "Modifications:\n" +
                        "Operator’s manual:\n" +
                        "Use on sloping ground:\n" +
                        "Operator training:\n" +
                        "Personal protective equipment:\n"}
        };
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
