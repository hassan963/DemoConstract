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
 * {@link HowToApproachingTasks.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HowToApproachingTasks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HowToApproachingTasks extends Fragment {


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

    public HowToApproachingTasks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HowToApproachingTasks.
     */
    // TODO: Rename and change types and number of parameters
    public static HowToApproachingTasks newInstance(String param1, String param2) {
        HowToApproachingTasks fragment = new HowToApproachingTasks();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_howto, container, false);

        // preparing list data
        prepareListData();

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

        groups = new String[]{"Digging a trench", "Keeping grade", "Loading Trucks"};

        children = new String[][]{
                {"Before digging a trench, mark out your area of digging. Then make your one call to make sure there are no cables or pipes underground in your area of digging.\n" +
                        "Allow enough room in your easement for the backfill material to be 1 meter away from your trench.Use the dirt management section to calculate how much dirt you will be removing to do better estimates on area needed for your easement.\n" +
                        "When digging with a surveyor, be aware and have good communication with them. They will be around you at all times to make sure you are at a correct depth and may leave to go check other areas of site.\n" +
                        "Keeping track of your depth on the stick of the machinery helps keeping a steady grade for beginners.\n"},
                {"When bringing dirt to grade, keep your bucket or blade full to always have material to work with. \n" +
                        "Having stakes in different areas to keep track of different grade helps.\n" +
                        "If digging area is soft and moves the stakes when approaching with machinery, using a line level you can move the stake to a different location while keeping the grade in the desired location until finished with equipment in area\n"},
                {"With Loader:\n" +
                        "Fill bucket and rise to loading area. Signal truck to advance to loading area, when in position signal to stop. Fill truck and signal when finished.\n" +
                        "\nWith Excavator:\n" +
                        "Signal truck to advance to loading area. Fill bucket and swing over the back end of the truck.(never go over the cab of the truck) When finished loading signal truck.\n"}
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

    /*public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] groups;
        private String[][] children;

        public ExpandableListAdapter(String[] groups, String[][] children) {
            this.groups = groups;
            this.children = children;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.lblListItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getChild(groupPosition, childPosition).toString());

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_group, parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.lblListHeader);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getGroup(groupPosition).toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private class ViewHolder {
            TextView text;
        }

    }*/

}
