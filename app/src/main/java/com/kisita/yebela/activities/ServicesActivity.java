package com.kisita.yebela.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.kisita.yebela.R;
import com.kisita.yebela.gcm.QuickstartPreferences;
import com.kisita.yebela.gcm.RegistrationIntentService;
import com.kisita.yebela.utility.RecycleAdapter;

import java.text.BreakIterator;
import java.text.CollationElementIterator;

import static com.kisita.yebela.sync.YebelaSyncAdapter.initializeSyncAdapter;


public class ServicesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "ServicesActivity";
    //private CollapsingToolbarLayout collapsingToolbarLayout;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int YEBELA_REQUEST_COARSE_LOCATION = 100;
    private RecyclerView mRecyclerView;
    private RecycleAdapter mAdapter;

    private Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        setRecyclerView();
        setActionBar();
        createGoogleApiClient();
        initializeSyncAdapter(this);

        Log.i(TAG, "Try to start registration service");
        if (checkPlayServices()) {
            Log.i(TAG, "Google services checked");
            // Start IntentService to register this application with GCM.
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getApplicationContext().getString(R.string.yebela_keys), Context.MODE_PRIVATE);
            boolean sentToken = sharedPref
                    .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (!sentToken) {
                Log.i(TAG, "Starting registration service");
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.search_icon) {
            Intent search = new Intent(this, SearchActivity.class);
            startActivity(search);
            //onSearchRequested();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_services, menu);

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/
       /* MenuItem search_icon = menu.findItem(R.id.search_icon);
        search_icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return false;
            }
        });*/
        return true;
    }


    private void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ServicesActivity.this, 2));
        mAdapter = new RecycleAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }


    private void setActionBar() {
        //collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitleEnabled(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Yebela", "connected to google play services ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    YEBELA_REQUEST_COARSE_LOCATION);
        }else{
            getLocation();
        }
    }

    private  void getLocation(){
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i("Yebela","connected to google play services  : latitude = "+mLastLocation.getLatitude());
            Log.i("Yebela","connected to google play services  : longitude = "+mLastLocation.getLongitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case YEBELA_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Yebela","connection to google play services suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Yebela","connection to google play services failed");
    }
}
