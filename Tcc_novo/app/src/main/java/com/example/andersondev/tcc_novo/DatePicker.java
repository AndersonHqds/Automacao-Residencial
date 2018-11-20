package com.example.andersondev.tcc_novo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;
import java.util.Date;

public class DatePicker extends AppCompatActivity {

    Button select;
    Calendar calendar;
    DatePickerDialog dpd;
    LottieAnimationView animationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datepicker);

        select = (Button)findViewById(R.id.btnSelect);
        animationView = findViewById(R.id.chart);


        animationView.setSpeed(0.5F);
        animationView.playAnimation();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(DatePicker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int nYear, int nDonth, int nDay) {

                    }
                }, day,month,year);
                dpd.show();
            }
        });
    }
}
