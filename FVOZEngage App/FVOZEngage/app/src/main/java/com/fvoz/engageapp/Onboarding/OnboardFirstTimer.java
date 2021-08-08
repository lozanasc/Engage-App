package com.fvoz.engageapp.Onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.fvoz.engageapp.Forms.DevRequestURLForm;
import com.fvoz.engageapp.R;
import com.fvoz.engageapp.Splash.SplashScreen;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class OnboardFirstTimer extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_first_timer);

        fragmentManager = getSupportFragmentManager();
        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataforOnboarding());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // fragmentTransaction method is used
        // do all the transactions or changes
        // between different fragments
        fragmentTransaction.add(R.id.LayoutOfTheFrame, paperOnboardingFragment);

        // all the changes are committed
        fragmentTransaction.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent ToSplash = new Intent(OnboardFirstTimer.this, DevRequestURLForm.class);
                startActivity(ToSplash);
                finish();
            }
        }, 6500);

    }

    private ArrayList<PaperOnboardingPage> getDataforOnboarding() {

        // the first string is to show the main title ,
        // second is to show the message below the
        // title, then color of background is passed ,
        // then the image to show on the screen is passed
        // and at last icon to navigate from one screen to other
        PaperOnboardingPage Welcome = new PaperOnboardingPage("Welcome to FVOZEngage", " ", Color.parseColor("#FFFFFF"),R.drawable.fvoz_logo_light, R.drawable.current_board);
        PaperOnboardingPage BeUpToDate = new PaperOnboardingPage("Be up to date with the Events and Projects in Church", "Check out latest Events and on-going Projects here in the App", Color.parseColor("#FFFFFF"),R.drawable.update_illustration_board_2, R.drawable.current_board);
        PaperOnboardingPage SendRequests = new PaperOnboardingPage("Send requests via the FVOZ Engage App", "Requesting documents has never been EASIER!", Color.parseColor("#FFFFFF"),R.drawable.request_illustration_board_final, R.drawable.current_board);

        // array list is used to store
        // data of onbaording screen
        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();

        // all the sources(data to show on screens)
        // are added to array list
        elements.add(Welcome);
        elements.add(BeUpToDate);
        elements.add(SendRequests);
        return elements;
    }

}