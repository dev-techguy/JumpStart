package com.tecksolke.jumpstart;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.Locale;

public class Lorry_JumpStart extends AppCompatActivity {

    //speech variables
    TemporaryDB temporaryDB;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;

    EditText faultype;
    //Switch jumpswitch;
    ToggleButton jumpswitch;
    FloatingActionButton btnmic;
    Button blorry;
    String username = null;
    ArrayList<String> spokendata;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lorry_jump_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        temporaryDB = new TemporaryDB(this);
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //call meted for fetching data
        readData();

        //speech method
        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.UK);
                    toSpeech.speak("Hello " + getUsername() + ",  Am JumpStart , How May I Help You Fix Your Lorry", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    niftyDialogBuilder
                            .withIcon(getResources().getDrawable(R.mipmap.logologo))
                            .withTitle("Speech Talking Status")
                            .withTitleColor("#9dffffff")
                            .withMessage("Feature not supported in your device")
                            .withMessageColor("#9dffffff")
                            .withDialogColor("#2A3342")
                            .withButton1Text("OK")
                            .withDuration(700)
                            .isCancelable(false)
                            .withEffect(Effectstype.Shake)
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toSpeech.stop();
                                    niftyDialogBuilder.cancel();
                                }
                            })
                            .show();
                }
            }
        });

        //get components
        btnmic = findViewById(R.id.Lorry_Microphone);
        jumpswitch = findViewById(R.id.Lorry_Switch_Modes);
        faultype = findViewById(R.id.lorryfaults);
        blorry = findViewById(R.id.blorryjumpstart);


        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setMinimumHeight(300);
        imageView.setMinimumWidth(300);

        jumpswitch.setTextOff("Write Mode");
        jumpswitch.setTextOn("Speak Mode");

        //set button mic to hide mode
        btnmic.setVisibility(View.GONE);


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
    public void onSwitchClick(View view) {
        if (jumpswitch.isChecked()) {
            toSpeech.speak("Speak Mode Activated . Click the mic button.", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setText("");
            btnmic.setVisibility(View.VISIBLE);
            blorry.setVisibility(View.GONE);
            faultype.setVisibility(View.GONE);
        } else {
            toSpeech.speak("Write Mode Activated", TextToSpeech.QUEUE_FLUSH, null);
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the problem Of Your Lorry");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Sorry Failed To Pick lorry Fault", Toast.LENGTH_SHORT).show();
        }
    }

    //for microphone processing
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 100) && (data != null) && (resultCode == RESULT_OK)) {
            spokendata = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (spokendata.get(0).equalsIgnoreCase("Hello") || spokendata.get(0).equalsIgnoreCase("Hey")|| spokendata.get(0).equalsIgnoreCase("hallo")) {
                toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
            } else if ((spokendata.get(0).equalsIgnoreCase("Engine Failed") || spokendata.get(0).equalsIgnoreCase("Engine wont start") || spokendata.get(0).equalsIgnoreCase("Engine won't start")|| spokendata.get(0).equalsIgnoreCase("Start Failed"))) {
                engineStart();
            }else if ((spokendata.get(0).equalsIgnoreCase("Engine Overheating") || spokendata.get(0).equalsIgnoreCase("Overheating engine") || spokendata.get(0).equalsIgnoreCase("overheating"))) {
                overHeatingEngine();
            } else {
                faultMissing();
            }
        }
    }

    //method for processing lorry fault
    public void processLorryFaults() {
        if (faultype.getText().toString().equalsIgnoreCase("")) {
            toSpeech.speak("Please Enter Lorry,  Fault To JumpStart", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setError("Please Enter Lorry Fault To JumpStart...");
        } else if ((faultype.getText().toString().equalsIgnoreCase("Hey") || faultype.getText().toString().equalsIgnoreCase("Hello") || faultype.getText().toString().equalsIgnoreCase("hallo")|| faultype.getText().toString().equalsIgnoreCase("hallo"))) {
            toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((faultype.getText().toString().equalsIgnoreCase("Engine Failed") || faultype.getText().toString().equalsIgnoreCase("Engine wont start") || faultype.getText().toString().equalsIgnoreCase("Engine won't start")|| faultype.getText().toString().equalsIgnoreCase("Start Failed"))) {
           engineStart();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Engine Overheating") || faultype.getText().toString().equalsIgnoreCase("Engine Overheating") || faultype.getText().toString().equalsIgnoreCase("overheating"))) {
           overHeatingEngine();
        } else {
            faultMissing();
        }
    }

    //lorry faults processing inference engines
    private void engineStart() {
        final String state = "Engine won’t start.";
        final String filterFixing = "1. Check the fuel supply. Replace fuel filters.\n2. Check batteries and connections to starter.\n3. Check starter motor.\n4. Check fuel pump and fuel lines.\n5. Check fuel for contamination.Because dirty fuel will cause problems.\n6. Check and clean the air filters. Replace if necessary.\n7. Check the fuel Injectors.\n8. Close and Check. Then try to start the engine ones more.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.start);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(filterFixing, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(filterFixing)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                jumpHelpfull();
                            }
                        })
                        .show();
            }
        }, 3500);
    }

    //Overheating Engine
    private void overHeatingEngine(){
        final String state = "How to Fix OverHeating Engine.";
        final String filterFixing = "1. Check the fuel supply. Replace fuel filters.\n2. Check batteries and connections to starter.\n3. Check starter motor.\n4. Check fuel pump and fuel lines.\n5. Check fuel for contamination.Because dirty fuel will cause problems.\n6. Check and clean the air filters. Replace if necessary.\n7. Check the fuel Injectors.\n8. Close and Check. Then try to start the engine ones more.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.start);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(filterFixing, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(filterFixing)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                jumpHelpfull();
                            }
                        })
                        .show();
            }
        }, 3500);
    }


    //car fault missing
    private void faultMissing(){
        String noFault = "Sorry your Lorry fault is not yet implemented in JumpStart.";
        toSpeech.speak(noFault, TextToSpeech.QUEUE_FLUSH, null);
        niftyDialogBuilder
                .withIcon(getResources().getDrawable(R.mipmap.logologo))
                .withTitle("Lorry Fault")
                .withTitleColor("#9dffffff")
                .withMessage(noFault)
                .withMessageColor("#9dffffff")
                .withDialogColor("#2A3342")
                .withButton1Text("NEXT")
                .withDuration(700)
                .isCancelable(false)
                .withEffect(Effectstype.Fliph)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toSpeech.stop();
                        niftyDialogBuilder.cancel();
                        jumpHelpfull();
                    }
                })
                .show();
    }

    //jumpStart HElpFul
    private void jumpHelpfull() {
        //talk
        toSpeech.speak("Was JumpStart Helpful", TextToSpeech.QUEUE_FLUSH, null);
        niftyDialogBuilder
                .withTitle("JumpStart")
                .withTitleColor("#9dffffff")
                .withMessage("Was JumpStart Helpful")
                .withMessageColor("#9dffffff")
                .withDialogColor("#2A3342")
                .withButton1Text("YES")
                .withButton2Text("FIND GARAGE")
                .withDuration(700)
                .isCancelable(false)
                .withEffect(Effectstype.Fall)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toSpeech.speak("Thank you For Using JumpStart.", TextToSpeech.QUEUE_FLUSH, null);
                        faultype.setText("");
                        niftyDialogBuilder.cancel();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toSpeech.speak("JumpStart will locate for you the nearest garage for assistance. Launching Maps", TextToSpeech.QUEUE_FLUSH, null);
                        niftyDialogBuilder.cancel();
                        //start a thread to give a counter
                        Thread timer = new Thread() {
                            public void run() {
                                try {
                                    //give your delay timer here
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    startActivity(new Intent(Lorry_JumpStart.this,Lorry_Garage_Maps.class));
                                    finish();
                                }
                            }
                        };
                        timer.start();
                    }
                })
                .show();
    }

    //function for reading data
    public void readData() {
        Cursor res = temporaryDB.getAllData();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                username = String.valueOf((res.getString(1)));
            }
        }
        setUsername(username);
    }

    //get username
    public String getUsername() {
        return username;
    }

    //set username
    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        toSpeech.stop();
        startActivity(new Intent(Lorry_JumpStart.this, Category.class));
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.jump_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            deleteData();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (id == R.id.Garage) {
            startActivity(new Intent(this, Lorry_Garage_Maps.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //delete data from SQlite
    public void deleteData() {
        Cursor res = temporaryDB.getAllData();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                temporaryDB.deleteData(res.getString(0));
            }
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
