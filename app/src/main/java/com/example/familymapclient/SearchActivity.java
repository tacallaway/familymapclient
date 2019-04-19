package com.example.familymapclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    FamilyModel familyModel;
    RecyclerView searchListView;
    SearchListAdapter searchListAdapter;
    SearchActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        familyModel = (FamilyModel)getIntent().getSerializableExtra("FAMILY_MODEL");

        this.activity = this;

        searchListView = findViewById(R.id.searchList);
        searchListView.setLayoutManager(new LinearLayoutManager(this));

        ((EditText)findViewById(R.id.searchString)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = s.toString();
                List<String> searchListData;

                if (searchString.length() > 0) {
                    searchListData = SearchListData.getData(s.toString(), familyModel);
                } else {
                    searchListData = new ArrayList<String>();
                }

                searchListAdapter = new SearchListAdapter(activity, searchListData);
                searchListView.setAdapter(searchListAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
