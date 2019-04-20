package com.example.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {
    FamilyModel familyModel;
    RecyclerView filterListView;
    FilterListAdapter filterListAdapter;
    FilterActivity activity;
    List<String> filterListData;
    FiltersData filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        familyModel = (FamilyModel)getIntent().getSerializableExtra("FAMILY_MODEL");
        filters = (FiltersData)getIntent().getSerializableExtra("FILTERS");

        if (filters == null) {
            filters = new FiltersData();
        }

        this.activity = this;

        filterListView = findViewById(R.id.filterList);
        filterListView.setLayoutManager(new LinearLayoutManager(this));

        filterListData = FilterListData.getData(familyModel);

        filterListAdapter = new FilterListAdapter(activity, filterListData, filters);
        filterListView.setAdapter(filterListAdapter);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("FILTERS", filters);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
