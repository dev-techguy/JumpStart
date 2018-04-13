package com.tecksolke.jumpstart;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by techguy on 4/1/18.
 */

public class Lorry_User_Faults extends Fragment {

    //speech variables
    View view;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;
    EditText faultype;
    ToggleButton jumpswitch;
    FloatingActionButton btnmic;
    Button blorry;
    ArrayList<String> spokendata;
    ImageView imageView;
    Resources resources;

    public Lorry_User_Faults() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set a nifty dialog
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

        //get resources
        resources = getResources();

        //speech method
        toSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    result = toSpeech.setLanguage(Locale.UK);
                    toSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lorry_user_faults, container, false);

        //get components
        btnmic = view.findViewById(R.id.Lorry_Microphone);
        jumpswitch = view.findViewById(R.id.Lorry_Switch_Modes);
        faultype = view.findViewById(R.id.lorryfaults);
        blorry = view.findViewById(R.id.blorryjumpstart);


        imageView = new ImageView(getActivity());
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

        jumpswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onSwitchClick();
            }
        });
        return view;
    }
    //get the mode of input
    public void onSwitchClick() {
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
            String noFault = "Sorry Failed To Pick lorry Fault.";
            toSpeech.speak(noFault, TextToSpeech.QUEUE_FLUSH, null);
            niftyDialogBuilder
                    .withIcon(getResources().getDrawable(R.mipmap.logologo))
                    .withTitle("Car Fault")
                    .withTitleColor("#9dffffff")
                    .withMessage(noFault)
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("OK")
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 100) && (data != null) && (resultCode == RESULT_OK)) {
            spokendata = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (spokendata.get(0).equalsIgnoreCase("Hello") || spokendata.get(0).equalsIgnoreCase("Hey")|| spokendata.get(0).equalsIgnoreCase("hallo")) {
                toSpeech.speak("Hello how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
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
            toSpeech.stop();
            toSpeech.speak("Please Enter Lorry,  Fault To JumpStart", TextToSpeech.QUEUE_FLUSH, null);
            faultype.setError("Please Enter Lorry Fault To JumpStart...");
        } else if ((faultype.getText().toString().equalsIgnoreCase("Hey") || faultype.getText().toString().equalsIgnoreCase("Hello") || faultype.getText().toString().equalsIgnoreCase("hallo")|| faultype.getText().toString().equalsIgnoreCase("hallo"))) {
            toSpeech.speak("Hello how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((faultype.getText().toString().equalsIgnoreCase("Engine Failed") || faultype.getText().toString().equalsIgnoreCase("Engine wont start") || faultype.getText().toString().equalsIgnoreCase("Engine won't start")|| faultype.getText().toString().equalsIgnoreCase("Start Failed"))) {
            engineStart();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Engine Overheating") || faultype.getText().toString().equalsIgnoreCase("Engine Overheating") || faultype.getText().toString().equalsIgnoreCase("overheating"))) {
            overHeatingEngine();
        }   else {
            faultMissing();
        }
    }

    /**
     * Lorry Inference Faults
     * */
    //lorry faults processing inference engines
    private void engineStart() {
        final String state = "Engine won’t start.";
        final String startFailedOne = resources.getString(R.string.startFailedOne);
        final String startFailedTwo = resources.getString(R.string.startFailedTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.start);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(startFailedOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(startFailedOne)
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
                                //SPEAK TO USER
                                toSpeech.speak(startFailedTwo, TextToSpeech.QUEUE_FLUSH, null);
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(startFailedTwo)
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

    //Overheating Engine
    private void overHeatingEngine(){
        final String state = "How to Fix OverHeating Engine.";
        final String overheatingOne = resources.getString(R.string.overHeating_One);
        final String overheatingTwo = resources.getString(R.string.overHeating_Two);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.overheating);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(overheatingOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(overheatingOne)
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
                                //SPEAK TO USER
                                toSpeech.speak(overheatingTwo, TextToSpeech.QUEUE_FLUSH, null);
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(overheatingTwo)
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
                .withIcon(getResources().getDrawable(R.mipmap.logologo))
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
                                    startActivity(new Intent(getActivity(),Lorry_Garage_Maps.class));
                                   getActivity().finish();
                                }
                            }
                        };
                        timer.start();
                    }
                })
                .show();
    }

}
