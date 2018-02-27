package com.tecksolke.jumpstart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText firstName, sirName, Email, userName, userPassowrd, conpassword;
    Button btnRegister, btnLogin;
    private ProgressBar spinner;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        spinner = findViewById(R.id.progressBar);
        //set spinner color
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

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

    public void processNewUser(View view) {
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
        }
    }
}
