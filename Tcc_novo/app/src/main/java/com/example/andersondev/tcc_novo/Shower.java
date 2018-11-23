package com.example.andersondev.tcc_novo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Shower extends AppCompatActivity {

    Button start, stop;
    LottieAnimationView animationView;
    TextView time, valor, litros;
    int lastMinute = 1,  runningTime = 0;
    Consumo  consumo;
    Speech speech;
    Double litrosConsumidos;

    private Chronometer ch;
    private long milliseconds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shower);
        consumo = new Consumo();
        start = findViewById(R.id.btnStart);
        stop  = findViewById(R.id.btnStop);
        animationView = findViewById(R.id.arrow);
        valor = findViewById(R.id.txtValor);
        litros = findViewById(R.id.txtLitros);
        animationView.setMaxFrame(230);
        speech = new Speech(Shower.this, getApplicationContext());
        speech.initializeTextToSpeech();
        ch = findViewById(R.id.txtTimer);
        milliseconds = 0;
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;

                if(b.getText().equals("Iniciar")){
                    ch.setBase(SystemClock.elapsedRealtime() - 0);
                    ch.start();
                    stop.setEnabled(true);
                    b.setText("Parar");
                }
                else{
                    ch.stop();
                    stop.setEnabled(false);
                    b.setText("Iniciar");
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if(b.getText().equals("Pausar")){
                    b.setText("Continuar");
                    ch.stop();
                }
                else{
                    b.setText("Pausar");
                    ch.setBase(SystemClock.elapsedRealtime() - milliseconds);
                    ch.start();
                }
                milliseconds = SystemClock.elapsedRealtime() - ch.getBase();


            }
        });

        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String [] time = chronometer.getText().toString().split(":");
                int seconds = Integer.parseInt(time[1]);
                int minutes = Integer.parseInt(time[0]);

                runningTime = minutes * 60 + seconds;

                if(minutes == lastMinute){
                    if(minutes < 10) {
                        speech.speak("Você já atingiu " + getTimeInText(minutes) + " de banho");
                        lastMinute += 1;
                    }
                    else{
                        speech.speak("Você já ultrapassou dez minutos de banho");
                    }

                }

                litrosConsumidos = litros2M3(0.2 * runningTime  );
                litros.setText(String.format("%.4f",litrosConsumidos) + " L");
                //4 é o valor do m3 de água daqui de casa
                valor.setText("R$ " + String.format("%.4f",4 * litrosConsumidos));
            }
        });

    }


    public String getTimeInText(int minute){

        switch(minute){
            case 1: return "um";
            case 2: return "dois";
            case 3: return "tres";
            case 4: return "quatro";
            case 5: return "cinco";
            case 6: return "seis";
            case 7: return "sete";
            case 8: return "oito";
            case 9: return "nove";
            case 10: return "dez";
        }
        return "";
    }

    public Double litros2M3(double litros){
        return litros/1000.0;
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(Shower.this, MainActivity.class);
        startActivity(it);
    }
}
