package com.fvoz.engageapp.Menu.PageViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64OutputStream;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.Menu.Content.UserSettingPage;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SettingsEditProfilePage extends AppCompatActivity {

    private ImageButton back;
    private ImageView editProfile;
    private Button acceptChanges;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    String id;

    SharedPreferences sharedPrefs;
    SharedPreferences sharedAuthPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_edit_profile_page);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            id = bundle.getString("id");
        } else {
            Toast.makeText(getApplicationContext(), "Bundle empty, something went wrong!", Toast.LENGTH_LONG).show();
        }

        EditText newName, newEmail, newContact, newPassword;

        back = findViewById(R.id.editProfileBackButton);
        acceptChanges = findViewById(R.id.ApplyChanges);
        editProfile = findViewById(R.id.userEditProfilePicture);

        newName = findViewById(R.id.NewNameInput);
        newEmail = findViewById(R.id.NewEmailInput);
        newContact = findViewById(R.id.NewContactInput);
        newPassword = findViewById(R.id.NewPasswordInput);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToDashboard = new Intent(SettingsEditProfilePage.this, Dashboard.class);
                startActivity(ToDashboard);
                finish();
            }
        });

        acceptChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String payLoad = ("{"+"\"id\":" + "\"" + id + "\"," +
                                "\"email\":" + "\"" + newEmail.getText() + "\"," +
                                "\"name\":" + "\"" + newName.getText() + "\"," +
                                "\"contact\":" + "\"" + newContact.getText() + "\"," +
                                "\"password\":" + "\"" + newPassword.getText() + "\"" +  "}");
                System.out.println(payLoad);
                UpdateInfo(payLoad);

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage = new Intent();
                getImage.setType("image/*");
                getImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getImage, IMG_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                editProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String imageConverter(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageToBytes = byteArrayOutputStream.toByteArray();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(imageToBytes);
        }

        return null;
    }

    private void UpdateInfo(String payload){

        final String savedPayload = payload;
        System.out.println(savedPayload);
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        final String URL = sharedPrefs.getString("prefUrl", "default")+"/engage/db_api/v1/user/update_mobile";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objResponse = new JSONObject(response);
                    if (objResponse.getString("update_desc") != null) {
                        // Clears token and email and the app's auth status
                        sharedAuthPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
                        sharedAuthPrefs.edit().clear().apply();
                        // Reroutes user to Login page
                        Intent ToLogout = new Intent(SettingsEditProfilePage.this, Login.class);
                        ToLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ToLogout);
                        finish();
                        Toast.makeText(getApplicationContext(), objResponse.getString("update_desc"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                    System.out.println(ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Error" + error, Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
            }
        }) {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMapParams = new HashMap<String, String>();
//                hashMapParams.put("file", imageConverter(bitmap));
//                hashMapParams.put("id", id);
//                hashMapParams.put("name", newName.getText().toString());
//                hashMapParams.put("email", newEmail.getText().toString());
//                hashMapParams.put("contact", newContact.getText().toString());
//                hashMapParams.put("password", newPassword.getText().toString());
//                System.out.println("Hashmap:" + hashMapParams);
//                return super.getParams();
//            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                sharedAuthPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
                String secret_token = sharedAuthPrefs.getString("token", null);
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + secret_token);
                return headers;
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