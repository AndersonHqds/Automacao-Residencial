package com.example.andersondev.tcc_novo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andersondev.tcc_novo.database.DataOpenHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout layoutCoord;

    /* TTS */
    Speech speech;
    private TextToSpeech myTTS;


    /* DATABASE */
    private SQLiteDatabase conn;
    private DataOpenHelper dataOH;

    /* Bluetooth */
    Bluetooth bluetooth;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");


    /* Controle */
    ImageButton btnControl;

    /* Temperature */
    ImageButton btnTemperature;
    /**
     * When it's being created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /* BLUETOOTH */
        bluetooth = new Bluetooth(getApplicationContext(),uuid,this);
        bluetooth.create();
        speech =  new Speech(this, getApplicationContext(),bluetooth);

        setContentView(R.layout.controle);

        btnControl  = (ImageButton) findViewById(R.id.btnControl);
        btnTemperature = (ImageButton) findViewById(R.id.btnTemperature);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /* Mic button Event */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,new Locale("pt","BR"));

                speech.mySR.startListening(intent);
            }
        });

        /* TTS FUNCTIONS*/
        speech.initializeTextToSpeech();
        speech.initializeSpeechRecognizer();




        /* CREATE CONNECTION DATABASE */
        createConnection();


        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Control.class);
                startActivity(it);
            }
        });

        btnTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Temperature.class);
                startActivity(it);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bluetooth.result(requestCode,resultCode,data);
    }

    private void createConnection(){
        try{
            dataOH = new DataOpenHelper(this);

            conn = dataOH.getWritableDatabase();
            Snackbar.make(findViewById(android.R.id.content), "Conexão criada com sucesso!",Snackbar.LENGTH_SHORT).setAction("OK",null).show();
        }
        catch (SQLException e){
            AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
            dlg.setTitle("Erro");
            dlg.setMessage(e.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onPause(){
        super.onPause();
        speech.myTTS.shutdown();
    }
}
