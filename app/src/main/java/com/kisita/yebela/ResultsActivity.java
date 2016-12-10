package com.kisita.yebela;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        TextView text = (TextView)findViewById(R.id.title);
        text.setText(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);

        Bundle arguments = new Bundle();
        arguments.putInt(getString(R.string.service_id),getIntent().getIntExtra(getString(R.string.service_id),0));
        ResultsListFragment fragment = new ResultsListFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.result_container, fragment)
                .commit();
    }
}
