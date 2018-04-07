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

public class Car_Spinner_Faults extends Fragment {
    View view;
    NiftyDialogBuilder niftyDialogBuilder;
    TextToSpeech toSpeech;
    int result;
    Button buttonSpinner;
    AppCompatSpinner spinnerFaults;
    Resources resources;
    String[] carFaults;
    ArrayAdapter<String> adapter;
    ImageView imageView;

    public Car_Spinner_Faults() {
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
        view = inflater.inflate(R.layout.car_spinner_faults, container, false);

        //set String of car array here
        resources = getResources();
        carFaults = resources.getStringArray(R.array.car_faults);

        spinnerFaults = view.findViewById(R.id.SpinnerCarFaults);
        spinnerFaults.setBackgroundColor(Color.parseColor("#2A3342"));

        /*
         * Code for drop down options
         * */
        adapter = new ArrayAdapter<>(spinnerFaults.getContext(), android.R.layout.simple_spinner_item, carFaults);
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerFaults.setAdapter(adapter);
        buttonSpinner = view.findViewById(R.id.bcar_spinner_jumpstart);

        buttonSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCarProcessing();
            }
        });
        return view;
    }

    /**
     * Adapter faults call function
     */
    private void adapterCarProcessing() {
        if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Choose Car Faults")) {
            toSpeech.speak("Please choose a car fault to jumpstart", TextToSpeech.QUEUE_FLUSH, null);
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty Filter")) {
            toSpeech.stop();
            carFilter();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty GasLifts")) {
            toSpeech.stop();
            gasLifts();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty Antenna")) {
            toSpeech.stop();
            antennaFault();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty Cabin")) {
            toSpeech.stop();
            cabinFault();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty Bulbs")) {
            toSpeech.stop();
            nonHeadLights();
        } else if (spinnerFaults.getSelectedItem().toString().equalsIgnoreCase("Faulty sun roof")) {
            toSpeech.stop();
            sunRoof();
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
