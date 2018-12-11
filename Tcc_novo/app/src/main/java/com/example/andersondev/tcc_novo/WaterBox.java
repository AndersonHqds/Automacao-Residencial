package com.example.andersondev.tcc_novo;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.UUID;

public class WaterBox extends AppCompatActivity {

    LottieAnimationView animationView;
    Bluetooth bluetooth;
    final float speed_moon_to_sun = -1.0F;
    float total;
    float size = 0;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    public static final int MESSAGE_READ = 3;
    TextView txtNivel, txtStatus;
    int lastNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_box);

        txtNivel = (TextView)findViewById(R.id.txtNivel);
        txtStatus = (TextView)findViewById(R.id.txtStatus);

        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_READ){

                    String receiveds = (String) msg.obj;
                    int total = 20;

                    int infoEnd = receiveds.indexOf("}");
                    if(infoEnd > 0){
                        String compData = receiveds.substring(0, infoEnd);
                        int infoSize = compData.length();

                        if(receiveds.charAt(0) == '{'){
                            String finalData = receiveds.substring(1, infoSize);
                            Double number = Double.parseDouble(finalData.toString().replaceAll("\\D+", ""));

                            if(number > 14 && number < 1000){
                                number = 14.0;
                            }

                            if(number > 1000){
                                number = 0.0;
                            }
                            int quantidade = (int)(100 - ( number / 14) * 100);
                            txtNivel.setText( quantidade + "%");

                            Log.d("LastNumber", String.valueOf(lastNumber));
                            Log.d("quantidade", String.valueOf(quantidade));
                        }
                        //Log.d("CHEGANDO  2", receiveds);
                        //btData.delete(0, btData.length());
                    }

                }
            }
        };
        bluetooth = new Bluetooth(getApplicationContext(),uuid,this,mHandler, 2);
        bluetooth.create();
        animationView = findViewById(R.id.water_loader);
        //loadBox();


        animationView.addAnimatorListener(new Animator.AnimatorListener(){
            float speed = 0.1F;
            int max = Integer.parseInt(txtNivel.getText().toString().replaceAll("\\D+", ""));

            int min = 20;
            int maxPercent = (30 * max) / 100 + min;
            int minPercent = maxPercent - 2;
            @Override
            public void onAnimationStart(Animator animation) {
                animationView.setSpeed(0.3F);
                changeStatus(max);

                if(max <= 25){
                    maxPercent = 30;
                    minPercent = maxPercent - 2;
                }
                else{
                    maxPercent = (30 * max) / 100 + min;
                    minPercent = maxPercent - 2;
                }

                animationView.setMinAndMaxFrame(0,maxPercent);
                //animationView.setMinAndMaxFrame(39,40);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                max = Integer.parseInt(txtNivel.getText().toString().replaceAll("\\D+", ""));
                changeStatus(max);

                if(max <= 25){
                    maxPercent = 30;
                    minPercent = maxPercent - 2;
                }
                else{
                    maxPercent = (30 * max) / 100 + min;
                    minPercent = maxPercent - 2;
                }

                animationView.setMinAndMaxFrame(minPercent, maxPercent);
                if(speed == 0.1F){
                    animationView.setSpeed(-0.1F);
                    speed = -0.1F;
                }
                else {
                    animationView.setSpeed(0.1F);
                    speed = 0.1F;
                }
            }

        });
        animationView.playAnimation();
    }

    @Override
    public void onBackPressed() {
        bluetooth.closeConn();
        Intent it = new Intent(WaterBox.this, MainActivity.class);
        startActivity(it);
    }



    private void changeStatus(int max){
        if(max >= 70 && max <= 99){
            txtStatus.setText("Alto");
            txtStatus.setTextColor(Color.BLUE);
        }

        if(max >= 50 && max < 70){
            txtStatus.setText("Medio");
            txtStatus.setTextColor(Color.GREEN);
        }

        if(max > 5 && max < 50){
            txtStatus.setText("Baixo");
            txtStatus.setTextColor(Color.YELLOW);
        }

        if(max <= 5){
            txtStatus.setText("Vazio");
            txtNivel.setTextColor(Color.RED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluetooth.result(requestCode,resultCode,data);
    }
}
