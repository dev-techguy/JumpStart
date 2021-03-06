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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

public class Login extends AppCompatActivity {

    static Button btnlogin, btnregister, btnforgot;
    static ImageView imageView;
    static TextView tecksolKE;
    static private ProgressBar spinner;
    static EditText userName, userPassword;
    RelativeLayout relativeLayout;
    static ProgressDialog progressDialog;
    NiftyDialogBuilder niftyDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);

        //set a circular progress bar
        progressDialog = new ProgressDialog(Login.this,R.style.MyAlertDialogStyle);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#364155")));
        progressDialog.setMessage("JumpStart Processing...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.hide();

        btnlogin = findViewById(R.id.blogin);
        btnregister = findViewById(R.id.bregister);
        btnforgot = findViewById(R.id.bforgotpassword);
        userName = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        //spinner = findViewById(R.id.progressBarLogin);
        imageView = findViewById(R.id.userpiclogin);
        relativeLayout = findViewById(R.id.loginlayout);
        tecksolKE = findViewById(R.id.tecksolke);

        //set spinner color
//        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

        //animate the login layout
        //create animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jumpstartransition);
        //start animation with the variables
        imageView.startAnimation(animation);
        userName.startAnimation(animation);
        userPassword.startAnimation(animation);
        btnlogin.startAnimation(animation);
        btnregister.startAnimation(animation);
        btnforgot.startAnimation(animation);
        tecksolKE.startAnimation(animation);
        // relativeLayout.startAnimation(animation);

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

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
        btnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start resenting page
                startActivity(new Intent(Login.this, PasswordUpdated.class));
                finish();
            }
        });

    }

    //process login
    public void loginProcess(View view) {
        if (userPassword.getText().toString().equalsIgnoreCase("")) {
            userPassword.setError("Please enter your password");
        }
        if (userName.getText().toString().equalsIgnoreCase("")) {
            userName.setError("Please enter your username");
        } else {
            if (userName.getText().toString().equalsIgnoreCase("") || userPassword.getText().toString().equalsIgnoreCase("")) {
                //do nothing
            } else {
                checkConnection(this);
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
                String username = userName.getText().toString();
                String userpassowrd = userPassword.getText().toString();

                //call the function for processing
                Login_Processing login_processing = new Login_Processing(this);
                login_processing.execute(username, userpassowrd);
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
