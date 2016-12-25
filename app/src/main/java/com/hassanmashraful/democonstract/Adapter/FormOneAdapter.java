package com.hassanmashraful.democonstract.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hassanmashraful.democonstract.Content.FormData;
import com.hassanmashraful.democonstract.R;

import java.util.ArrayList;

/**
 * Created by Hassan M.Ashraful on 11/16/2016.
 */

public class FormOneAdapter extends RecyclerView.Adapter<FormOneAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<FormData> formDatas;



    public FormOneAdapter(ArrayList<FormData> formDatas, Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.formDatas = formDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_form_second_table, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, context, formDatas);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.firstTV.setText(formDatas.get(position).getTitle());
        //this.formDatas.get(position).setStatus(Boolean.valueOf(holder.checkbox_ok.toString()));



        holder.commentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                formDatas.get(position).setComment(s.toString());



            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v("%%%$$ "+position, formDatas.get(position).getComment());
            }
        });




    }

    @Override
    public int getItemCount() {
        return formDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView firstTV;
        CheckBox checkbox_ok;
        EditText commentET;
        ArrayList<FormData> formDataMyView;
        Context context;


        MyViewHolder(View itemView, Context context, ArrayList<FormData> formDatas) {
            super(itemView);
            this.context = context;
            this.formDataMyView = formDatas;

            firstTV = (TextView) itemView.findViewById(R.id.firstTV);
            checkbox_ok = (CheckBox) itemView.findViewById(R.id.checkbox_ok);
            commentET = (EditText) itemView.findViewById(R.id.commentET);


            checkbox_ok.setOnClickListener(this);
            //commentET.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            FormData formDatas = this.formDataMyView.get(position);
/*
            if (v.getId() == commentET.getId()) {
                //formDataMyView.setComment(commentET.getText().toString());
                //saveDATA(commentET.getText().toString(), false, position);
                Log.v(formDataMyView.getTitle(), formDataMyView.getComment() + "");

            }*/



            if (v.getId() == checkbox_ok.getId()) {
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    formDatas.setStatus(true);
                    //formDataMyView.setComment(commentET.getText().toString());
                    //saveDATA(commentET.getText().toString(), true, position);
                    Log.v(formDatas.getTitle(), formDatas.getStatus() + "");
                } else {
                    formDatas.setStatus(false);
                    //formDataMyView.setComment(commentET.getText().toString());
                    //saveDATA(commentET.getText().toString(), false, position);
                    Log.v(formDatas.getTitle(), formDatas.getStatus() + "");
                }

            }


        }


    }
}
