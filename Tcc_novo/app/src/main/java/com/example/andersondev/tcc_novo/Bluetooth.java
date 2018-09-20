package com.example.andersondev.tcc_novo;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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

public class Bluetooth extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter = null;
    BluetoothDevice device = null;
    BluetoothSocket btSocket = null;


    private static final int BLUETOOTH_ACTIVE = 1;
    private static final int CONNECTION_PERMISSION = 2;

    ConnectedThread connectedThread;

    Button btnConection, btnLed1, btnFan;

    private boolean isConection = false;

    private static String MAC = null;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnConection = (Button)findViewById(R.id.conect);
        btnLed1 = (Button)findViewById(R.id.btnLed1);
        btnFan = (Button)findViewById(R.id.btnFan);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Seu dispositivo não possui bluetooth", Toast.LENGTH_LONG).show();
        }else if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,BLUETOOTH_ACTIVE);
        }

        btnConection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(isConection){
            //Disconnect
            try{
                btSocket.close();
                isConection = false;

                btnConection.setText("Conectar");

                Toast.makeText(getApplicationContext(),"Conexão encerrada", Toast.LENGTH_LONG).show();
            }
            catch(IOException error){
                Toast.makeText(getApplicationContext(),"Erro" + error, Toast.LENGTH_LONG).show();
            }
        }
        else{
            //Connect
            Intent openList = new Intent(Bluetooth.this, ShowDevices.class);
            startActivityForResult(openList,CONNECTION_PERMISSION);
        }

        btnLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConection){
                    connectedThread.write("led1");
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth não conectado", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConection){
                    connectedThread.write("fan");
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth não conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case BLUETOOTH_ACTIVE:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(),"O bluetooth foi ativado", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "O bluetooth não foi ativado, o app será encerrado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case CONNECTION_PERMISSION:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(ShowDevices.MAC_ADDRESS);

                    device = bluetoothAdapter.getRemoteDevice(MAC);

                    try{
                        //Create a communication socket (channel)
                        btSocket = device.createRfcommSocketToServiceRecord(uuid);
                        btSocket.connect();

                        isConection = true;

                        connectedThread = new ConnectedThread(btSocket);
                        connectedThread.start();

                        btnConection.setText("Desconectar");
                        Toast.makeText(getApplicationContext(),"Conectado com: " + MAC, Toast.LENGTH_LONG).show();
                    }catch(IOException error){
                        isConection = false;
                        Toast.makeText(getApplicationContext(),"Falha ao conectar com: " + MAC, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Falha ao obter o endereço MAC", Toast.LENGTH_LONG).show();
                }

        }

    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            /*while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                  //  mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                    //          .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }*/
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] buffer = input.getBytes();

            try {
                mmOutStream.write(buffer);
            } catch (IOException e) { }
        }


    }
}
