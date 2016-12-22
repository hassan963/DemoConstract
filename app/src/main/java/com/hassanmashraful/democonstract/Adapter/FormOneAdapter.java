package com.hassanmashraful.democonstract.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hassanmashraful.democonstract.Content.FormData;
import com.hassanmashraful.democonstract.Fragment.FormFragment;
import com.hassanmashraful.democonstract.R;

import java.util.ArrayList;

/**
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormOneAdapter extends RecyclerView.Adapter<FormOneAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private FormFragment fragment;
    private ArrayList<FormData> formDatas;

    private ArrayList<FormData> operationDATA;

    String title, comment;
    Boolean status;

    public FormOneAdapter(ArrayList<FormData> formDatas, Context context, FormFragment fragment) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
        this.formDatas = formDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_form_second_table, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, context, formDatas);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.firstTV.setText(formDatas.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return formDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView firstTV;
        CheckBox checkbox_ok;
        EditText commentET;
        ArrayList<FormData> formDatas;
        Context context;


        MyViewHolder(View itemView, Context context, ArrayList<FormData> formDatas) {
            super(itemView);
            this.context = context;
            this.formDatas = formDatas;

            firstTV = (TextView) itemView.findViewById(R.id.firstTV);
            checkbox_ok = (CheckBox) itemView.findViewById(R.id.checkbox_ok);
            commentET = (EditText) itemView.findViewById(R.id.commentET);


            checkbox_ok.setOnClickListener(this);
            commentET.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            FormData formDatas = this.formDatas.get(position);

            if (v.getId() == commentET.getId()) {
                //formDatas.setComment(commentET.getText().toString());
                saveDATA(commentET.getText().toString(), false, position);
                Log.v(formDatas.getTitle(), formDatas.getComment() + "");

            }



            if (v.getId() == checkbox_ok.getId()) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    //formDatas.setStatus(true);
                    //formDatas.setComment(commentET.getText().toString());
                    saveDATA(commentET.getText().toString(), true, position);
                    Log.v(formDatas.getTitle(), formDatas.getStatus() + "");
                } else {
                    //formDatas.setStatus(false);
                    //formDatas.setComment(commentET.getText().toString());
                    saveDATA(commentET.getText().toString(), false, position);
                    Log.v(formDatas.getTitle(), formDatas.getStatus() + "");
                }

            }


        }


        public void saveDATA(String comment, Boolean status, int position) {

            this.formDatas.get(position).setComment(comment);
            this.formDatas.get(position).setStatus(status);
        }
    }
}
