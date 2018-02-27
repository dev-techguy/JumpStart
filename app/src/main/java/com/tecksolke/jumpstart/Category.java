package com.tecksolke.jumpstart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class Category extends AppCompatActivity {

    private ProgressBar spinnerlorry, spinnercar, spinnerpickup;
    CardView carCard, lorryCard, pickupCard;
    RelativeLayout relativeLayout;
    Button btnlorry,btnpickup,btncar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        spinnerlorry = findViewById(R.id.progressBarlorry);
        spinnerpickup = findViewById(R.id.progressBarpickups);
        spinnercar = findViewById(R.id.progressBarCar);
        lorryCard = findViewById(R.id.cardlorry);
        pickupCard = findViewById(R.id.cardpickup);
        carCard = findViewById(R.id.cardcar);
        relativeLayout = findViewById(R.id.design_bottom_sheet);
        btncar = findViewById(R.id.bcar);
        btnlorry = findViewById(R.id.blorry);
        btnpickup = findViewById(R.id.bpickups);

        //set spinner color
        spinnerpickup.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinnercar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);
        spinnerlorry.getIndeterminateDrawable().setColorFilter(Color.parseColor("#9dffffff"), android.graphics.PorterDuff.Mode.MULTIPLY);

        //create animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.jumpstartransition);
        //start animation with the variables
        lorryCard.startAnimation(animation);
        pickupCard.startAnimation(animation);
        carCard.startAnimation(animation);
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

                }
            }
        };
        //start the timer
        timer.start();

        btnlorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Category.this,LorryJumpStart.class));
            }
        });
    }
}
