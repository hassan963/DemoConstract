package com.constructefile.democonstract.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.constructefile.democonstract.R;

import java.util.ArrayList;
import java.util.List;

public class Converter extends AppCompatActivity implements View.OnClickListener {
    EditText input;
    Spinner unitSpinner;
    TextView output;
    Button btn_km, btn_miles, btn_celcius, btn_fahrenheit, btn_kg, btn_pounds, btn_stones, btn_meters,
            btn_feet, btn_inch, btn_cm, btn_mm, btn_mph, btn_kph;
    int selectedUnit = 0;
    double inputNumberValueInDouble = 0.00, outputNumberValueInDouble = 0.00;
    String inputNumberValue = "";

    Button inputKmToMBtn, inputMToKmBtn, inputCelToFahrBtn, inputFarToCelBtn, inputKgToPoundBtn, inputPoundToKgBtn,
            inputKgToStoneBtn, inputStoneToKgBtn, inputMeterToFeetBtn, inputFeetToMeterBtn,
            inputInchToCmBtn, inputCmToInchBtn, inputMmToInchBtn, inputInchToMmBtn, inputInchToftBtn,
            inputFtToInchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        input = (EditText) findViewById(R.id.input);
        output = (TextView) findViewById(R.id.output);


        inputKmToMBtn = (Button) findViewById(R.id.inputKmToMBtn);
        inputMToKmBtn = (Button) findViewById(R.id.inputMToKmBtn);
        inputCelToFahrBtn = (Button) findViewById(R.id.inputCelToFahrBtn);
        inputFarToCelBtn = (Button) findViewById(R.id.inputFarToCelBtn);
        inputKgToPoundBtn = (Button) findViewById(R.id.inputKgToPoundBtn);
        inputPoundToKgBtn = (Button) findViewById(R.id.inputPoundToKgBtn);
        inputKgToStoneBtn = (Button) findViewById(R.id.inputKgToStoneBtn);
        inputStoneToKgBtn = (Button) findViewById(R.id.inputStoneToKgBtn);
        inputMeterToFeetBtn = (Button) findViewById(R.id.inputMeterToFeetBtn);
        inputFeetToMeterBtn = (Button) findViewById(R.id.inputFeetToMeterBtn);
        inputInchToCmBtn = (Button) findViewById(R.id.inputInchToCmBtn);
        inputCmToInchBtn = (Button) findViewById(R.id.inputCmToInchBtn);
        inputMmToInchBtn = (Button) findViewById(R.id.inputMmToInchBtn);
        inputInchToMmBtn = (Button) findViewById(R.id.inputInchToMmBtn);
        inputInchToftBtn = (Button) findViewById(R.id.inputInchToftBtn);
        inputFtToInchBtn = (Button) findViewById(R.id.inputFtToInchBtn);

        inputKmToMBtn.setOnClickListener(this);
        inputMToKmBtn.setOnClickListener(this);
        inputCelToFahrBtn.setOnClickListener(this);
        inputFarToCelBtn.setOnClickListener(this);
        inputKgToPoundBtn.setOnClickListener(this);
        inputPoundToKgBtn.setOnClickListener(this);
        inputKgToStoneBtn.setOnClickListener(this);
        inputStoneToKgBtn.setOnClickListener(this);
        inputMeterToFeetBtn.setOnClickListener(this);
        inputFeetToMeterBtn.setOnClickListener(this);
        inputInchToCmBtn.setOnClickListener(this);
        inputCmToInchBtn.setOnClickListener(this);
        inputMmToInchBtn.setOnClickListener(this);
        inputInchToMmBtn.setOnClickListener(this);
        inputInchToftBtn.setOnClickListener(this);
        inputFtToInchBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        try {
            inputNumberValue = input.getText().toString(); // Same
            inputNumberValueInDouble = Double.parseDouble(inputNumberValue); // Make use of autoboxing.  It's also easier to read.
        } catch (Exception e) {
            // p did not contain a valid double
        }

        switch (v.getId()) {

            case R.id.inputCelToFahrBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 33.8;
                output.setText(outputNumberValueInDouble + " F");
                break;

            case R.id.inputCmToInchBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.393701;
                output.setText(outputNumberValueInDouble + " inch");
                break;

            case R.id.inputFarToCelBtn:
                outputNumberValueInDouble = ((inputNumberValueInDouble - 32) * 5) / 9;
                output.setText(outputNumberValueInDouble + " Degree Celcius");
                break;

            case R.id.inputFeetToMeterBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.3048;
                output.setText(outputNumberValueInDouble + " Meter");
                break;

            case R.id.inputStoneToKgBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 6.35029;
                output.setText(outputNumberValueInDouble + " KG");
                break;

            case R.id.inputPoundToKgBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.453592;
                output.setText(outputNumberValueInDouble + " KG");
                break;

            case R.id.inputMToKmBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 1.60934;
                output.setText(outputNumberValueInDouble + " KM");
                break;

            case R.id.inputMmToInchBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.0393701;
                output.setText(outputNumberValueInDouble + " inch");
                break;

            case R.id.inputMeterToFeetBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 3.28084;
                output.setText(outputNumberValueInDouble + " ft");
                break;

            case R.id.inputKmToMBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.621371;
                output.setText(outputNumberValueInDouble + " Mile");
                break;

            case R.id.inputKgToStoneBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.157473;
                output.setText(outputNumberValueInDouble + " Stone");
                break;

            case R.id.inputKgToPoundBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 2.20462;
                output.setText(outputNumberValueInDouble + " Lbs");
                break;

            case R.id.inputInchToMmBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 25.4;
                output.setText(outputNumberValueInDouble + " mm");
                break;

            case R.id.inputInchToftBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 0.0833333;
                output.setText(outputNumberValueInDouble + " ft");
                break;

            case R.id.inputInchToCmBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 2.54;
                output.setText(outputNumberValueInDouble + " cm");
                break;

            case R.id.inputFtToInchBtn:
                outputNumberValueInDouble = inputNumberValueInDouble * 12;
                output.setText(outputNumberValueInDouble + " inch");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
