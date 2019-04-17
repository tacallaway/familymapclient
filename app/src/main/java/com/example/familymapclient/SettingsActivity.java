package com.example.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SettingsData settings = (SettingsData)getIntent().getSerializableExtra("SETTINGS");

        if (settings != null) {
            ((Spinner)findViewById(R.id.mapType)).setSelection(settings.mapType);
            ((Spinner)findViewById(R.id.storyLineColor)).setSelection(settings.storyLineColor);
            ((Switch)findViewById(R.id.storyLineSwitch)).setChecked(settings.storyLines);
            ((Spinner)findViewById(R.id.treeLineColor)).setSelection(settings.familyTreeLinesColor);
            ((Switch)findViewById(R.id.treeLineSwitch)).setChecked(settings.familyTreeLines);
            ((Spinner)findViewById(R.id.spouseLineColor)).setSelection(settings.spouseLineColor);
            ((Switch)findViewById(R.id.spouseLineSwitch)).setChecked(settings.spouseLines);
        } else {
            ((Spinner)findViewById(R.id.storyLineColor)).setSelection(1);
            ((Spinner)findViewById(R.id.treeLineColor)).setSelection(2);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                SettingsData sd = new SettingsData();
                sd.mapType = ((Spinner)findViewById(R.id.mapType)).getSelectedItemPosition();
                sd.storyLineColor = ((Spinner)findViewById(R.id.storyLineColor)).getSelectedItemPosition();
                sd.storyLines = ((Switch)findViewById(R.id.storyLineSwitch)).isChecked();
                sd.familyTreeLinesColor = ((Spinner)findViewById(R.id.treeLineColor)).getSelectedItemPosition();
                sd.familyTreeLines = ((Switch)findViewById(R.id.treeLineSwitch)).isChecked();
                sd.spouseLineColor = ((Spinner)findViewById(R.id.spouseLineColor)).getSelectedItemPosition();
                sd.spouseLines = ((Switch)findViewById(R.id.spouseLineSwitch)).isChecked();
                intent.putExtra("SETTINGS", sd);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
