package com.example.andersondev.tcc_novo;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;




public class ConnectionThread extends Thread{
        Thread workerThread;
        byte[] readBuffer;
        int readBufferPosition;
        volatile boolean stopWorker;
        TextView txt;
        private InputStream mmInStream;
        private final OutputStream mmOutStream;
        public static final int MESSAGE_READ = 3;
        public static Context context;
        Handler mHandler;
        StringBuilder bluetoothData = new StringBuilder();

        public ConnectionThread(BluetoothSocket socket, Context context, Handler mHandler) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                mmInStream = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmOutStream = tmpOut;
            this.context = context;
            this.mHandler = mHandler;
        }

    public ConnectionThread(BluetoothSocket socket, Context context) {

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            mmInStream = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }


        mmOutStream = tmpOut;
        this.context = context;
    }



    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                String btData = new String(buffer, 0, bytes);

                // Send the obtained bytes to the UI activity
                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, btData)
                        .sendToTarget();
            } catch (IOException e) {}
        }
    }
        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] buffer = input.getBytes();

            try {
                mmOutStream.write(buffer);
            } catch (IOException e) { }
        }

        public void read(Context context){
            try {
                Toast.makeText(context,mmInStream.read(), Toast.LENGTH_LONG).show() ;
            }catch(IOException e){}
        }


}

