package com.example.familymapclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {
    FamilyModel familyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String eventId = getIntent().getStringExtra("EVENT_ID");

        if (eventId != null) {
            familyModel = (FamilyModel)getIntent().getSerializableExtra("FAMILY_MODEL");
        }

        Iconify.with(new FontAwesomeModule());

        MapFragment mapFragment = new MapFragment();
        mapFragment.setHideOptions(true);
        mapFragment.setFamilyModel(familyModel);
        mapFragment.setEventId(eventId);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mapFragment).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
