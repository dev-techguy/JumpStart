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

public class Lorry_Spinner_Faults extends Fragment {
    //speech variables
    View view;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;
    ImageView imageView;
    Button buttonSpinner;
    AppCompatSpinner spinnerFaults;
    Resources resources;
    String[] lorryFaults;
    String chooseFault;
    ArrayAdapter<String> adapter;

    public Lorry_Spinner_Faults() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set a nifty dialog
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

        //get resources
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
        view = inflater.inflate(R.layout.lorry_spinner_faults, container, false);
        //set String of car array here
        resources = getResources();
        lorryFaults = resources.getStringArray(R.array.lorry_faults);

        spinnerFaults = view.findViewById(R.id.SpinnerLorryFaults);
        spinnerFaults.setBackgroundColor(Color.parseColor("#2A3342"));

        /*
         * Code for drop down options
         * */
        adapter = new ArrayAdapter<>(spinnerFaults.getContext(), android.R.layout.simple_spinner_item, lorryFaults);
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerFaults.setAdapter(adapter);
        buttonSpinner = view.findViewById(R.id.blorry_spinner_jumpstart);

        buttonSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterLorryProcessing();
            }
        });

        return  view;
    }

    private void adapterLorryProcessing() {
        if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Choose Lorry Fault")) {
          chooseLorryFault();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Engine Start Failure")) {
            toSpeech.stop();
           engineStart();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Engine Overheating")) {
            toSpeech.stop();
            overHeatingEngine();
        }
    }

    /**
     * Lorry Inference Faults
     * */
    //lorry faults processing inference engines
    private void engineStart() {
        final String state = "Engine Start.";
        final String stateSpeech = "How to fix Engine start failure.";
        final String startFailedOne = resources.getString(R.string.startFailedOne);
        final String startFailedTwo = resources.getString(R.string.startFailedTwo);
        final String startFailedThree = resources.getString(R.string.startFailedThree);
        //speak
        toSpeech.speak(stateSpeech, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.start);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), stateSpeech, Toast.LENGTH_LONG);
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
                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
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
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //SPEAK TO USER
                                toSpeech.speak(startFailedTwo, TextToSpeech.QUEUE_FLUSH, null);
                                niftyDialogBuilder
                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(startFailedTwo)
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
                                                //SPEAK TO USER
                                                toSpeech.speak(startFailedThree, TextToSpeech.QUEUE_FLUSH, null);
                                                niftyDialogBuilder
                                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                                        .withTitle(state)
                                                        .withTitleColor("#9dffffff")
                                                        .withMessage(startFailedThree)
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
        }, 3500);
    }

    //Overheating Engine
    private void overHeatingEngine(){
        final String state = "OverHeating Engine.";
        final String stateSpeech = "How to Fix OverHeating Engine.";
        final String overheatingOne = resources.getString(R.string.overHeating_One);
        final String overheatingTwo = resources.getString(R.string.overHeating_Two);
        final String overheatingThree = resources.getString(R.string.overHeating_Three);
        //speak
        toSpeech.speak(stateSpeech, TextToSpeech.QUEUE_FLUSH, null);

        //get image to show in toast
        imageView.setImageResource(R.mipmap.overheating);

        //show image in toast
        Toast toast = Toast.makeText(getActivity(), stateSpeech, Toast.LENGTH_LONG);
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
                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
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
                                toSpeech.stop();
                                niftyDialogBuilder.cancel();
                                //SPEAK TO USER
                                toSpeech.speak(overheatingTwo, TextToSpeech.QUEUE_FLUSH, null);
                                niftyDialogBuilder
                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                        .withTitle(state)
                                        .withTitleColor("#9dffffff")
                                        .withMessage(overheatingTwo)
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
                                                //SPEAK TO USER
                                                toSpeech.speak(overheatingThree, TextToSpeech.QUEUE_FLUSH, null);
                                                niftyDialogBuilder
                                                        .withIcon(getResources().getDrawable(R.mipmap.logologo))
                                                        .withTitle(state)
                                                        .withTitleColor("#9dffffff")
                                                        .withMessage(overheatingThree)
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
        }, 3500);
    }

    //function for user has'nt choose lorry fault
    private void chooseLorryFault(){
        chooseFault = "Please choose a lorry fault to jumpstart";
        //speak to user
        toSpeech.speak(chooseFault, TextToSpeech.QUEUE_FLUSH, null);
        /**
         * niftyDialogBuilder
         * {@link NiftyDialogBuilder}
         * */
        niftyDialogBuilder
                .withIcon(getResources().getDrawable(R.mipmap.logologo))
                .withTitle("Lorry Fault")
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
