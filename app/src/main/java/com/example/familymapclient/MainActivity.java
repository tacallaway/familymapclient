package com.example.familymapclient;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    LoginFragment loginFragment = new LoginFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Iconify.with(new FontAwesomeModule());

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        loginFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).commit();
    }

    public void showMapFragment(FamilyModel familyModel) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setFamilyModel(familyModel);

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        mapFragment.setArguments(getIntent().getExtras());

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();

        tran.remove(loginFragment);
        tran.add(R.id.fragment_container, mapFragment).commit();
    }
}
