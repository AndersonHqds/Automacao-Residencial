package com.example.andersondev.tcc_novo;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ShowDevices extends ListActivity{

    private BluetoothAdapter bluetoothAdapter = null;

    static String MAC_ADDRESS = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                String btName = device.getName();
                String mac = device.getAddress();
                ArrayBluetooth.add(btName + "\n" + mac);
            }
        }
        setListAdapter(ArrayBluetooth);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String generalInfo = ((TextView) v).getText().toString();

        //Toast.makeText(getApplicationContext(), "Info: " + generalInfo, Toast.LENGTH_LONG).show();

        //17 is the MAC ADDRESS length
        String macAddress = generalInfo.substring(generalInfo.length() - 17);

        Intent macReturn = new Intent();
        macReturn.putExtra(MAC_ADDRESS, macAddress);

        setResult(RESULT_OK, macReturn);
        finish();
    }
}
