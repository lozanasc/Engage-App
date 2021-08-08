package com.fvoz.engageapp.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.R;

public class RequestSuccessSplash extends AppCompatActivity {

    private TextView RedirectSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_success_splash);

        RedirectSuccess = findViewById(R.id.SuccessRedirectMenu);

        RedirectSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RedirectMenu = new Intent(RequestSuccessSplash.this, Dashboard.class);
                startActivity(RedirectMenu);
            }
        });

    }
}