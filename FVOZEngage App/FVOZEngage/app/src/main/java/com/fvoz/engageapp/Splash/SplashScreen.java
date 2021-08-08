package com.fvoz.engageapp.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.fvoz.engageapp.Forms.DevRequestURLForm;
import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.Onboarding.OnboardFirstTimer;
import com.fvoz.engageapp.R;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPrefs;
    SharedPreferences sharedAuthPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        sharedAuthPrefs = getSharedPreferences("localAuthStorage", MODE_PRIVATE);
        if(sharedPrefs.getString("prefUrl", null)!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ToSplash = new Intent(SplashScreen.this, Login.class);
                    startActivity(ToSplash);
                    finish();
                }
            }, 3000);
        } else if ((sharedPrefs.getString("prefUrl", null)==null) && (sharedAuthPrefs.getString("token", null)==null)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ToOnboard = new Intent(SplashScreen.this, OnboardFirstTimer.class);
                    startActivity(ToOnboard);
                    finish();
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ToSplash = new Intent(SplashScreen.this, DevRequestURLForm.class);
                    startActivity(ToSplash);
                    finish();
                }
            }, 3000);
        }
    }


}