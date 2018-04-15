package com.tecksolke.jumpstart;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.Locale;

/**
 * Created by techguy on 4/1/18.
 */

public class Pickup_Spinner_Faults extends Fragment {

    //speech variables
    View view;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;
    ImageView imageView;
    Button buttonSpinner;
    AppCompatSpinner spinnerFaults;
    Resources resources;
    String[] pickupFaults;
    String chooseFault;
    ArrayAdapter<String> adapter;

    public Pickup_Spinner_Faults() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        niftyDialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

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
        view = inflater.inflate(R.layout.pickup_spinner_faults, container, false);

        //set String of car array here
        resources = getResources();
        pickupFaults = resources.getStringArray(R.array.pickup_faults);

        spinnerFaults = view.findViewById(R.id.SpinnerPickupFaults);
        spinnerFaults.setBackgroundColor(Color.parseColor("#2A3342"));

        /*
         * Code for drop down options
         * */
        adapter = new ArrayAdapter<>(spinnerFaults.getContext(), android.R.layout.simple_spinner_item, pickupFaults);
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerFaults.setAdapter(adapter);
        buttonSpinner = view.findViewById(R.id.bpickup_spinner_jumpstart);

        buttonSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPickUpProcessing();
            }
        });

        return view;
    }

    private void adapterPickUpProcessing() {
        if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Choose Pickup Faults")) {
           choosePickUpFault();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("How Change Oil")) {
            toSpeech.stop();
            changeOil();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Excess Smoke")) {
            toSpeech.stop();
            smokyEngine();
        }
    }

    /**
     * Pick Fault Inference Engine
     * Start
     * */
    //smoky engine
    private void smokyEngine() {
        final String state = "Excess Smoke.";
        final String stateSpeech = "How to Fix Excess Smoke.";
        final String smokieOne = resources.getString(R.string.smokeOne);
        final String smokieTwo = resources.getString(R.string.smokeTwo);
        //speak
        toSpeech.speak(stateSpeech, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.smoky);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), stateSpeech, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(smokieOne, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(smokieOne)
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
                                toSpeech.speak(smokieTwo, TextToSpeech.QUEUE_FLUSH, null);
                                //show dialog
                                niftyDialogBuilder
                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(smokieTwo)
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
                                                jumpHelpful();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }
        }, 3500);
    }

    //changing engine oil
    private void changeOil() {
        final String state = "Change Oil.";
        final String stateSpeech = "How to Change Oil In Your PickUp.";
        final String oil1 = resources.getString(R.string.oil_One);
        final String oil2 = resources.getString(R.string.oil_Two);
        final String oil3 = resources.getString(R.string.oil_Three);
        final String oil4 = resources.getString(R.string.oil_Four);
        //speak
        toSpeech.speak(stateSpeech, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.oil);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), stateSpeech, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(imageView);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //speak to user
                toSpeech.speak(oil1, TextToSpeech.QUEUE_FLUSH, null);
                //show dialog
                niftyDialogBuilder
                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                        .withTitle(state)
                        .withTitleColor("#9dffffff")
                        .withMessage(oil1)
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
                                //continue
                                toSpeech.speak(oil2, TextToSpeech.QUEUE_FLUSH, null);

                                niftyDialogBuilder
                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(oil2)
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
                                                //continue
                                                toSpeech.speak(oil3, TextToSpeech.QUEUE_FLUSH, null);

                                                niftyDialogBuilder
                                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                                        .withTitle(state)
                                                        .withTitleColor("#9dffffff")
                                                        .withMessage(oil3)
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
                                                                //continue
                                                                toSpeech.speak(oil4, TextToSpeech.QUEUE_FLUSH, null);

                                                                niftyDialogBuilder
                                                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                                                        .withTitle(state)
                                                                        .withTitleColor("#9dffffff")
                                                                        .withMessage(oil4)
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
                                                                                jumpHelpful();
                                                                            }
                                                                        })
                                                                        .show();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }
        }, 3500);
    }

    //function for user has'nt choose PickUp fault
    private void choosePickUpFault(){
        chooseFault = "Please choose a pickup fault to jumpstart";
        //speak to user
        toSpeech.speak(chooseFault, TextToSpeech.QUEUE_FLUSH, null);
        /**
         * niftyDialogBuilder
         * {@link NiftyDialogBuilder}
         * */
        niftyDialogBuilder
                .withIcon(getResources().getDrawable(R.mipmap.logologo))
                .withTitle("PickUp Fault")
                .withTitleColor("#9dffffff")
                .withMessage(chooseFault)
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

    //jumpStart HElpFul
    private void jumpHelpful() {
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
                                    startActivity(new Intent(getActivity(), Pickup_Garage_Maps.class));
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
