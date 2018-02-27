package com.tecksolke.jumpstart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    Button btnlogin, btnregister, btnforgot;
    ImageView imageView;
    TextView tecksolKE;
    private ProgressBar spinner;
    EditText userName, userPassword;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnlogin = findViewById(R.id.blogin);
        btnregister = findViewById(R.id.bregister);
        btnforgot = findViewById(R.id.bforgotpassword);
        userName = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        spinner = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.userpiclogin);
        relativeLayout = findViewById(R.id.loginlayout);
        tecksolKE = findViewById(R.id.tecksolke);

        //set spinner color
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

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
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Category.class));
                finish();
            }
        });
        btnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(Login.this).setTitleText("TEST JUMPSTART").show();
            }
        });

    }
}
