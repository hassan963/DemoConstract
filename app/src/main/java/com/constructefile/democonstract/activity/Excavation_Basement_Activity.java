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

public class Excavation_Basement_Activity extends AppCompatActivity {

    EditText widthBET, heightBET, lengthBET, widthEET, heightEET, lengthEET;
    Button clearBTN, calculateBTN;
    TextView resultTV;
    ImageView exIMG, basIMG;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slope, container, false);*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_excavationbase);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
        widthBET = (EditText) findViewById(R.id.widthBET);
        heightBET = (EditText) findViewById(R.id.heightBET);
        lengthBET = (EditText) findViewById(R.id.lengthBET);

        widthEET = (EditText) findViewById(R.id.widthEET);
        lengthEET = (EditText) findViewById(R.id.lengthEET);
        heightEET = (EditText) findViewById(R.id.heightEET);
        exIMG = (ImageView) findViewById(R.id.exIMG);
        basIMG = (ImageView) findViewById(R.id.basIMG);


        resultTV = (TextView) findViewById(R.id.resultTV);

        clearBTN = (Button) findViewById(R.id.clearBTN);
        calculateBTN = (Button) findViewById(R.id.calculateBTN);

        calculateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double l = Double.parseDouble(widthBET.getText().toString());
                double b = Double.parseDouble(heightBET.getText().toString());
                double h = Double.parseDouble(lengthBET.getText().toString());

                double g = Double.parseDouble(widthEET.getText().toString());   //(LengthxBasexHeight)+base/2(HeightxBase)xgrade%=
                double s = Double.parseDouble(lengthEET.getText().toString());
                double r = Double.parseDouble(heightEET.getText().toString());

                //(Basement WidthxBasement Heightx Basement Length)-(Excavation WidthxExcavation Heightx Excavation Length)

                //Calculation
                double i = l*b*h;
                double j = g*s*r;
                double output = i-j;
                resultTV.setText(String.valueOf(output));
            }
        });

        clearBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                widthBET.setText("");
                heightBET.setText("");
                lengthBET.setText("");
                widthEET.setText("");
                lengthEET.setText("");
                heightEET.setText("");
                resultTV.setText("");
            }
        });
        Picasso.with(getApplicationContext()).load(R.drawable.ex).into(exIMG);
        Picasso.with(getApplicationContext()).load(R.drawable.base).into(basIMG);

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
