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
import com.kisita.yebela.utility.ResultAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class ResultsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

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
    private static final String[] QUERY_TYPE = new String[] {
            "Restaurant",
            "Lodging",
            "Event",
            "culture",
            "Health",
            "Spare-time",
            "Well-being",
            "Transport"
    };

    private ResultAdapter mMainAdapter;

    public ResultsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        getLoaderManager().initLoader(0, null, this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_results, container, false);
        // The ForecastAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMainAdapter = new ResultAdapter(getActivity(), null, 0);
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
        String selection = PlacesContract.PlaceEntry.COLUMN_TYPE + " = ?";
        String[] selectionArgs = { QUERY_TYPE[getArguments().getInt(getString(R.string.service_id))] };

        return new CursorLoader(getActivity(),
                PlacesUri,
                PLACES_COLUMNS,
                selection,
                selectionArgs,
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
