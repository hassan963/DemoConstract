package com.constructefile.democonstract.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.constructefile.democonstract.R;

import java.util.ArrayList;
import java.util.List;

import com.constructefile.democonstract.models.GetsHour;

/**
 * Created by Optimus Prime on 4/24/2017.
 */

public class GetHourAdapter extends RecyclerView.Adapter<GetHourAdapter.ViewHolder>  implements Filterable {

    public Context mContext;
    public List<GetsHour> original_items= new ArrayList<>();
    public List<GetsHour> filtered_items= new ArrayList<>();
    ItemFilter mFilters = new ItemFilter();

    public GetHourAdapter(Context mContext, List<GetsHour> getList) {
        this.mContext = mContext;
        this.original_items = getList;
        this.filtered_items = getList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_get_hour_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GetsHour getshour = filtered_items.get(position);
        holder.date.setText(getshour.date);
        holder.supervisor.setText(getshour.supervisor);
        holder.remarks.setText(getshour.remarks);
        holder.name.setText(getshour.name);
        holder.updatedOn.setText(getshour.updatedOn);
        holder.job.setText(getshour.job);
        holder.clockOutTime.setText(getshour.clockOutTIme);
        holder.clockInTime.setText(getshour.clockInTime);
        holder.totalTime.setText(getshour.totalTime);
    }

    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public Filter getFilter() {
        return mFilters;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, name, job, clockInTime, clockOutTime, totalTime, supervisor, remarks, updatedOn;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            supervisor = (TextView) itemView.findViewById(R.id.supervisor);
            name = (TextView) itemView.findViewById(R.id.name);
            job = (TextView) itemView.findViewById(R.id.job);
            clockInTime = (TextView) itemView.findViewById(R.id.clockInTime);
            clockOutTime = (TextView) itemView.findViewById(R.id.clockOutTime);
            totalTime = (TextView) itemView.findViewById(R.id.totalTime);
            remarks = (TextView) itemView.findViewById(R.id.remarks);
            updatedOn = (TextView) itemView.findViewById(R.id.updatedOn);
        }
    }



    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String query = charSequence.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<GetsHour> list = original_items;
            final List<GetsHour> result_list = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                String str_title = list.get(i).date;
                if (str_title.toLowerCase().contains(query)) {
                    result_list.add(list.get(i));
                }
            }
            results.values = result_list;
            results.count = result_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            filtered_items = (List<GetsHour>) filterResults.values;
            notifyDataSetChanged();

        }
    }

}