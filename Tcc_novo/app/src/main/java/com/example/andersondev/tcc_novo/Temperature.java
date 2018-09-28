package com.example.andersondev.tcc_novo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;
import java.util.Date;

public class Temperature extends AppCompatActivity {

    LottieAnimationView animationView;
    final int FRAME_FIRST = 46;
    final int FRAME_LAST  = 82;

    final float speed_sun_to_moon = 1.0F;
    final float speed_moon_to_sun = -1.0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        animationView = findViewById(R.id.lav_sun_moon);
        animationView.setMinAndMaxFrame(FRAME_FIRST,FRAME_LAST);
        updateByHour(getTime());
    }

    private int getTime(){

        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }


    private void updateByHour(int hour){

        if(!canAnimate(hour))
            return;

        float speedValue = (isMorning(hour)) ? speed_moon_to_sun : speed_sun_to_moon;

        animationView.setSpeed(speedValue);
        animationView.playAnimation();
    }

    private boolean canAnimate(int hour){
        return (isMorning(hour) && animationView.getFrame() != FRAME_FIRST) || (!isMorning(hour) && animationView.getFrame() != FRAME_LAST);
    }

    private boolean isMorning(int hour){
        return (hour >= 6 && hour < 18);
    }
}
