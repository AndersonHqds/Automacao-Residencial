package com.example.andersondev.tcc_novo;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
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
    Handler mHandler;
    Boolean isRead = false;
    Boolean hasRow = false;
    private Cursor cursor;
    BancoDados db;
    int type;
    public Bluetooth(Context context, UUID uuid, Activity activity, int type)
    {
        this.context = context;
        this.uuid = uuid;
        this.activity = activity;
        this.type = type;
    }

    public Bluetooth(Context context, UUID uuid, Activity activity, Handler mHandler, int type)
    {
        this.context = context;
        this.uuid = uuid;
        this.activity = activity;
        this.isRead = true;
        this.mHandler = mHandler;
        this.type = type;
    }

    public void create(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        db = new BancoDados(context);
        //db.getWritableDatabase();
        //db.addSetting(new Settings("FC:A8:9A:00:20:BA", 30.00));
        hasRow = db.hasRow();

        //db.addSetting(new Settings("FC:A8:9A:00:20:BA", 30.00));
        if(bluetoothAdapter == null){
            Toast.makeText(context, "Seu dispositivo não possui bluetooth", Toast.LENGTH_LONG).show();
        }else if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BLUETOOTH_ACTIVE);
            hasBlueetooth = true;
        }
        else{
            cursor = db.getSetting();
            MAC = cursor.getString(1);
            createCommunication(MAC);
        }
    }


    public void result(int requestCode, int resultCode, Intent data){

        switch(requestCode){

            case BLUETOOTH_ACTIVE:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(context,"O bluetooth foi ativado", Toast.LENGTH_LONG).show();
                    if(!hasRow)
                        connect();
                    else{
                        cursor = db.getSetting();
                        MAC = cursor.getString(1);
                        createCommunication(MAC);
                    }
                }
                else {
                    Toast.makeText(context, "O bluetooth não foi ativado, o app será encerrado", Toast.LENGTH_LONG).show();
                    activity.finish();
                }
                break;
            case CONNECTION_PERMISSION:
                if(resultCode == activity.RESULT_OK){
                    MAC = data.getExtras().getString(ShowDevices.MAC_ADDRESS);
                    db.addSetting(new Settings(MAC, 30.00));
                    createCommunication(MAC);

                }else{
                    Toast.makeText(context,"Falha ao obter o endereço MAC", Toast.LENGTH_LONG).show();
                }

        }
    }

    public void createCommunication(String MAC){
        try{
            //btSocket.close();
            device = bluetoothAdapter.getRemoteDevice(MAC);
            //Create a communication socket (channel)
            btSocket = device.createRfcommSocketToServiceRecord(uuid);

            btSocket.connect();
            isConection = true;

            if(isRead) {
                connectedThread = new ConnectionThread(btSocket, context, mHandler);
                connectedThread.start();
            }
            else {
                connectedThread = new ConnectionThread(btSocket, context);
            }

            Toast.makeText(context,"Conectado com: " + MAC, Toast.LENGTH_LONG).show();
            if(type == 0){
                connectedThread.write("cmds");
            }
            if(type == 1){
                connectedThread.write("temperature");
            }
            if(type == 2){
                connectedThread.write("caixa");
            }
        }catch(IOException error){
            isConection = false;
            Toast.makeText(context,"Falha ao conectar com: " + MAC, Toast.LENGTH_LONG).show();
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

    public void closeConn() {
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




