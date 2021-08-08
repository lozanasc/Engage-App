package com.fvoz.engageapp.Menu.Content;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Splash.RequestSuccessSplash;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

public class RequestFormsPage extends AppCompatActivity {

    private Button SendRequestButton;
    private ImageButton Back;
    private TextInputLayout Subject;
    private TextInputLayout Note;
    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_forms_page);

        SendRequestButton = findViewById(R.id.RequestSubmitButton);
        Back = findViewById(R.id.BackButton);
        Subject = findViewById(R.id.RequestTypeInput);
        Note = findViewById(R.id.RequestNote);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToDashboard = new Intent(RequestFormsPage.this, Dashboard.class);
                startActivity(ToDashboard);
            }
        });

        SendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(Subject.getEditText()).getText().toString().length() != 0 &&
                        Objects.requireNonNull(Note.getEditText()).getText().toString().length() != 0) {
                    String payLoad = ("{" + "\"subject\":" + "\"" + Subject.getEditText().getText().toString() + "\"," +
                            "\"message\":" + "\"" + Note.getEditText().getText().toString() + "\"" + "}");
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MailRequest(payLoad);
                        }
                    }, 2000);

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void MailRequest(String payload){

        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        final String savedPayload = payload;
        final String URL = sharedPrefs.getString("prefUrl", "default")+"/engage/mail_api/v1/request/mail";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objResponse = new JSONObject(response);
                    boolean Success = objResponse.getBoolean("status");
                    if(Success){
                        Intent ToSuccess = new Intent(RequestFormsPage.this, RequestSuccessSplash.class);
                        startActivity(ToSuccess);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println(ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(error.getMessage());
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