package com.kisita.yebela.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kisita.yebela.R;
import com.kisita.yebela.data.PlacesContract;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    private String selection = PlacesContract.PlaceEntry.COLUMN_NAME+ " LIKE ?";
    private String[] selectionArgs = {""};
    private static final String[] PLACES_COLUMNS = {
            PlacesContract.PlaceEntry.TABLE_NAME + "." + PlacesContract.PlaceEntry._ID,
            PlacesContract.PlaceEntry.COLUMN_NAME,
            PlacesContract.PlaceEntry.COLUMN_CITY,
            PlacesContract.PlaceEntry.COLUMN_LATITUDE,
            PlacesContract.PlaceEntry.COLUMN_LONGITUDE,
            PlacesContract.PlaceEntry.COLUMN_MEAN_PRICE,
            PlacesContract.PlaceEntry.COLUMN_WEBSITE,
            PlacesContract.PlaceEntry.COLUMN_TYPE,
            PlacesContract.PlaceEntry.COLUMN_VICINITY,
            PlacesContract.PlaceEntry.COLUMN_LOCALITY,
            PlacesContract.PlaceEntry.COLUMN_PHONE_NUMBER,
            PlacesContract.PlaceEntry.COLUMN_PLACE_ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getLoaderManager().getLoader(0) == null){
            getLoaderManager().initLoader(0, null, this);
        }else{
            getLoaderManager().restartLoader(0,null,this);
        }
    }


    /*
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SharedPreferences sharedPref = this.getSharedPreferences(
                this.getString(R.string.yebela_keys), Context.MODE_PRIVATE);

        String currLatitude = "-4.4011293";//= sharedPref.getString(this.getString(R.string.latitude),"-4.4011293");
        String currLongitude = "15.2527045"; //= sharedPref.getString(this.getString(R.string.longitude),"15.2527045");

        // Add a marker on my position and move the camera
        LatLng myPosition = new LatLng(Double.valueOf(currLatitude),Double.valueOf(currLongitude));
        mMap.addMarker(new MarkerOptions().position(myPosition).title(getString(R.string.my_position)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition,14));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri PlacesUri = PlacesContract.PlaceEntry.CONTENT_URI;
        selectionArgs[0] = "%"+selectionArgs[0]+"%";

        return new CursorLoader(this,
                PlacesUri,
                PLACES_COLUMNS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            while (cursor.moveToNext()) {
                LatLng place = new LatLng(Double.valueOf(cursor.getString(4)),Double.valueOf(cursor.getString(3)));
                mMap.addMarker(new MarkerOptions().position(place).title(cursor.getString(1)));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
