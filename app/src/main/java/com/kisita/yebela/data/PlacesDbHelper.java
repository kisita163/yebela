package com.kisita.yebela.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kisita.yebela.data.PlacesContract.PlaceEntry;
/**
 * Created by Hugues on 17/11/2016.
 */
public class PlacesDbHelper  extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 7;

    static final String DATABASE_NAME = "places.db";

    public PlacesDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold Places
        final String SQL_CREATE_PLACE_TABLE = "CREATE TABLE " + PlaceEntry.TABLE_NAME + " (" +

                PlaceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                PlaceEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_CITY+ " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                PlaceEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                PlaceEntry.COLUMN_WEBSITE + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_LOCALITY + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_VICINITY + " TEXT NOT NULL, " +
                PlaceEntry.COLUMN_MEAN_PRICE + " DOUBLE NOT NULL " + ");";


        db.execSQL(SQL_CREATE_PLACE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlaceEntry.TABLE_NAME);
        onCreate(db);
    }
}
