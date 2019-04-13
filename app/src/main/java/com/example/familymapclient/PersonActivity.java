package com.example.familymapclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonActivity extends AppCompatActivity {
    FamilyModel familyModel;
    ExpandableListView expandableListView;
    ExpandableListData expandableListData;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String personId = getIntent().getStringExtra("PERSON_ID");
        FamilyModel familyModel = (FamilyModel)getIntent().getSerializableExtra("FAMILY_MODEL");

        FamilyModel.Person person = familyModel.getPerson(personId);

        ((TextView)findViewById(R.id.textPersonFirstName)).setText(person.getFirstName());
        ((TextView)findViewById(R.id.textPersonLastName)).setText(person.getLastName());
        ((TextView)findViewById(R.id.textPersonGender)).setText(person.getGender().equals("m") ? "Male" : "Female");

        expandableListView = findViewById(R.id.familyList);
        expandableListDetail = ExpandableListData.getData(personId, familyModel);
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
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
