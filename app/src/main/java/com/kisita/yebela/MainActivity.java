package com.kisita.yebela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.kisita.yebela.gcm.QuickstartPreferences;
import com.kisita.yebela.gcm.RegistrationIntentService;
import com.kisita.yebela.utility.RecycleAdapter;

import static com.kisita.yebela.sync.YebelaSyncAdapter.initializeSyncAdapter;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    CollapsingToolbarLayout collapsingToolbarLayout;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private RecyclerView mRecyclerView;
    private RecycleAdapter mAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Demo");
        mAdapter = new RecycleAdapter(this);
        //recycler
        //LinearLayoutManager llm = new LinearLayoutManager(this);
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

       // getSupportActionBar().setElevation(0f);

        setSupportActionBar(toolbar);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast("Item "+position+" pressed." );
            }
        });

        initializeSyncAdapter(this);

        Log.i(TAG,"Try to start registration service");
        if (checkPlayServices()) {
            Log.i(TAG,"Google services checked");
            // Start IntentService to register this application with GCM.
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getApplicationContext().getString(R.string.yebela_keys), Context.MODE_PRIVATE);
            boolean sentToken = sharedPref
                    .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if(!sentToken) {
                Log.i(TAG,"Starting registration service");
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
   Check the device to make sure it has the google play services apk. if it doesn't,
   display a dialog that allow users to download the apk
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private void makeToast(String input) {
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }
}
