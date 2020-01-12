package com.example.android.moviesapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.android.moviesapp.R;

import gr.net.maroulis.library.EasySplashScreen;

// Display Splash Screen
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create obj from EasySplashScreen class
        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(1000)
                .withBackgroundColor(Color.parseColor("#1a1b29"))
                .withAfterLogoText("MoviesApp")
                .withLogo(R.drawable.logo);

        config.getAfterLogoTextView().setTextColor(Color.GRAY);

        View easySplashScreen = config.create();
        // Set ContentView method on easySplashScreen view
        setContentView(easySplashScreen);
    }
}