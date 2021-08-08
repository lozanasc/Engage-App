package com.fvoz.engageapp.Menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fvoz.engageapp.Forms.Login;
import com.fvoz.engageapp.Menu.Content.ChurchProjectListPage;
import com.fvoz.engageapp.Menu.Content.RequestFormsPage;
import com.fvoz.engageapp.Menu.Content.SeminarsAndEventListPage;
import com.fvoz.engageapp.Menu.Content.SermonsListPage;
import com.fvoz.engageapp.Menu.Content.UserSettingPage;
import com.fvoz.engageapp.R;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    NavigationView navView;
    ActionBarDrawerToggle navToggle;
    DrawerLayout navDrawer;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(checkLoginStatus())
            Log.d("AuthCheck", "onCreate: No worries!");
        else {
            Intent RerouteLogin = new Intent(Dashboard.this, Login.class);
            startActivity(RerouteLogin);
            finish();
        }

        CardView Events, Sermons, Projects, Requests;

        Events = findViewById(R.id.SeminarsAndEventsButton);
        Sermons = findViewById(R.id.SermonsButton);
        Projects = findViewById(R.id.ChurchProjectsButton);
        Requests = findViewById(R.id.RequestFormsButton);

        Toolbar navToolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            navToolbar = (Toolbar)findViewById(R.id.dashboard_toolbar);
        }
        setSupportActionBar(navToolbar);

        navView = (NavigationView)findViewById(R.id.dashboard_nav_menu);
        navDrawer = (DrawerLayout)findViewById(R.id.dashboard_drawer);

        navToggle = new ActionBarDrawerToggle(this, navDrawer, navToolbar, R.string.drawer_status_open, R.string.drawer_status_close);
        navDrawer.addDrawerListener(navToggle);
        navToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.menu_sermons:
                        Intent ToSermon = new Intent(Dashboard.this, SermonsListPage.class);
                        startActivity(ToSermon);
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_projects:
                        Intent ToProjects = new Intent(Dashboard.this, ChurchProjectListPage.class);
                        startActivity(ToProjects);
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_requests:
                        Intent ToRequests = new Intent(Dashboard.this, RequestFormsPage.class);
                        startActivity(ToRequests);
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_events:
                        Intent ToEvents = new Intent(Dashboard.this, SeminarsAndEventListPage.class);
                        startActivity(ToEvents);
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.menu_settings:
                        Intent ToSettings = new Intent(Dashboard.this, UserSettingPage.class);
                        startActivity(ToSettings);
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;


                    default:
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        navDrawer.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToEvents = new Intent(Dashboard.this, SeminarsAndEventListPage.class);
                startActivity(ToEvents);
            }
        });

        Sermons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToSermon = new Intent(Dashboard.this, SermonsListPage.class);
                startActivity(ToSermon);
                navDrawer.closeDrawer(GravityCompat.START);
            }
        });

        Projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToProjects = new Intent(Dashboard.this, ChurchProjectListPage.class);
                startActivity(ToProjects);
            }
        });

        Requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToRequests = new Intent(Dashboard.this, RequestFormsPage.class);
                startActivity(ToRequests);
            }
        });
    }

    boolean checkLoginStatus(){
        sharedPrefs =  getSharedPreferences("localAuthStorage", MODE_PRIVATE);
        return sharedPrefs.getString("token", null) != null;
    }

}