package com.kisita.yebela;

import android.support.v4.app.LoaderManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kisita.yebela.data.PlacesContract;
import com.kisita.yebela.utility.MainFragmentAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView mListView;


    private static final int PLACE_LOADER = 0;
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
    private MainFragmentAdapter mMainAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        // The ForecastAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMainAdapter = new MainFragmentAdapter(getActivity(), null, 0);
        // Get the reference of our list view an attach an adapter to it

        mListView = (ListView) rootView.findViewById(R.id.testList);
        mListView.setAdapter(mMainAdapter);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(PLACE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.
        Uri PlacesUri = PlacesContract.PlaceEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                PlacesUri,
                PLACES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMainAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMainAdapter.swapCursor(null);
    }

    // since we read the location when we create the loader, all we need to do is restart things
    /*void onLocationChanged( ) {
        //updatePlaces(); // sync immediately request
        getLoaderManager().restartLoader(PLACE_LOADER, null, this);
    }*/
}
