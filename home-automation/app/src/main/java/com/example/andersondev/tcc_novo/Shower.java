package com.example.andersondev.tcc_novo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class Shower extends AppCompatActivity {

    Button start, stop, chart;
    Switch btnShower;
    LottieAnimationView animationView;
    TextView time, valor, litros, energia, valorEnergia;
    int lastMinute = 1,  runningTime = 0, day, month;
    private final double KWH = 0.6;
    private final int DIALOG_ID = 0;
    Consumo  consumo;
    Speech speech;
    Double litrosConsumidos, gastoTotalEnergia, gastoTotalAgua;

    private Chronometer ch;
    private long milliseconds;

    BancoDados db;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog dpd;
    Calendar c;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shower);
        consumo         = new Consumo();
        start           = findViewById(R.id.btnStart);
        stop            = findViewById(R.id.btnStop);
        chart           = findViewById(R.id.btnChart);
        animationView   = findViewById(R.id.arrow);
        valor           = findViewById(R.id.txtValor);
        litros          = findViewById(R.id.txtLitros);
        btnShower       = findViewById(R.id.isShower);
        energia         = findViewById(R.id.lblEnergia);
        valorEnergia    = findViewById(R.id.txtValorEnergia);
        animationView.setMaxFrame(230);
        speech          = new Speech(Shower.this, getApplicationContext());
        speech.initializeTextToSpeech();
        ch              = findViewById(R.id.txtTimer);
        milliseconds    = 0;
        db = new BancoDados(getApplicationContext());


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }

        };


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;

                if(b.getText().equals("Iniciar")){
                    ch.setBase(SystemClock.elapsedRealtime() - 0);
                    ch.start();
                    stop.setEnabled(true);
                    b.setText("Parar");
                    animationView.playAnimation();
                }
                else{
                    ch.stop();
                    stop.setEnabled(false);
                    b.setText("Iniciar");
                    animationView.cancelAnimation();
                    Date todayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String todayString  = formatter.format(todayDate);
                    //Tipo 1 - Água - Valor, 2 - Energia , 3 - Água Litros
                    db.addConsumo(new Consumo(1, todayString,litrosConsumidos));
                    db.addConsumo(new Consumo(2, todayString,gastoTotalEnergia));
                    db.addConsumo(new Consumo(3, todayString,gastoTotalAgua));
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
                    animationView.cancelAnimation();
                }
                else{
                    b.setText("Pausar");
                    ch.setBase(SystemClock.elapsedRealtime() - milliseconds);
                    ch.start();
                    animationView.playAnimation();
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
                //valor em segundos
                //Kw do chuveiro * quantidade de horas * kwh(tarifa local)
                double valorConta = (((4.8 * 1 * KWH) / 60)/60);
                //minutos * 60 ( 1 minuto ) + segundos, e.g: 1*60 + 30 = 90 segundos
                runningTime = minutes * 60 + seconds;

                if(minutes == lastMinute){
                    if(minutes < 10) {
                        speech.speak("Você já atingiu " + getTimeInText(minutes) + " minutos de banho");
                        lastMinute += 1;
                    }
                    else{
                        speech.speak("Você já ultrapassou dez minutos de banho");
                    }

                }

                litrosConsumidos = litros2M3(0.416 * runningTime );
                litros.setText(String.format("%.4f",litrosConsumidos) + " L");
                //4 é o valor do m3 de água daqui de casa
                gastoTotalAgua = 4 * litrosConsumidos;
                valor.setText("R$ " + String.format("%.4f", gastoTotalAgua));
                if(btnShower.isChecked()) {
                    gastoTotalEnergia = runningTime * valorConta;
                }
                else{
                    gastoTotalEnergia = 0.0;
                }
                valorEnergia.setText("R$ " + String.format("%.4f",gastoTotalEnergia));
            }
        });

        btnShower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    energia.setVisibility(View.VISIBLE);
                    valorEnergia.setVisibility(View.VISIBLE);
                }
                else{
                    energia.setVisibility(View.INVISIBLE);
                    valorEnergia.setVisibility(View.INVISIBLE);
                }
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Shower.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        valor.setText(String.valueOf(mMonth));
                        Intent intent = new Intent(getApplicationContext(), Chart.class);
                        intent.putExtra(Intent.EXTRA_TEXT, String.valueOf(mMonth));
                        startActivity(intent);
                    }
                }, day,month,year);
                dpd.show();
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
