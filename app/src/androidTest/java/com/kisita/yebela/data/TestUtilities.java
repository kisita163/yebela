package com.kisita.yebela.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.kisita.yebela.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/*
    Students: These are functions and some test data to make it easier to test your database and
    Content Provider.  Note that you'll want your PlacesContract class to exactly match the one
    in our solution to use these as-given.
 */
public class TestUtilities extends AndroidTestCase {
    static final String TEST_LOCATION = "Kinshasa";
    static final String TEST_NAME = "Hotel Memling";
    static final String TEST_TYPE = "lodging";

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default place values for your database tests.
     */
    static ContentValues createPlaceValues(long locationRowId) {
        ContentValues placeValues = new ContentValues();
        placeValues.put(PlacesContract.PlaceEntry.COLUMN_LOC_KEY, locationRowId);
        placeValues.put(PlacesContract.PlaceEntry.COLUMN_NAME, TEST_NAME);
        placeValues.put(PlacesContract.PlaceEntry.COLUMN_TYPE, TEST_TYPE);
        placeValues.put(PlacesContract.PlaceEntry.COLUMN_MIN_PRICE,300);
        placeValues.put(PlacesContract.PlaceEntry.COLUMN_SHORT_DESC, "Hotel");


        return placeValues;
    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the PlacesContract.
     */
    static ContentValues createKinshasaLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(PlacesContract.LocationEntry.COLUMN_CITY_NAME, TEST_LOCATION);
        testValues.put(PlacesContract.LocationEntry.COLUMN_COORD_LAT,"-4.444997");
        testValues.put(PlacesContract.LocationEntry.COLUMN_COORD_LONG,"15.265503");

        return testValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the PlacesContract as well as the PlacesDbHelper.
     */
    static long insertKinshasaLocationValues(Context context) {
        // insert our test records into the database
        PlacesDbHelper dbHelper = new PlacesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createKinshasaLocationValues();

        long locationRowId;
        locationRowId = db.insert(PlacesContract.LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Kinshasa Location Values", locationRowId != -1);

        return locationRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
