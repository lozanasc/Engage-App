package com.fvoz.engageapp.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

    public class Login extends AppCompatActivity {

    private TextView ErrorPrompt;
    private TextInputLayout EmailInput;
    private TextInputLayout PasswordInput;
    private ImageView secret;

    SharedPreferences sharedPrefs;
    SharedPreferences sharedAuthPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        sharedAuthPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);

        if(sharedPrefs.getString("prefUrl", null) == null){
            Intent toDevForm = new Intent(Login.this, DevRequestURLForm.class);
            startActivity(toDevForm);
            finish();
        } else if(sharedAuthPrefs.getString("token", null) != null) {
            Intent ToDashboard = new Intent(Login.this, Dashboard.class);
            ToDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ToDashboard);
            finish();
        }

        ErrorPrompt = findViewById(R.id.OutputPromptLabel);
        secret = findViewById(R.id.FVOZLoginLogo);
        EmailInput = findViewById(R.id.urlFormInput);
        PasswordInput = findViewById(R.id.PasswordInput);

        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefs.getString("prefUrl", null) == null){
                    Intent toDevForm = new Intent(Login.this, DevRequestURLForm.class);
                    startActivity(toDevForm);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "URL detected no need! "+sharedPrefs.getString("prefUrl", null), Toast.LENGTH_LONG).show();
                }
            }
        });
        Button loginTest = findViewById(R.id.ButtonLogin);
        loginTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Weakest type of form validation
                if (Objects.requireNonNull(EmailInput.getEditText()).getText().toString().length() != 0 &&
                        Objects.requireNonNull(PasswordInput.getEditText()).getText().toString().length() != 0) {
                    String payLoad = ("{"+"\"email\":" + "\"" + EmailInput.getEditText().getText().toString() + "\"," +
                            "\"password\":" + "\"" + PasswordInput.getEditText().getText().toString() + "\"" +  "}");
                    LoginRequest(payLoad);
                } else {
                    ErrorPrompt.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ErrorPrompt.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            }
        });
        TextView toRegister = findViewById(R.id.ToRegisterButton);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToRegister = new Intent(Login.this, Register.class);
                startActivity(ToRegister);
                finish();
            }
        });
    }
        private void LoginRequest(String payload){
            sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
            final String savedPayload = payload;
            final String URL = sharedPrefs.getString("prefUrl", "default")+"/engage/db_api/v1/user/auth";

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objResponse = new JSONObject(response);
                        if(objResponse.getBoolean("auth_status")){
                                String userEmail = objResponse.getString("email");
                                String userToken = objResponse.getString("token");
                                saveAuthInfo(userToken, userEmail);

                                Toast.makeText(getApplicationContext(), objResponse.getString("auth_desc"), Toast.LENGTH_LONG).show();

                                Intent ToDashboard = new Intent(Login.this, Dashboard.class);
                                startActivity(ToDashboard);
                                finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException ex) {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorPrompt.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ErrorPrompt.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return savedPayload == null ? null : savedPayload.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);
        }

    private void saveAuthInfo(String token, String email){
        sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
        saveToken(token);
        saveEmail(email);
        Objects.requireNonNull(EmailInput.getEditText()).setText("");
        Objects.requireNonNull(PasswordInput.getEditText()).setText("");
    }

    private void saveToken(String token) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void saveEmail(String email) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("email", email);
        editor.apply();
    }
}