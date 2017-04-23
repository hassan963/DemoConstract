package com.constructefile.democonstract.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.constructefile.democonstract.activity.FuelRecord;
import com.constructefile.democonstract.content.CategoryData;
import com.constructefile.democonstract.MainActivity;
import com.constructefile.democonstract.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hassan M.Ashraful on 11/26/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<CategoryData> categoryDatas;
    private Context mcontext;

    public CategoryAdapter(Context context, ArrayList<CategoryData> categoryDatas) {
        this.categoryDatas = categoryDatas;
        this.mcontext = context;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, mcontext, categoryDatas);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        holder.textView.setText(categoryDatas.get(position).getrecyclerViewTitleText());
        Picasso.with(mcontext).load(categoryDatas.get(position).getrecyclerViewImage()).resize(240, 180).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView imageView;
        private Context context;
        private ArrayList<CategoryData> android;

        public ViewHolder(View v, Context context, ArrayList<CategoryData> android) {
            super(v);
            this.context = context;
            this.android = android;
            v.setOnClickListener(this);

            textView = (TextView) v.findViewById(R.id.text);
            imageView = (ImageView) v.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final CategoryData android = this.android.get(position);
            //Toast.makeText(context, "CLICKED: " + android.getrecyclerViewTitleText() + " " + position, Toast.LENGTH_SHORT).show();

            //show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

            builder.setTitle("Select Form Option");
            builder.setPositiveButton("Fuel Record", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, FuelRecord.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("CATEGORY", position + 1);
                            intent.putExtra("CATEGORY_NAME", android.getrecyclerViewTitleText());
                            context.startActivity(intent);
                        }
                    }
            );
            builder.setNegativeButton("Equipment Checklist", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            Toast.makeText(context, android.getrecyclerViewTitleText() + " " + position, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("CATEGORY", position + 1);
                            context.startActivity(intent);
                        }
                    }
            );
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //Toast.makeText(context, "CLICKED: " + android.getrecyclerViewTitleText(), Toast.LENGTH_SHORT).show();
        }
    }


}
