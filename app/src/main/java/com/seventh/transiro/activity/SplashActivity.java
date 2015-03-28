package com.seventh.transiro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.seventh.transiro.R;
import com.seventh.transiro.util.PrefUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @InjectView(R.id.splash_logo)
    ImageView splash_logo;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        startAnimation();
    }

    private void startAnimation(){
        handler.postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                PrefUtil.clearPreferences(getApplicationContext());
                startApplication();

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startApplication() {
        if (PrefUtil.getLocationUSer(this) == null) {
            Intent welcome = new Intent(this, WelcomeActivity.class);
            startActivity(welcome);
        } else {
            Intent chat = new Intent(this, BusRowActivity.class);
            startActivity(chat);
        }

    }
}
