package com.tecksolke.jumpstart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    TextView tecksolKE,jumpStart;
    ImageView splash;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        jumpStart = findViewById(R.id.jumpstartsplash);
        tecksolKE = findViewById(R.id.tecksolke);
        spinner = findViewById(R.id.progressBar);
        splash = findViewById(R.id.splash_image);

        //set color to progress bar
        spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

        //create animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jumpstartransition);
        //start animation with the variables
        jumpStart.startAnimation(animation);
        tecksolKE.startAnimation(animation);
        spinner.startAnimation(animation);
        splash.startAnimation(animation);

        //start a new activity after splash screen
        final Intent intent = new Intent(this, Login.class);

        //start a thread to give a counter
        Thread timer = new Thread() {
            public void run() {
                try {
                    //give your delay timer here
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        //start the timer
        timer.start();
    }
}
