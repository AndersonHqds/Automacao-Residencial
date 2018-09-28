package com.example.andersondev.tcc_novo;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.UUID;


public class Control extends AppCompatActivity{
    Bluetooth bluetooth;

    BluetoothSocket btSocket = null;

    Button btnConection;
    ImageButton btnFan, btnLed;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.funcionalidade);

        btnConection = (Button)findViewById(R.id.connect);
        btnLed = (ImageButton)findViewById(R.id.btnLed);
        btnFan = (ImageButton)findViewById(R.id.btnFan);
        bluetooth = new Bluetooth(getApplicationContext(),uuid,Control.this);



        btnConection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.create();
            }
        });


        btnLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth.isConnected()){
                    //connectedThread.write("led1");
                    bluetooth.connectedThread.write("led1");
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth não conectado", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetooth.isConnected()){
                    bluetooth.connectedThread.write("fan");
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth não conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluetooth.result(requestCode,resultCode,data);
    }
}
