package com.constructefile.democonstract.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.constructefile.democonstract.R;

import java.util.List;

import com.constructefile.democonstract.models.GetsHour;

/**
 * Created by Optimus Prime on 4/24/2017.
 */

public class GetHourAdapter extends RecyclerView.Adapter<GetHourAdapter.ViewHolder> {

    public Context mContext;
    public List<GetsHour> getList;

    public GetHourAdapter(Context mContext, List<GetsHour> getList) {
        this.mContext = mContext;
        this.getList = getList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_get_hour_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GetsHour getshour = getList.get(position);
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
        return getList.size();
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
}