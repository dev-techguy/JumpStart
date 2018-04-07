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

public class Car_User_Faults extends Fragment {


    //speech variables
    View view;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;
    EditText faultype;
    ToggleButton jumpswitch;
    FloatingActionButton btnmic;
    Button bcar;
    ArrayList<String> spokendata;
    ImageView imageView;
    Resources resources;

    public Car_User_Faults() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set a nifty dialog
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

        //get string resources
        resources = getResources();


        /*
        * Code for setting an image in a toast
        * */
        imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setMinimumHeight(300);
        imageView.setMinimumWidth(300);

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
        view = inflater.inflate(R.layout.car_user_faults, container, false);
        //get components
        btnmic = view.findViewById(R.id.Car_Microphone);
        jumpswitch =  view.findViewById(R.id.Car_Switch_Modes);
        faultype =  view.findViewById(R.id.carfaults);
        bcar =  view.findViewById(R.id.bcarjumpstart);

        /*
        * Code changing the mode button in toggle button
        * */
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

            if (spokendata.get(0).equalsIgnoreCase("Hello") || spokendata.get(0).equalsIgnoreCase("Hey") || spokendata.get(0).equalsIgnoreCase("hallo")) {
                toSpeech.speak("Hello how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Filter") || spokendata.get(0).equalsIgnoreCase("Filter") || spokendata.get(0).equalsIgnoreCase("Spoiled Filter"))) {
                carFilter();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty GasLifts") || spokendata.get(0).equalsIgnoreCase("Gas Lift") || spokendata.get(0).equalsIgnoreCase("Spoiled Gas Lift"))) {
                gasLifts();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Antenna") || spokendata.get(0).equalsIgnoreCase("Broken Antenna") || spokendata.get(0).equalsIgnoreCase("Spoiled Antenna"))) {
                antennaFault();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Cabin air Filter") || spokendata.get(0).equalsIgnoreCase("Cabin") || spokendata.get(0).equalsIgnoreCase("Spoiled Cabin air"))) {
                cabinFault();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty Bulbs") || spokendata.get(0).equalsIgnoreCase("Bulbs") || spokendata.get(0).equalsIgnoreCase("Non headlight bulbs"))) {
                nonHeadLights();
            } else if ((spokendata.get(0).equalsIgnoreCase("Faulty sun roof") || spokendata.get(0).equalsIgnoreCase("sun roof") || spokendata.get(0).equalsIgnoreCase("sunroof"))) {
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
        } else if ((faultype.getText().toString().equalsIgnoreCase("Hey") || faultype.getText().toString().equalsIgnoreCase("Hello") || faultype.getText().toString().equalsIgnoreCase("hallo") || faultype.getText().toString().equalsIgnoreCase("hallo"))) {
            toSpeech.speak("Hello how may i help you fix your car", TextToSpeech.QUEUE_FLUSH, null);
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Filter") || faultype.getText().toString().equalsIgnoreCase("Filter") || faultype.getText().toString().equalsIgnoreCase("Spoiled Filter"))) {
            carFilter();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty GasLifts") || faultype.getText().toString().equalsIgnoreCase("Gas Lift") || faultype.getText().toString().equalsIgnoreCase("Spoiled Gas Lift"))) {
            gasLifts();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Antenna") || faultype.getText().toString().equalsIgnoreCase("Broken Antenna") || faultype.getText().toString().equalsIgnoreCase("Spoiled Antenna"))) {
            antennaFault();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Cabin air Filter") || faultype.getText().toString().equalsIgnoreCase("Cabin") || faultype.getText().toString().equalsIgnoreCase("Spoiled Cabin air Filter"))) {
            cabinFault();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty Bulbs") || faultype.getText().toString().equalsIgnoreCase("Bulbs") || faultype.getText().toString().equalsIgnoreCase("Non headlight bulbs"))) {
            nonHeadLights();
        } else if ((faultype.getText().toString().equalsIgnoreCase("Faulty sun roof") || faultype.getText().toString().equalsIgnoreCase("sun roof") || faultype.getText().toString().equalsIgnoreCase("sunroof"))) {
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
        final String filterFixingOne = resources.getString(R.string.FilterOne);
        final String filterFixingTwo = resources.getString(R.string.FilterTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.filter);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(filterFixingOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(filterFixingOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(filterFixingTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(filterFixingTwo)
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

    //gas lifts
    private void gasLifts() {
        final String state = "How to Fix a Gas Lift.";
        final String gasFixingOne = resources.getString(R.string.gasLiftOne);
        final String gasFixingTwo = resources.getString(R.string.gasLiftTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.gaslifts);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(gasFixingTwo, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(gasFixingOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(gasFixingTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(gasFixingTwo)
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

    //antenna fixing
    private void antennaFault() {
        final String state = "How to Fix a Broken Antenna.";
        final String brokenAtennaOne = resources.getString(R.string.antennaOne);
        final String brokenAtennaTwo = resources.getString(R.string.antennaTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.antenna);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(brokenAtennaOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(brokenAtennaOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(brokenAtennaTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(brokenAtennaTwo)
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

    //cabin fault
    private void cabinFault() {
        final String state = "How to Fix a Cabin Air Filter.";
        final String cabinFaultOne = resources.getString(R.string.cabinFaultOne);
        final String cabinFaultTwo = resources.getString(R.string.cabinFaultTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.cabin);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(cabinFaultOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(cabinFaultOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(cabinFaultTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(cabinFaultTwo)
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

    //headlights
    private void nonHeadLights() {
        final String state = "How to Replace Non-Headlight Bulbs.";
        final String nonheadOne = resources.getString(R.string.nonheadOne);
        final String nonheadTwo = resources.getString(R.string.nonheadTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.nonheadlights);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(nonheadOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(nonheadOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(nonheadTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(nonheadTwo)
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

    //sunroof fault
    private void sunRoof() {
        final String state = "How to Fix a Leaky Sunroof.";
        final String sunFaultOne = resources.getString(R.string.roofOne);
        final String sunFaultTwo = resources.getString(R.string.roofTwo);
        //speak
        toSpeech.speak(state, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.sunroof);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), state, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(sunFaultOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(sunFaultOne)
                        .withMessageColor("#9dffffff")
                        .withDialogColor("#2A3342")
                        .withButton1Text("NEXT STEPS")
                        .withDuration(700)
                        .isCancelable(false)
                        .withEffect(Effectstype.Fall)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //speak to user
                                toSpeech.speak(sunFaultTwo, TextToSpeech.QUEUE_FLUSH, null);
                                /**
                                 * Show second NiftyDialog
                                 * */
                                niftyDialogBuilder
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(sunFaultTwo)
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
        String noFault = "Sorry your car fault is not yet implemented in JumpStart.";
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
                                    startActivity(new Intent(getActivity(), Car_Garage_Maps.class));
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
