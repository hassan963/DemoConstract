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

public class Trench_Pipe_Activity extends AppCompatActivity {

    EditText lengthET, baseET, heightET, radiusET;
    Button clearBTN, calculateBTN;
    TextView resultTV;
    ImageView trenchIMG;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slope, container, false);*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trench);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        lengthET = (EditText) findViewById(R.id.lengthET);
        baseET = (EditText) findViewById(R.id.baseET);
        heightET = (EditText) findViewById(R.id.heightET);
        radiusET = (EditText) findViewById(R.id.radiusET);
        trenchIMG = (ImageView) findViewById(R.id.trenchIMG);

        resultTV = (TextView) findViewById(R.id.resultTV);

        clearBTN = (Button) findViewById(R.id.clearBTN);
        calculateBTN = (Button) findViewById(R.id.calculateBTN);

        calculateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double l = Double.parseDouble(lengthET.getText().toString());
                double b = Double.parseDouble(baseET.getText().toString());
                double h = Double.parseDouble(heightET.getText().toString());
                double g = Double.parseDouble(radiusET.getText().toString());   //(LengthxBasexHeight)+base/2(HeightxBase)xgrade%=

                //(LenghtxBasexHeight)-(3.14xRadiusxLength)
                //Calculation
                double i = l * b * h;
                double j = 3.14 * g * l;
                double output = i - j;
                resultTV.setText(String.valueOf(output));
            }
        });

        clearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lengthET.setText("");
                heightET.setText("");
                baseET.setText("");
                radiusET.setText("");
                resultTV.setText("");
            }
        });

        Picasso.with(getApplicationContext()).load(R.drawable.tre).into(trenchIMG);

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
