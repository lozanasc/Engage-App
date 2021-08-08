package com.fvoz.engageapp.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.R;

public class RegistrationSuccessSplash extends AppCompatActivity {

    private TextView SuccessToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success_splash);
        SuccessToLogin = findViewById(R.id.SuccessToLogin);
        SuccessToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackToLogin = new Intent(RegistrationSuccessSplash.this, Login.class);
                startActivity(BackToLogin);
                finish();
            }
        });
    }
}