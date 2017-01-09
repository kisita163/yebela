package com.kisita.yebela.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kisita.yebela.R;
import com.kisita.yebela.data.PlacesContract;
import com.kisita.yebela.utility.ResultAdapter;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener{
    private ListView searchList;
    private TextView mNoResult;
    private String selection = PlacesContract.PlaceEntry.COLUMN_NAME+ " LIKE ?";
    private ResultAdapter mMainAdapter;
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
        setContentView(R.layout.activity_search);
        searchList = (ListView)findViewById(R.id.searchList);
        mMainAdapter = new ResultAdapter(this, null, 0);
        searchList.setAdapter(mMainAdapter);
        searchList.setOnItemClickListener(mMainAdapter);
        mNoResult = (TextView)findViewById(R.id.no_result) ;
        getLoaderManager().initLoader(0, null, this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setFocusable(true);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectionArgs[0] = newText;
        getLoaderManager().restartLoader(0,null,this);
        return false;
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
        mMainAdapter.swapCursor(cursor);
        System.out.println("onLoadFinished and count  = " + mMainAdapter.getCount());
        if(mMainAdapter.getCount() == 0){ // No result
            mNoResult.setVisibility(View.VISIBLE);
        }else {
            mNoResult.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO
    }
}
