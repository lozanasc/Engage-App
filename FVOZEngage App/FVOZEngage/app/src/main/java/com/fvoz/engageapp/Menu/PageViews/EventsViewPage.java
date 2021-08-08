package com.fvoz.engageapp.Menu.PageViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.fvoz.engageapp.R;

public class EventsViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_view_page);

        Bundle bundle = getIntent().getExtras();

        TextView EventsPageViewTitle = findViewById(R.id.EventsPageViewTitle);
        TextView EventsPageViewDate = findViewById(R.id.EventsPageViewDate);
        TextView EventsPageViewDesc = findViewById(R.id.EventsPageViewDescription);
        TextView EventsPageViewBarTitle = findViewById(R.id.EditProfilePageBarTitle);

        if(bundle != null){
            String EventsViewTitle = bundle.getString("EventTitle");
            String EventsViewDate = bundle.getString("EventDate");
            String EventsViewDesc = bundle.getString("EventDesc");

            if(EventsViewTitle.length() > 15)
                EventsPageViewBarTitle.setText(EventsViewTitle.subSequence(0, 15));
            else
                EventsPageViewBarTitle.setText(EventsViewTitle);
            EventsPageViewTitle.setText(EventsViewTitle);
            EventsPageViewDate.setText(EventsViewDate);
            EventsPageViewDesc.setText(EventsViewDesc);
        }


    }

}