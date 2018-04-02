package com.tecksolke.jumpstart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class Register extends AppCompatActivity {

    static EditText firstName, sirName, Email, userName, userPassowrd, conpassword;
    static Button btnRegister, btnLogin;
    static ProgressBar spinner;
    RelativeLayout relativeLayout;
    NiftyDialogBuilder niftyDialogBuilder;
    static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //alert
        niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);

        //set a circular progress bar
        progressDialog = new ProgressDialog(Register.this, R.style.MyAlertDialogStyle);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("JumpStart Processing...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.hide();

        //call the layout components
        firstName = findViewById(R.id.Fname);
        sirName = findViewById(R.id.Sname);
        Email = findViewById(R.id.email);
        userName = findViewById(R.id.rusername);
        userPassowrd = findViewById(R.id.rpassword);
        conpassword = findViewById(R.id.confirmpassword);
        relativeLayout = findViewById(R.id.registerlayout);
        //buttons
        btnLogin = findViewById(R.id.register_login_button);
        btnRegister = findViewById(R.id.register_button);
        //spinner
//        spinner = findViewById(R.id.progressBarRegister);
//        spinner.setVisibility(View.GONE);
//        //set spinner color
//        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

        //animate the login layout
        //create animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jumpstartransition);
        //start animation with the variables
        relativeLayout.startAnimation(animation);

        //start a thread to give a counter
        Thread timer = new Thread() {
            public void run() {
                try {
                    //give your delay timer here
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //do nothing
                }
            }
        };
        //start the timer
        timer.start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

    }

    //function for executing data
    public void processNewUser(View view) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (firstName.getText().toString().equalsIgnoreCase("")) {
            firstName.setError("Enter your First Name");
        }
        if (sirName.getText().toString().equalsIgnoreCase("")) {
            sirName.setError("Enter your Sir Name");
        }
        if (Email.getText().toString().equalsIgnoreCase("")) {
            Email.setError("Enter your Email Address i.e myemail@gmail.com");
        }
        if (userName.getText().toString().equalsIgnoreCase("")) {
            userName.setError("Enter your UserName");
        }
        if (userPassowrd.getText().toString().equalsIgnoreCase("")) {
            userPassowrd.setError("Enter your Password");
        }
        if (conpassword.getText().toString().equalsIgnoreCase("")) {
            conpassword.setError("Confirm Password");
        } else {
            EditText[] userdetails = {
                    firstName = findViewById(R.id.Fname),
                    sirName = findViewById(R.id.Sname),
                    Email = findViewById(R.id.email),
                    userName = findViewById(R.id.rusername),
                    userPassowrd = findViewById(R.id.rpassword),
                    conpassword = findViewById(R.id.confirmpassword),
            };

            int verified = 0;
            for (EditText aTestEmpty : userdetails) {
                if (aTestEmpty.getText().toString().equalsIgnoreCase("")) {
                    verified = 1;
                    break;
                }
            }
            if (verified == 0) {
                if (conpassword.getText().toString().length() < 8 || userPassowrd.getText().toString().length() < 8) {
                    userPassowrd.setError("You must have 8 characters in your password");
//                    conpassword.setError("You must have 8 characters in your password");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()) {
                    Email.setError("Please Enter A valid Email");
                } else {
                    if (!conpassword.getText().toString().equalsIgnoreCase(userPassowrd.getText().toString())) {
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
                String firstname = firstName.getText().toString();
                String sirname = sirName.getText().toString();
                String email = Email.getText().toString();
                String username = userName.getText().toString();
                String userpassowrd = userPassowrd.getText().toString();

                //call the function for processing
                Registration_Processing registration_processing = new Registration_Processing(this);
                registration_processing.execute(firstname, sirname, email, username, userpassowrd);
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
