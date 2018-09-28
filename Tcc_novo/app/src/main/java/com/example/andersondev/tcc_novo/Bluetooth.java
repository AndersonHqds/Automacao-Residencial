package com.example.andersondev.tcc_novo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Bluetooth extends Activity  {

    private BluetoothAdapter bluetoothAdapter = null;
    private Context context;
    private Activity activity;
    BluetoothDevice device = null;
    BluetoothSocket btSocket = null;
    private static final int BLUETOOTH_ACTIVE = 1;
    private static final int CONNECTION_PERMISSION = 2;
    private boolean isConection = false;
    private boolean hasBlueetooth = false;
    ConnectionThread connectedThread;
    UUID uuid = null;
    private static String MAC = null;

    public Bluetooth(Context context, UUID uuid, Activity activity)
    {
        this.context = context;
        this.uuid = uuid;
        this.activity = activity;
    }

    public void create(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            Toast.makeText(context, "Seu dispositivo não possui bluetooth", Toast.LENGTH_LONG).show();
        }else if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BLUETOOTH_ACTIVE);
            hasBlueetooth = true;
        }
    }


    public void result(int requestCode, int resultCode, Intent data){

        switch(requestCode){

            case BLUETOOTH_ACTIVE:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(context,"O bluetooth foi ativado", Toast.LENGTH_LONG).show();
                    connect();
                }
                else {
                    Toast.makeText(context, "O bluetooth não foi ativado, o app será encerrado", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
                break;
            case CONNECTION_PERMISSION:
                if(resultCode == activity.RESULT_OK){
                    MAC = data.getExtras().getString(ShowDevices.MAC_ADDRESS);

                    device = bluetoothAdapter.getRemoteDevice(MAC);

                    try{
                        //Create a communication socket (channel)
                        btSocket = device.createRfcommSocketToServiceRecord(uuid);
                        btSocket.connect();

                        isConection = true;

                        connectedThread = new ConnectionThread(btSocket);
                        connectedThread.start();

                        Toast.makeText(context,"Conectado com: " + MAC, Toast.LENGTH_LONG).show();
                    }catch(IOException error){
                        isConection = false;
                        Toast.makeText(context,"Falha ao conectar com: " + MAC, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Falha ao obter o endereço MAC", Toast.LENGTH_LONG).show();
                }

        }
    }


    public void connect(){
        if(hasBlueetooth){

            if(isConection){
                //Disconnect
                try{
                    btSocket.close();
                    isConection = false;

                    //btnConection.setText("Conectar");

                    Toast.makeText(context,"Conexão encerrada", Toast.LENGTH_LONG).show();
                }
                catch(IOException error){
                    Toast.makeText(context,"Erro" + error, Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(context, "Chegando ta", Toast.LENGTH_SHORT).show();
                //Connect
                Intent openList = new Intent(activity, ShowDevices.class);

                activity.startActivityForResult(openList,CONNECTION_PERMISSION);
            }

        }

    }

    public boolean isConnected(){
        return isConection;
    }

    public String getMac(){
        return MAC;
    }

    public ConnectionThread getConnectedThread() {
        return connectedThread;
    }
}


