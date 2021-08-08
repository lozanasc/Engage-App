package com.fvoz.engageapp.Menu.Content;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.Menu.PageViews.SermonPageView;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Utilities.SermonTemplate;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SermonsListPage extends AppCompatActivity {

      List<SermonTemplate> SermonList;
      ListView SermonListViews;
      SermonListAdapter adapterSermonList;
      SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermons_page);
        SermonListViews = (ListView) findViewById(R.id.SermonListView);
        SermonList = new ArrayList<>();
        GetSermonsFromPlaylist();

        ImageButton BackButton = findViewById(R.id.SermonBackButton);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackToDashboard = new Intent(SermonsListPage.this, Dashboard.class);
                startActivity(BackToDashboard);
            }
        });
    }



    private void GetSermonsFromPlaylist() {
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        String RequestURL = sharedPrefs.getString("prefUrl", "default")+"/engage/yt_api/v1/sermon/sermons";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject SermonRequestResult = response.getJSONObject(i);
                        SermonTemplate SermonObject = new SermonTemplate();
                        SermonObject.setSermonTitle(SermonRequestResult.getString("title"));
                        SermonObject.setDuration(SermonRequestResult.getString("duration"));
                        JSONObject ThumbnailObject = SermonRequestResult.getJSONObject("bestThumbnail");
                        SermonObject.setThumbnailURL(ThumbnailObject.getString("url"));
                        SermonObject.setSermonShortURL(SermonRequestResult.getString("shortUrl"));
                        SermonList.add(SermonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterSermonList = new SermonListAdapter();
                SermonListViews.setAdapter(adapterSermonList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SermonsListPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public class SermonListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return SermonList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.sermon_card, parent, false);

            TextView SermonTitle = convertView.findViewById(R.id.SermonTitle);
            TextView SermonDuration = convertView.findViewById(R.id.SermonDuration);
            TextView SermonSecretUrl = convertView.findViewById(R.id.SermonShortURL);
            ImageView SermonThumbnail = convertView.findViewById(R.id.SermonPhoto);

            SermonTitle.setText(SermonList.get(position).getSermonTitle());
            SermonDuration.setText(SermonList.get(position).getDuration());
            SermonSecretUrl.setText(SermonList.get(position).getSermonShortURL());
            Picasso.get().load(SermonList.get(position).getThumbnailURL()).into(SermonThumbnail);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SermonsListPage.this, SermonPageView.class);
                    intent.putExtra("SermonTitle", SermonTitle.getText());
                    intent.putExtra("SermonDuration", SermonDuration.getText());
                    intent.putExtra("SermonSecretUrl", SermonSecretUrl.getText());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}