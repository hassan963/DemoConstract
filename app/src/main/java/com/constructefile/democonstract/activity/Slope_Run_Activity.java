package com.constructefile.democonstract.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.constructefile.democonstract.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Hassan M.Ashraful on 4/25/2017.
 */

public class Slope_Run_Activity extends AppCompatActivity  {

    EditText riseET, runET;
    Button clearBTN, calculateBTN;
    TextView resultTV;
    ImageView slopeIMG;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slope_run, container, false);*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_slope_run);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        riseET = (EditText) findViewById(R.id.riseET);
        runET = (EditText) findViewById(R.id.runET);
        slopeIMG = (ImageView) findViewById(R.id.slopeIMG);

        resultTV = (TextView) findViewById(R.id.resultTV);

        clearBTN = (Button) findViewById(R.id.clearBTN);
        calculateBTN = (Button) findViewById(R.id.calculateBTN);

        calculateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double l = Double.parseDouble(riseET.getText().toString());
                double b = Double.parseDouble(runET.getText().toString());
                //(LengthxBasexHeight)+base/2(HeightxBase)xgrade%=

                //Calculation

                double output = (l/b)*100;
                resultTV.setText(String.valueOf(output));
            }
        });

        clearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                riseET.setText("");
                runET.setText("");

                resultTV.setText("");
            }
        });

        Picasso.with(getApplicationContext()).load(R.drawable.slope).into(slopeIMG);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
