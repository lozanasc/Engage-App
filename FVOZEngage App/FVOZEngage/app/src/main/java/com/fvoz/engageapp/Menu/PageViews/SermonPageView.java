package com.fvoz.engageapp.Menu.PageViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SermonPageView extends AppCompatActivity {

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_page_view);
        TextView BarTitle = findViewById(R.id.EditProfilePageBarTitle);
        TextView PageTitle = findViewById(R.id.SermonViewPageTitle);
        TextView PageDate = findViewById(R.id.SermonViewPageDate);
        TextView PageUrl = findViewById(R.id.SermonViewPageDescription);
        Button Download = findViewById(R.id.SermonViewPageDownloadButton);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String SermonTitle = bundle.getString("SermonTitle");
            String SermonDuration = bundle.getString("SermonDuration");
            String SermonShortUrl = bundle.getString("SermonSecretUrl");
            if(SermonTitle.length() > 15)
                BarTitle.setText(SermonTitle.subSequence(0,15));
            else
                BarTitle.setText(SermonTitle);
            PageTitle.setText(SermonTitle);
            PageDate.setText(SermonDuration);
            PageUrl.setText(SermonShortUrl);
            YouTubePlayerView youTubePlayerView = findViewById(R.id.SermonPlayer);
            getLifecycle().addObserver(youTubePlayerView);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(SermonShortUrl.substring(SermonShortUrl.indexOf('=')+1), 0);
                }
            });
        }

        Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert bundle != null;
                String SermonShortUrl = bundle.getString("SermonSecretUrl");
                downloadVideo("{"+"\"url\":" + "\"" + SermonShortUrl.substring(SermonShortUrl.indexOf('=')+1) + "\"" +"}");
            }
        });
    }

    void downloadVideo(String payload){
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String savedPayload = payload;
        final String URL = sharedPrefs.getString("prefUrl", "default")+"/engage/db_api/v1/user/info";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objResponse = new JSONObject(response);
                    if(objResponse.getString("status") != null) {
                        Toast.makeText(getApplicationContext(), objResponse.getString("status"), Toast.LENGTH_LONG).show();
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
        }){
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