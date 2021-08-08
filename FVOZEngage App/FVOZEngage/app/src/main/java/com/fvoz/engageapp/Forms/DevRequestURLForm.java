package com.fvoz.engageapp.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fvoz.engageapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class DevRequestURLForm extends AppCompatActivity {

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_request_urlform);

        TextInputLayout URLInput = findViewById(R.id.urlFormInput);
        Button Proceed = findViewById(R.id.proceedBtn);

        Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRequestUrlPref(URLInput.getEditText().getText().toString());
                Intent toMain = new Intent(DevRequestURLForm.this, Login.class);
                toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toMain);
                finish();
            }
        });
    }

    void saveRequestUrlPref(String Url){
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        saveUrl(Url);
    }

    private void saveUrl(String url) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("prefUrl", url);
        editor.apply();
    }

}