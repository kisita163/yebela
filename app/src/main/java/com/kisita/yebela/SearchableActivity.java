package com.kisita.yebela;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class SearchableActivity extends AppCompatActivity{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        TextView text = (TextView)findViewById(R.id.title);
        text.setText(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);
    }
}
