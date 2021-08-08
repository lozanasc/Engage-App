package com.fvoz.engageapp.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Splash.RegistrationSuccessSplash;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class Register extends AppCompatActivity {

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextInputLayout email, full_name, contact, confirm_password;

        email = findViewById(R.id.EditEmailInput);
        full_name = findViewById(R.id.EditFullnameInput);
        contact = findViewById(R.id.EditContactInput);
        confirm_password = findViewById(R.id.EditPasswordInput);

        TextView toLogin = findViewById(R.id.ToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToLogin = new Intent(Register.this, Login.class);
                startActivity(ToLogin);
            }
        });

        Button register = findViewById(R.id.RegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is only for testing/prototyping...
                System.out.println("We've reached this func");
                // Some sort of form validation lol
                if(Objects.requireNonNull(email.getEditText()).getText().toString().length() != 0 &&
                        Objects.requireNonNull(full_name.getEditText()).getText().toString().length() != 0 &&
                        Objects.requireNonNull(contact.getEditText()).getText().toString().length() != 0 &&
                        Objects.requireNonNull(confirm_password.getEditText()).getText().toString().length() != 0) {
                            // String as a valid json payload
                            String payLoad = ("{"+"\"email\":" + "\"" + email.getEditText().getText().toString() + "\"," +
                                             "\"name\":" + "\"" + full_name.getEditText().getText().toString() + "\"," +
                                             "\"contact\":" + "\"" + contact.getEditText().getText().toString() + "\"," +
                                             "\"password\":" + "\"" + confirm_password.getEditText().getText().toString() + "\"" +  "}");
                            RegisterRequest(payLoad);
                            // Clears the TextInputLayouts
                            email.getEditText().setText("");
                            full_name.getEditText().setText("");
                            contact.getEditText().setText("");
                            confirm_password.getEditText().setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Missing fields!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void RegisterRequest(String payload){
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        final String savedPayload = payload;
        final String URL = sharedPrefs.getString("prefUrl", "default")+"/engage/db_api/v1/user/register";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objResponse = new JSONObject(response);
                    if(objResponse.toString().length() != 0) {
                        Intent SuccessIntent = new Intent(Register.this, RegistrationSuccessSplash.class);
                        startActivity(SuccessIntent);
                        finish();
                        Toast.makeText(getApplicationContext(), objResponse.getString("register_desc"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
}