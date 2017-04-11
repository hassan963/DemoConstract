package com.constructefile.democonstract.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.constructefile.democonstract.R;
import com.constructefile.democonstract.models.Gets;

import java.util.List;

/**
 * Created by Optimus Prime on 4/12/2017.
 */

public class GetAdapter extends RecyclerView.Adapter<GetAdapter.ViewHolder> {

    public Context mContext;
    public List<Gets> getList;

    public GetAdapter(Context mContext, List<Gets> getList) {
        this.mContext = mContext;
        this.getList = getList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_get_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Gets gets = getList.get(position);
        holder.date.setText(gets.date);
        holder.supervisor.setText(gets.supervisor_id);
        holder.desc.setText(gets.description);
    }

    @Override
    public int getItemCount() {
        return getList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, supervisor, desc;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            supervisor = (TextView) itemView.findViewById(R.id.supervisor);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }
    }
}