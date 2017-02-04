package com.kisita.yebela.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.vision.text.Text;
import com.kisita.yebela.R;
import com.kisita.yebela.data.PlacesContract;
import com.kisita.yebela.utility.PlaceDescription;

public class PlaceActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private PlaceDescription place;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private String selection = PlacesContract.PlaceEntry._ID + "=?";
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
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        place = new PlaceDescription();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (getLoaderManager().getLoader(0) == null){
            getLoaderManager().initLoader(0, null, this);
        }else{
            getLoaderManager().restartLoader(0,null,this);
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place, menu);
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
            Intent settings = new Intent(this,SettingsActivity.class);
            startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri PlacesUri = PlacesContract.PlaceEntry.CONTENT_URI;
        selectionArgs[0] = getIntent().getStringExtra("PlaceId");

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
                toolbar.setTitle(cursor.getString(1));
                place.setAddress(cursor.getString(8));
                place.setWebsite(cursor.getString(6));
                place.setPhoneNumber(cursor.getString(10));
                place.setName(cursor.getString(1));
                place.setLatlng(new LatLng(Double.valueOf(cursor.getString(4)),Double.valueOf(cursor.getString(3))));
            }
        } finally {
            cursor.close();
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnMapReadyCallback{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private static PlaceDescription mPlace;
        private GoogleMap mMap;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, PlaceDescription place) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            mPlace= place;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (sectionNumber){
                case 1 :
                    inflateViewWithThisResource(inflater,R.layout.fragment_place,container);
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.mapPlace);
                    if(mapFragment != null)
                        mapFragment.getMapAsync(this);
                    break;
                default:
                    break;
            }

            return rootView;
        }

        private void inflateViewWithThisResource(LayoutInflater inflater, int resource,ViewGroup container){
            if (rootView != null) {
                ViewGroup parent = (ViewGroup) rootView.getParent();
                if (parent != null)
                    parent.removeView(rootView);
            }
            try {
                rootView = inflater.inflate(resource, container, false);
            } catch (InflateException e) {
                System.err.println("View already created");
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.addMarker(new MarkerOptions().position(mPlace.getLatlng()).title(mPlace.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPlace.getLatlng(),16));
            setPlaceDescription();
        }

        void setPlaceDescription(){
            TextView addressView = (TextView)getActivity().findViewById(R.id.address);
            TextView phoneView = (TextView)getActivity().findViewById(R.id.phone_number);
            TextView websiteView = (TextView)getActivity().findViewById(R.id.website);


            addressView.setText(mPlace.getAddress());

            if(!mPlace.getPhoneNumber().equalsIgnoreCase("")) {
                phoneView.setText(mPlace.getPhoneNumber());
                phoneView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                phoneView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        call.setData(Uri.parse("tel:"+mPlace.getPhoneNumber()));
                        startActivity(call);
                    }
                });
            }
            else
                phoneView.setText(R.string.not_available);

            if(!mPlace.getWebsite().equalsIgnoreCase("")){
                websiteView.setText(mPlace.getWebsite());
                websiteView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                websiteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent web = new Intent(Intent.ACTION_VIEW);
                        web.setData(Uri.parse(mPlace.getWebsite()));
                        startActivity(web);
                    }
                });
            }
            else
                websiteView.setText(R.string.not_available);

        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1,place);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "INFO";
                case 1:
                    return "AVIS";
                case 2:
                    return "GALLERY";
            }
            return null;
        }
    }
}
