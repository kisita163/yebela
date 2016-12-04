package com.kisita.yebela.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Hugues on 17/11/2016.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String LOCATION_QUERY = "Kinshasa, RDC";

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_PLACE_DIR = PlacesContract.PlaceEntry.CONTENT_URI;
    private static final Uri TEST_PLACE_WITH_LOCATION_DIR = PlacesContract.PlaceEntry.buildPlaceLocation(LOCATION_QUERY);
    // content://com.example.android.sunshine.app/location"
    private static final Uri TEST_LOCATION_DIR = PlacesContract.LocationEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = PlacesProvider.buildUriMatcher();

        assertEquals("Error: The PLACE URI was matched incorrectly.",
                testMatcher.match(TEST_PLACE_DIR), PlacesProvider.PLACE);
        assertEquals("Error: The PLACE WITH LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_PLACE_WITH_LOCATION_DIR), PlacesProvider.PLACE_WITH_LOCATION);
        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_LOCATION_DIR), PlacesProvider.LOCATION);
    }
}
