package com.example.andersondev.tcc_novo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Temperature extends AppCompatActivity {

    LottieAnimationView animationView;
    final int FRAME_FIRST = 46;
    final int FRAME_LAST  = 82;

    final float speed_sun_to_moon = 1.0F;
    final float speed_moon_to_sun = -1.0F;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    Bluetooth bluetooth;
    public static final int MESSAGE_READ = 3;
    TextView temperatura;
    String valor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        animationView = findViewById(R.id.lav_sun_moon);
        temperatura = findViewById(R.id.temperatura);
        animationView.setMinAndMaxFrame(FRAME_FIRST,FRAME_LAST);
        updateByHour(getTime());

        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_READ){
                    String receiveds = (String) msg.obj;

                    int infoEnd = receiveds.indexOf("}");
                    if(infoEnd > 0){
                        String compData = receiveds.substring(0, infoEnd);
                        int infoSize = compData.length();

                        if(receiveds.charAt(0) == '{'){
                            String finalData = receiveds.substring(1, infoSize);
                            if(finalData.contains("C")){
                                temperatura.setText(finalData.replaceAll("(\\r|\\n)", ""));
                            }
                            Log.d("CHEGANDO", finalData);
                        }
                        Log.d("CHEGANDO  2", receiveds);
                        //btData.delete(0, btData.length());
                    }

                }
            }
        };
        bluetooth = new Bluetooth(getApplicationContext(),uuid,this,mHandler, 1);
        bluetooth.create();
    }

    private int getTime(){

        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    @Override
    public void onBackPressed() {
        bluetooth.closeConn();
        Intent it = new Intent(Temperature.this, MainActivity.class);
        startActivity(it);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluetooth.result(requestCode,resultCode,data);
    }
}
