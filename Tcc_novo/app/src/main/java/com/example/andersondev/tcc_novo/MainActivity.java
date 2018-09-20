package com.example.andersondev.tcc_novo;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.example.andersondev.tcc_novo.database.DataOpenHelper;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout layoutCoord;

    /* TTS */
    private TextToSpeech myTTS;
    private SpeechRecognizer mySR;

    /* DATABASE */
    private SQLiteDatabase conn;
    private DataOpenHelper dataOH;

    /* Bluetooth */




    /**
     * When it's being created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.controle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /* Mic button Event */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,new Locale("pt","BR"));

                mySR.startListening(intent);
            }
        });

        /* TTS FUNCTIONS*/
        initializeTextToSpeech();
        initializeSpeechRecognizer();

        layoutCoord = (CoordinatorLayout)findViewById(R.id.layout_main);

        /* CREATE CONNECTION DATABASE
        createConnection();
        new Bluetooth();
        */
    }

    private void createConnection(){
        try{
            dataOH = new DataOpenHelper(this);

            conn = dataOH.getWritableDatabase();
            Snackbar.make(layoutCoord, "Conexão criada com sucesso!",Snackbar.LENGTH_SHORT)
                    .setAction("OK",null).show();
        }
        catch (SQLException e){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(e.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySR = SpeechRecognizer.createSpeechRecognizer(this);
            mySR.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );

                    processResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String cmd) {
        cmd = cmd.toLowerCase();

        if(cmd.contains("qual")){
            if(cmd.contains("o seu nome")){
                speak("Meu nome é Skyler.");
            }
            else if(cmd.contains("a hora")){
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("A hora agora é" + time);
            }
            else if(cmd.contains("o melhor sistema")){
                speak("Óbvio que é linux");
            }
            else{
                speak("Desculpe mas não entendi");
            }
        }
        else{
            speak("Não entendi");
            if(cmd.indexOf("abrir") != -1){
                if(cmd.contains("navegador")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://devanderson.000webhostapp.com"));
                    startActivity(intent);
                }
            }
        }
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(MainActivity.this, "There is no TTS engine on your device", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Locale locale = new Locale("pt","BR");
                    myTTS.setLanguage(locale);
                    speak("Olá anderson");
                    speak("Eu estou pronta para falar");
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message, TextToSpeech.QUEUE_ADD, null, null);
        }
        else{
            myTTS.speak(message, TextToSpeech.QUEUE_ADD, null);
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
        myTTS.shutdown();
    }
}
