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

public class Car_JumpStart extends AppCompatActivity {

    //speech variables
    TemporaryDB temporaryDB;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;

    EditText faultype;
    ToggleButton jumpswitch;
    FloatingActionButton btnmic;
    Button bcar;
    String username = null;
    ArrayList<String> spokendata;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__jump_start);

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
                    toSpeech.speak("Hello " + getUsername() + ",  Am JumpStart , How May I Help You Fix Your Car", TextToSpeech.QUEUE_FLUSH, null);
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
        btnmic = findViewById(R.id.Car_Microphone);
        jumpswitch = findViewById(R.id.Car_Switch_Modes);
        faultype = findViewById(R.id.carfaults);
        bcar = findViewById(R.id.bcarjumpstart);

        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setMinimumHeight(300);
        imageView.setMinimumWidth(300);

//        LayoutInflater layoutInflater = getLayoutInflater();
//        viewFilter = layoutInflater.inflate(R.layout.custom_car_faults, (ViewGroup) findViewById(R.id.Car_Filter_Image));
//        viewGasLifts = layoutInflater.inflate(R.layout.custom_car_faults, (ViewGroup) findViewById(R.id.Car_GasLifts_Image));
//
//        imageView = viewFilter.findViewById(R.id.FilterImage);

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

        bcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processcarFaults();
            }
        });

    }

    //get the mode of input
    public void onSwitchClick(View view) {
        if (jumpswitch.isChecked()) {
            toSpeech.speak("Speak Mode Activated . Click the mic button.", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setText("");
            btnmic.setVisibility(View.VISIBLE);
            bcar.setVisibility(View.GONE);
            faultype.setVisibility(View.GONE);
        } else {
            toSpeech.speak("Write Mode Activated", TextToSpeech.QUEUE_FLUSH, null);
            btnmic.setVisibility(View.GONE);
            bcar.setVisibility(View.VISIBLE);
            faultype.setVisibility(View.VISIBLE);
        }
    }

    //microphone activation on screen of user
    public void microphone() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the problem Of Your Car");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            String noFault = "Sorry Failed To Pick Car Fault.";
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

            if (spokendata.get(0).equalsIgnoreCase("Hello") || spokendata.get(0).equalsIgnoreCase("Hey")|| spokendata.get(0).equalsIgnoreCase("hallo")) {
                toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Filter") || spokendata.get(0).equalsIgnoreCase("Filter") || spokendata.get(0).equalsIgnoreCase("Spoiled Filter"))) {
                carFilter();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty GasLifts") || spokendata.get(0).equalsIgnoreCase("Gas Lift") || spokendata.get(0).equalsIgnoreCase("Spoiled Gas Lift"))) {
                gasLifts();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Antenna") || spokendata.get(0).equalsIgnoreCase("Broken Antenna") || spokendata.get(0).equalsIgnoreCase("Spoiled Antenna"))) {
               antennaFault();
            }else if ((spokendata.get(0).equalsIgnoreCase("Faulty Cabin air Filter") || spokendata.get(0).equalsIgnoreCase("Cabin") || spokendata.get(0).equalsIgnoreCase("Spoiled Cabin air"))) {
               cabinFault();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Bulbs") || spokendata.get(0).equalsIgnoreCase("Bulbs") || spokendata.get(0).equalsIgnoreCase("Non headlight bulbs"))) {
              nonHeadLights();
            }else if ((spokendata.get(0).equalsIgnoreCase("Faulty sun roof") || spokendata.get(0).equalsIgnoreCase("sun roof") || spokendata.get(0).equalsIgnoreCase("sunroof"))) {
              sunRoof();
            } else {
                faultMissing();
            }
        }
    }

    //method for processing car fault
    public void processcarFaults() {
        if (faultype.getText().toString().equalsIgnoreCase("")) {
            toSpeech.speak("Please Enter Car,  Fault To JumpStart", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setError("Please Enter Car Fault To JumpStart...");
        }else if ((faultype.getText().toString().equalsIgnoreCase("Hey") || faultype.getText().toString().equalsIgnoreCase("Hello") || faultype.getText().toString().equalsIgnoreCase("hallo")|| faultype.getText().toString().equalsIgnoreCase("hallo"))) {
            toSpeech.speak("Hello " + getUsername() + "how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Filter") || faultype.getText().toString().equalsIgnoreCase("Filter") || faultype.getText().toString().equalsIgnoreCase("Spoiled Filter"))) {
            carFilter();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty GasLifts") || faultype.getText().toString().equalsIgnoreCase("Gas Lift") || faultype.getText().toString().equalsIgnoreCase("Spoiled Gas Lift"))) {
            gasLifts();
        }else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Antenna") || faultype.getText().toString().equalsIgnoreCase("Broken Antenna") || faultype.getText().toString().equalsIgnoreCase("Spoiled Antenna"))) {
            antennaFault();
        }else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Cabin air Filter") || faultype.getText().toString().equalsIgnoreCase("Cabin") || faultype.getText().toString().equalsIgnoreCase("Spoiled Cabin air Filter"))) {
            cabinFault();
        }else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Bulbs") || faultype.getText().toString().equalsIgnoreCase("Bulbs") || faultype.getText().toString().equalsIgnoreCase("Non headlight bulbs"))) {
            nonHeadLights();
        }else if ((faultype.getText().toString().equalsIgnoreCase("Faulty sun roof") || faultype.getText().toString().equalsIgnoreCase("sun roof") || faultype.getText().toString().equalsIgnoreCase("sunroof"))) {
            sunRoof();
        } else {
          faultMissing();
        }
    }

    /**
     * car faults processing inference engines
     */
    //carFilter
    private void carFilter() {
        final String state = "How to Fix an Air Filter.";
        final String filterFixing = "1. Identify the cold air intake system of the truck’s engine. That is where the air filter is installed.\n2. Identify the adjacent parts next to the faulty filter and remove them.\n3. Start removing the clamps, hooks, fasteners, clips, hoses and any other holding part on the engine.\n4. Remove Air Intake System.\n5. Clean the Parts.\n6. Replace the Air Filter.\n7. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.filter);

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

    //gas lifts
    private void gasLifts() {
        final String state = "How to Fix a Gas Lift.";
        final String gasFixing = "1. Lift the hatch slightly higher than its normal open position and have a friend hold the hatch up while you remove the gas lift. Or lock it in place with a lift support clamp.\n2. To disengage the spring clip, simply shove a small flat blade screwdriver between the clip and the cylinder.\n3.Then pull the cylinder off the ball stud.\n4. Replace the gas lift.\n5. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.gaslifts);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(gasFixing, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(gasFixing)
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

    //antenna fixing
    private void antennaFault(){
        final String state = "How to Fix a Broken Antenna.";
        final String brokenAtenna = "1.  Disconnect the antenna cable from your radio and connect heavy string to the end.\n2. Then unscrew the antenna mount from the pillar and pull the old antenna and the string straight out.\n3. Attach the new antenna cable to the string, pull the cable back into the vehicle and connect it to your radio.\n4. Then secure the new antenna to the pillar using the screws provided.\n5. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.antenna);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(brokenAtenna, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(brokenAtenna)
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

    //cabin fault
    private void cabinFault(){
        final String state = "How to Fix a Cabin Air Filter.";
        final String brokenAtenna = "1. Cabin air filters are usually located in the air ducts behind the glove box in late model vehicles.\n2. Must remove the access covers and slide out the old filter but note the direction of the airflow arrows so you can install the new filter in the proper orientation .\n3. Then reinstall the covers.\n4. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.cabin);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(brokenAtenna, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(brokenAtenna)
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

    //headlights
    private void nonHeadLights(){
        final String state = "How to Replace Non-Headlight Bulbs.";
        final String brokenAtenna = "1. Remove the retaining screws and pry off the lens.\n2.  Pull the bulb straight out of the socket.\n3. Handle the new bulb with gloved hands or hold it with a paper towel to prevent skin oils from depositing on the thin glass ? that can cause premature bulb failure.\n4. Then push the bulb into the socket until it clicks\n5. Reinstall the lens\n6. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.nonheadlights);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(brokenAtenna, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(brokenAtenna)
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

    //sunroof fault
    private void sunRoof(){
        final String state = "How to Fix a Leaky Sunroof.";
        final String brokenAtenna = "1. Open the sunroof and look for drain holes in the front and rear corners of your sunroof.\n2.  Once you locate the drains, duct tape a small rubber or plastic tube to the end of your shop vacuum and suck out any debris stuck in the drains.\n3. Then dribble water into each drain and check under the car to see if it's draining onto your driveway or garage floor.\n4. Then Flush the drain after snaking it with the speedometer cable. If it now runs free.\n5. Close and Check.";
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.sunroof);

        //show image in toast
        Toast toast = Toast.makeText(this, state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(brokenAtenna, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(brokenAtenna)
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
        String noFault = "Sorry your car fault is not yet implemented in JumpStart.";
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
                                    startActivity(new Intent(Car_JumpStart.this, Car_Garage_Maps.class));
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
        startActivity(new Intent(Car_JumpStart.this, Category.class));
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
            startActivity(new Intent(this, Car_Garage_Maps.class));
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
