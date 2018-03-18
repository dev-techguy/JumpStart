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

public class PickUp_JumpStart extends AppCompatActivity {
    //speech variables
    TemporaryDB temporaryDB;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;

    EditText faultype;
    //Switch jumpswitch;
    ToggleButton jumpswitch;
    FloatingActionButton btnmic;
    Button bpickup;
    String username = null;
    ArrayList<String> spokendata;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up__jump_start);

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
                    toSpeech.speak("Hello " + getUsername() + ",  Am JumpStart , How May I Help You Fix Your PickUp", TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    niftyDialogBuilder
                            .withIcon(getResources().getDrawable(R.mipmap.logo))
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
        btnmic = findViewById(R.id.Pickup_Microphone);
        jumpswitch = findViewById(R.id.Pickup_Switch_Modes);
        faultype = findViewById(R.id.pickupfaults);
        bpickup = findViewById(R.id.bpickupjumpstart);


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

        bpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processpickupFaults();
            }
        });
    }

    //get the mode of input
    public void onSwitchClick(View view) {
        if (jumpswitch.isChecked()) {
            toSpeech.speak("Speak Mode Activated . Click the mic button.", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setText("");
            btnmic.setVisibility(View.VISIBLE);
            bpickup.setVisibility(View.GONE);
            faultype.setVisibility(View.GONE);
        } else {
            toSpeech.speak("Write Mode Activated", TextToSpeech.QUEUE_FLUSH, null);
            btnmic.setVisibility(View.GONE);
            bpickup.setVisibility(View.VISIBLE);
            faultype.setVisibility(View.VISIBLE);
        }
    }

    //microphone activation on screen of user
    public void microphone() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the problem Of Your PickUp");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            String noFault = "Sorry Failed To Pick PickUp Fault.";
            toSpeech.speak(noFault, TextToSpeech.QUEUE_FLUSH, null);
            niftyDialogBuilder
                    .withIcon(getResources().getDrawable(R.mipmap.logologo))
                    .withTitle("Car Fault")
                    .withTitleColor("#9dffffff")
                    .withMessage(noFault)
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("NEXT")
                    .withDuration(700)
                    .isCancelable(false)
                    .withEffect(Effectstype.Newspager)
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
    }

    //for microphone processing
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 100) && (data != null) && (resultCode == RESULT_OK)) {
            spokendata = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (spokendata.get(0).equalsIgnoreCase("Hello") || spokendata.get(0).equalsIgnoreCase("Hey") || spokendata.get(0).equalsIgnoreCase("hallo")) {
                toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
            } else if ((spokendata.get(0).equalsIgnoreCase("Excess Smoke") || spokendata.get(0).equalsIgnoreCase("Smoking Engine") || spokendata.get(0).equalsIgnoreCase("Smoke"))) {
                smokyEngine();
            } else if ((spokendata.get(0).equalsIgnoreCase("Change Oil") || spokendata.get(0).equalsIgnoreCase("Oil") || spokendata.get(0).equalsIgnoreCase("Oil Change"))) {
                changeOil();
            } else {
                faultMissing();
            }
        }
    }

    //method for processing pickup fault
    public void processpickupFaults() {
        if (faultype.getText().toString().equalsIgnoreCase("")) {
            toSpeech.speak("Please Enter Pickup,  Fault To JumpStart", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setError("Please Enter Pickup Fault To JumpStart...");
        } else if ((faultype.getText().toString().equalsIgnoreCase("Hey") || faultype.getText().toString().equalsIgnoreCase("Hello") || faultype.getText().toString().equalsIgnoreCase("hallo") || faultype.getText().toString().equalsIgnoreCase("hallo"))) {
            toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((faultype.getText().toString().equalsIgnoreCase("Excess Smoke") || faultype.getText().toString().equalsIgnoreCase("Smoking Engine") || faultype.getText().toString().equalsIgnoreCase("Smoke"))) {
            smokyEngine();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Change Oil") || faultype.getText().toString().equalsIgnoreCase("Oil") || faultype.getText().toString().equalsIgnoreCase("Oil Change"))) {
            changeOil();
        } else {
            faultMissing();
        }
    }

    //pickup faults processing inference engines
    //smoky engine
    private void smokyEngine() {
        final String state = "How to Fix Excess Smoke.";
        final String filterFixing = "1. White smoke can mean the engine timing is off or the engine compression is weak.\n2. Blue smoke can mean worn cylinders, piston rings and valves.\n3. Black smoke can mean dirty air filters, bad injectors, a turbo problem or a problem in a cylinder head (insufficient fuel to the cylinder).\n4. Check and fix.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.smoky);

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
                        .withButton1Text("OK")
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

    //changing engine oil
    private void changeOil() {
        final String state = "How to Change Oil In Your PickUp.";
        final String filterFixing = "1. Before you change your oil you should run your pickup and get the oil warm so that it will gather all the dirty particles.\n2. Drain the oil from the oil pan, and clean the bolt with a rag and screw it back in with your fingers.\n3. Use the oil filter wrench to unscrew the oil filter which is to the right of the oil pan.";

        final String filterFixing2 = "4. Dispose of old filter and the new filter can be screwed in place\n5. Take fresh oil and rub it around the rim of the filter. Hand tighten the filter.\n6. Pour the recommended amount of oil into the vehicle.\n7. Run vehicle for ten minutes, then check oil levels with dipstick.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.oil);

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
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                niftyDialogBuilder.cancel();
                                //continue
                                toSpeech.speak(filterFixing2, TextToSpeech.QUEUE_FLUSH, null);

                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(filterFixing2)
                                        .withMessageColor("#9dffffff")
                                        .withDialogColor("#2A3342")
                                        .withButton1Text("OK")
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
                        })
                        .show();
            }
        }, 3500);
    }


    //car fault missing
    private void faultMissing() {
        String noFault = "Sorry. Your pickup fault is not yet implemented in JumpStart.";
        toSpeech.speak(noFault, TextToSpeech.QUEUE_FLUSH, null);
        niftyDialogBuilder
                .withIcon(getResources().getDrawable(R.mipmap.logologo))
                .withTitle("Pickup Fault")
                .withTitleColor("#9dffffff")
                .withMessage(noFault)
                .withMessageColor("#9dffffff")
                .withDialogColor("#2A3342")
                .withButton1Text("OK")
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
                                    startActivity(new Intent(PickUp_JumpStart.this, Pickup_Garage_Maps.class));
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
        startActivity(new Intent(PickUp_JumpStart.this, Category.class));
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
            startActivity(new Intent(this, Pickup_Garage_Maps.class));
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
