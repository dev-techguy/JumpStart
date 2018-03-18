package com.tecksolke.jumpstart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class PasswordUpdated extends AppCompatActivity {

    static EditText resetUser, passReset, confirmReset;
    static Button bback, breset;
    static ProgressDialog progressDialog;
    NiftyDialogBuilder niftyDialogBuilder;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_updated);

        niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);

        //set a circular progress bar
        progressDialog = new ProgressDialog(PasswordUpdated.this, R.style.MyAlertDialogStyle);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("JumpStart Processing...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.hide();

        //get components
        resetUser = findViewById(R.id.usernameReset);
        passReset = findViewById(R.id.passwordReset);
        confirmReset = findViewById(R.id.confirmpasswordReset);
        breset = findViewById(R.id.bReset);
        bback = findViewById(R.id.bBack);
        relativeLayout = findViewById(R.id.layoutPasswordReseting);

        //create animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jumpstartransition);
        //start animation with the variables
        relativeLayout.startAnimation(animation);

        //start a thread to give a counter
        Thread timer = new Thread() {
            public void run() {
                try {
                    //give your delay timer here
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //do nothing
                }
            }
        };
        //start the timer
        timer.start();

        //go back to the login
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordUpdated.this, Login.class));
                finish();
            }
        });
    }

    //set method for resenting password
    public void userPasswordUpdate(View view) {
        if (resetUser.getText().toString().equalsIgnoreCase("")) {
            resetUser.setError("Please Enter Username");
        }
        if (passReset.getText().toString().equalsIgnoreCase("")) {
            passReset.setError("Please Enter New Password");
        }
        if (confirmReset.getText().toString().equalsIgnoreCase("")) {
            confirmReset.setError("Please Enter Confirm Password");
        } else {
            if (confirmReset.getText().toString().length() < 8 || passReset.getText().toString().length() < 8) {
                passReset.setError("You must have 8 characters in your password");
                confirmReset.setError("You must have 8 characters in your password");
            } else {
                if (!confirmReset.getText().toString().equalsIgnoreCase(passReset.getText().toString())) {
                    niftyDialogBuilder
                            .withIcon(getResources().getDrawable(R.mipmap.logologo))
                            .withTitle("Password Validation")
                            .withTitleColor("#9dffffff")
                            .withMessage("Passwords Don't Match")
                            .withMessageColor("#9dffffff")
                            .withDialogColor("#2A3342")
                            .withButton1Text("OK")
                            .withDuration(700)
                            .isCancelable(false)
                            .withEffect(Effectstype.RotateBottom)
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    niftyDialogBuilder.cancel();
                                }
                            })
                            .show();
                } else {
                    checkConnection(this);
                }
            }
        }
    }

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public void checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            //if connection is true
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String username = resetUser.getText().toString();
                String userpassowrd = passReset.getText().toString();

                //call the function for processing
                PasswordUpdate_Processing passwordUpdate_processing = new PasswordUpdate_Processing(this);
                passwordUpdate_processing.execute(username, userpassowrd);
            }
        } else {
            niftyDialogBuilder
                    .withIcon(getResources().getDrawable(R.mipmap.logologo))
                    .withTitle("Network Status")
                    .withTitleColor("#9dffffff")
                    .withMessage("Please Connect To A Network To Proceed")
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("OK")
                    .withDuration(700)
                    .isCancelable(false)
                    .withEffect(Effectstype.Shake)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            niftyDialogBuilder.cancel();
                        }
                    })
                    .show();
        }
    }
}
