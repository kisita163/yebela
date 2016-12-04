package com.kisita.yebela.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hugues on 16/11/2016.
 */
public class PlacesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.kisita.yebela.app";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.kisita.yebela.app/place/ is a valid path for
    // looking at place data. content://com.kisita.yebela.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_PLACE = "place";

    /* Inner class that defines the table contents of the weather table */
    public static final class PlaceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;

        public static final String TABLE_NAME = "place";

        // Name
        public static final String COLUMN_NAME = "name";
        // Vicinity
        public static final String COLUMN_VICINITY = "vicinity";
        // Type
        public static final String COLUMN_TYPE = "type";

        // Phone number
        public static final String COLUMN_PHONE_NUMBER = "phone_number";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_WEBSITE = "website";

        // Latitude
        public static final String COLUMN_LATITUDE = "latitude";

        // Longitude
        public static final String COLUMN_LONGITUDE = "longitude";

        // City
        public static final String COLUMN_CITY = "city";

        // Mean price
        public static final String COLUMN_MEAN_PRICE = "mean_price";

        public static final String COLUMN_LOCALITY = "locality";

        public static Uri buildPlaceUri() {
            return CONTENT_URI;
        }
    }

}
