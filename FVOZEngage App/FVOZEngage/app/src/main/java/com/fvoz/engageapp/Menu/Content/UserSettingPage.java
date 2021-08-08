package com.fvoz.engageapp.Menu.Content;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.Menu.PageViews.SettingsEditProfilePage;
import com.fvoz.engageapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserSettingPage extends AppCompatActivity {

    private ImageButton Back;
    private Button Logout;
    private Button Edit;
    private TextView Fullname;
    private TextView email;
    private ImageView profile;
    private String id;


    SharedPreferences sharedPrefs;
    SharedPreferences sharedLocalPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_page);

        userDataRequest();

        Back = findViewById(R.id.editProfileBackButton);
        Edit = findViewById(R.id.EditProfile);
        Logout = findViewById(R.id.ApplyChanges);

        Fullname = findViewById(R.id.UserProfileFullName);
        email = findViewById(R.id.UserProfileEmail);
        profile = findViewById(R.id.UserSettingProfilePicture);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEdit = new Intent(UserSettingPage.this, SettingsEditProfilePage.class);
                toEdit.putExtra("name", Fullname.getText());
                toEdit.putExtra("email", email.getText());
                toEdit.putExtra("id", getId());
                startActivity(toEdit);
                finish();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDashboard = new Intent(UserSettingPage.this, Dashboard.class);
                startActivity(toDashboard);
                finish();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clears token and email and the app's auth status
                sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
                sharedPrefs.edit().clear().apply();
                // Reroutes user to Login page
                Intent ToLogout = new Intent(UserSettingPage.this, Login.class);
                ToLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ToLogout);
                finish();
            }
        });
    }

    void userDataRequest(){
        sharedLocalPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
        final String savedPayload = ("{"+"\"user_email\":" + "\"" + sharedPrefs.getString("email", "default")+ "\"" +"}");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String URL = sharedLocalPrefs.getString("prefUrl", "default")+"/engage/db_api/v1/user/info";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objResponse = new JSONObject(response);
                    if (objResponse.getBoolean("status")) {
                        JSONArray data = objResponse.getJSONArray("data");
                        JSONObject info = data.getJSONObject(0);
                        Fullname.setText("Name: "+info.getString("user_name"));
                        email.setText("Email: "+info.getString("user_email"));
                        setId(info.getString("user_id"));
                        Picasso.get().load(sharedLocalPrefs.getString("prefUrl",null)+"/uploads/"+info.getString("user_profile")).into(profile);

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
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                Log.d("VolleyResponseError", "onErrorResponse: "+error.getMessage());
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
                String secret_token = sharedPrefs.getString("token", "default");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + secret_token);
                return headers;
            }

        };
        requestQueue.add(stringRequest);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}