package com.example.andersondev.tcc_novo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Speech {

    public TextToSpeech myTTS;
    public SpeechRecognizer mySR;

    private Activity activity;
    private Context context;

    /* Bluetooth */
    Bluetooth bluetooth;
    public Speech(Activity activity, Context context, Bluetooth bt){
        this.activity = activity;
        this.context = context;
        bluetooth = bt;
    }

    public void initializeSpeechRecognizer() {

        if(SpeechRecognizer.isRecognitionAvailable(activity)){
            mySR = SpeechRecognizer.createSpeechRecognizer(activity);
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


    public void initializeTextToSpeech() {
        myTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size() == 0){
                    Toast.makeText(context, "There is no TTS engine on your device", Toast.LENGTH_LONG).show();
                    activity.finish();
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

    public void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message, TextToSpeech.QUEUE_ADD, null, null);
        }
        else{
            myTTS.speak(message, TextToSpeech.QUEUE_ADD, null);
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
                String time = DateUtils.formatDateTime(context, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("A hora agora é" + time);
            }
            else if(cmd.contains("o melhor sistema")){
                speak("Óbvio que é linux");
            }
            else{
                speak("O comando qual pode ser seguindo por, qual a hora, qual o seu nome");
            }
        }
        else{
            if(cmd.indexOf("abrir") != -1){
                if(cmd.contains("navegador")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://devanderson.000webhostapp.com"));
                    activity.startActivity(intent);
                }
            }
            else if(cmd.indexOf("ligar luz") != -1){
                bluetooth.connectedThread.write("led1");
            }
        }
    }
}
