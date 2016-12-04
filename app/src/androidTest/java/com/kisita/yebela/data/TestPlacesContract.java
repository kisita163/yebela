package com.kisita.yebela.data;

import android.net.Uri;
import android.test.AndroidTestCase;
/**
 * Created by Hugues on 17/11/2016.
 */
public class TestPlacesContract extends AndroidTestCase {
    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_PLACE_LOCATION = "/Kinshasa";

    /*
        Students: Uncomment this out to test your weather location function.
     */
    public void testbuildPlaceLocation() {
        Uri locationUri = PlacesContract.PlaceEntry.buildPlaceLocation(TEST_PLACE_LOCATION);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildPlaceLocation in " +
                        "PlacesContract.",
                locationUri);
        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_PLACE_LOCATION, locationUri.getLastPathSegment());
        assertEquals("Error: Weather location Uri doesn't match our expected result",
                locationUri.toString(),
                "content://com.kisita.yebela.app/place/%2FKinshasa");
    }
}
