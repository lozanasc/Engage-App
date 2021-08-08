package com.fvoz.engageapp.Menu.PageViews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.fvoz.engageapp.R;
import com.squareup.picasso.Picasso;

public class ProjectsViewPage extends AppCompatActivity {


    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_view_page);

        sharedPref =  getSharedPreferences("localPrefStorage", MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();

        TextView ProjectViewMenuBarTitle = findViewById(R.id.EditProfilePageBarTitle);
        TextView ProjectViewTitle = findViewById(R.id.ProjectPageViewTitle);
        TextView ProjectViewDate = findViewById(R.id.ProjectPageViewDate);
        TextView ProjectViewDescription = findViewById(R.id.ProjectViewPageDescription);
        ImageView ProjectImage = findViewById(R.id.ProjectPagePhoto);

        if(bundle != null){
            String ViewPageTitle = bundle.getString("ProjectTitle");
            String ViewPageDatePosted = bundle.getString("ProjectDatePosted");
            String ViewPageDescription = bundle.getString("ProjectUpdateDesc");
            String ViewImage = bundle.getString("ProjectImage");

            if(ViewPageTitle.length() > 15)
                ProjectViewMenuBarTitle.setText(ViewPageTitle.subSequence(0, 14));
            else
                ProjectViewMenuBarTitle.setText(ViewPageTitle);

            ProjectViewTitle.setText(ViewPageTitle);
            ProjectViewDate.setText(ViewPageDatePosted);
            ProjectViewDescription.setText(ViewPageDescription);
            Picasso.get().load(sharedPref.getString("prefUrl", "default")+"/uploads/"+ViewImage).into(ProjectImage);
        }
    }
}