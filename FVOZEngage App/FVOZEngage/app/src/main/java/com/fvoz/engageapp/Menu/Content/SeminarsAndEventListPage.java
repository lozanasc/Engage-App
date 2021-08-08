package com.fvoz.engageapp.Menu.Content;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fvoz.engageapp.Menu.Dashboard;
import com.fvoz.engageapp.Menu.PageViews.EventsViewPage;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Utilities.EventsTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeminarsAndEventListPage extends AppCompatActivity {

    private ListView SeminarAndEventList;
    private List<EventsTemplate> EventList;
    private SeminarAndEventAdapters eventAdapter;
    SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminars_and_event_list_page);

        ImageButton BackButton = findViewById(R.id.EventsBackButton);

        SeminarAndEventList = findViewById(R.id.SeminarAndEventsList);

        EventList = new ArrayList<>();

        GetEvents();

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BackToDashboard = new Intent(SeminarsAndEventListPage.this, Dashboard.class);
                startActivity(BackToDashboard);
            }
        });
    }

    private void GetEvents(){
        sharedPrefs =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);
        String requestURL = sharedPrefs.getString("prefUrl", "default")+"/engage/fb_api/v1/events/events";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resultObj = new JSONObject(response);
                    JSONObject result = resultObj.getJSONObject("result");
                    JSONArray data = result.getJSONArray("data");

                    for(int i=0; i < data.length(); i++){
                        JSONObject dataObj = data.getJSONObject(i);

                        EventsTemplate EventObject = new EventsTemplate();

                        EventObject.setEventTitle(dataObj.getString("name"));
                        EventObject.setEventDate(dataObj.getString("start_time"));
                        EventObject.setEventDescription(dataObj.getString("description"));

                        EventList.add(EventObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            eventAdapter = new SeminarAndEventAdapters();
            SeminarAndEventList.setAdapter(eventAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SeminarsAndEventListPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public class SeminarAndEventAdapters extends BaseAdapter {

        @Override
        public int getCount() { return EventList.size(); }

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
            convertView = getLayoutInflater().inflate(R.layout.seminar_event_card, parent, false);
            TextView EventTitle = convertView.findViewById(R.id.EventTitle);
            TextView EventDate = convertView.findViewById(R.id.EventDate);
            TextView EventDesc = convertView.findViewById(R.id.EventDesc);

            EventTitle.setText(EventList.get(position).getEventTitle());
            EventDate.setText(EventList.get(position).getEventDate());
            EventDesc.setText(EventList.get(position).getEventDescription());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toViewPage = new Intent(SeminarsAndEventListPage.this, EventsViewPage.class);
                    toViewPage.putExtra("EventTitle", EventTitle.getText());
                    toViewPage.putExtra("EventDate", EventDate.getText());
                    toViewPage.putExtra("EventDesc", EventDesc.getText());
                    startActivity(toViewPage);
                }
            });

            return convertView;
        }
    }
}