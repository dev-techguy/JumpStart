package com.tecksolke.jumpstart;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class LorryJumpStart extends AppCompatActivity {

    //speech variables
    TextToSpeech toSpeech;
    int result;

    EditText faultype;
    Switch jumpswitch;
    FloatingActionButton btnmic;
    Button bback,blorry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lorry_jump_start);

        //speech method
        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.UK);
                    toSpeech.speak("Hello Am JumpStart ,How May I Help You Fix Your Car", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    Toast.makeText(LorryJumpStart.this, "Feature not supported in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get components
        btnmic = findViewById(R.id.bmicrophone);
        bback = findViewById(R.id.backButton);
        jumpswitch = findViewById(R.id.switchModes);
        faultype = findViewById(R.id.lorryfaults);
        blorry = findViewById(R.id.blorryjumpstart);

        //set button mic to hide mode
        btnmic.setVisibility(View.GONE);

        //initialize method for speaking

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LorryJumpStart.this,Category.class));
               finish();
            }
        });

        btnmic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphone();
            }
        });

        blorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               processLorryFaults();
            }
        });

    }

    //get the mode of input
    public void onSwitchClick(View view){
        if(jumpswitch.isChecked()){
            toSpeech.speak("Speak Mode Activated",TextToSpeech.QUEUE_FLUSH,null);
            btnmic.setVisibility(View.VISIBLE);
            blorry.setVisibility(View.GONE);
            faultype.setVisibility(View.GONE);
        }else{
            toSpeech.speak("Write Mode Activated",TextToSpeech.QUEUE_FLUSH,null);
            btnmic.setVisibility(View.GONE);
            blorry.setVisibility(View.VISIBLE);
            faultype.setVisibility(View.VISIBLE);
        }
    }

    //microphone activation on screen of user
    public void microphone() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the problem Of Your Vehicle");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Sorry Failed To Pick Vehicle Problem", Toast.LENGTH_SHORT).show();
        }
    }

    //for microphone processing
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 100) && (data != null) && (resultCode == RESULT_OK)) {
            ArrayList<String> spokendata = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (spokendata.get(0).equalsIgnoreCase("Hello")) {
                toSpeech.speak("Hello Tech Guy", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    //method for processing lorry fault
    public void processLorryFaults(){
        if(faultype.getText().toString().equalsIgnoreCase("")){
            toSpeech.speak("Please Enter Lorry,  Fault To JumpStart",TextToSpeech.QUEUE_FLUSH,null);
            faultype.setError("Please Enter Lorry Fault To JumpStart...");
        }
        else {
            //do something
        }
    }

    @Override
    protected void onDestroy() {
        if (toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();
    }
}
