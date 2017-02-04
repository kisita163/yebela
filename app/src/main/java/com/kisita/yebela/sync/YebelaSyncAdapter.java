package com.kisita.yebela.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.kisita.yebela.R;
import com.kisita.yebela.data.PlacesContract;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


public class YebelaSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String LOG_TAG = YebelaSyncAdapter.class.getSimpleName();



    private static final String REQUEST_URL = "http://192.168.1.40/gcm/request_for_update.php";

    // Interval at which to sync with server, in seconds.
    private static final int SYNC_INTERVAL = 60;
    private static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private Context mContext;

    YebelaSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(LOG_TAG, "Starting sync");
        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.yebela_keys), Context.MODE_PRIVATE);

        boolean sync_requested = sharedPref.getBoolean(mContext.getString(R.string.update_request), false);

        if(sync_requested) {
            sendRequestForUpdateToServer();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(mContext.getString(R.string.update_request),false);
            editor.apply();
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    private static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.i("sync","configurePeriodicSync");
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    private static void syncImmediately(Context context) {
        Log.i("sync","syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    private static Account
    getSyncAccount(Context context) {
        Log.i("sync","getSyncAccount");
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        Log.i("sync","onAccountCreated");
        YebelaSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }




    public static void initializeSyncAdapter(Context context) {
        Log.i("sync","initializeSyncAdapter");
        getSyncAccount(context);
    }

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;
        String     type;
        String     vicinity;
        String     name;
        String     place_id;
        String     city;
        String     locality;
        double     latitude;
        double     longitude;
        String     website;
        String     phone_number;
        double     mean_price;

        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonResults = jsonData.getJSONArray("results");

        for (int i = 0; i < jsonResults.length(); i++) {
            JSONObject jsonPlace = jsonResults.getJSONObject(i);


            name = jsonPlace.getString("name");
            city = jsonPlace.getString("city");
            website = jsonPlace.getString("website");
            vicinity = jsonPlace.getString("vicinity");
            type = jsonPlace.getString("type");
            phone_number = jsonPlace.getString("phone_number");
            place_id = jsonPlace.getString("place_id");
            locality = jsonPlace.getString("locality");
            latitude = Double.valueOf(jsonPlace.getString("latitude"));
            longitude = Double.valueOf(jsonPlace.getString("longitude"));
            mean_price = Double.valueOf(jsonPlace.getString("mean_price"));

            Log.i(LOG_TAG,"name = " + name  + "\n"+
                          "city = " + city  + "\n"+
                          "latitude = "+ latitude + "\n"+
                          "longitude = "+ longitude + "\n"+
                          "mean_price = "+ mean_price + "\n"+
                          "vicinity = "+ vicinity + "\n"+
                          "type = "+ type + "\n"+
                          "website = "+ website + "\n");
            // Add data to db
            addLocation(name,
                    city,
                    latitude,
                    longitude,
                    vicinity,
                    website,
                    type,
                    mean_price,
                    phone_number,
                    locality,
                    place_id);
        }
    }

    private void addLocation(String  name,
                             String city,
                             double  latitude,
                             double  longitude,
                             String  vicinity,
                             String  website,
                             String     type,
                             double mean_price,
                             String phone_number,
                             String locality,
                             String place_id) {
        ContentValues PlaceValues = new ContentValues();

        // Then add the data, along with the corresponding name of the data type,
        // so the content provider knows what kind of value is being inserted.
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_PLACE_ID, place_id);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_PHONE_NUMBER, phone_number);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_LOCALITY, locality);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_NAME, name);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_LATITUDE,longitude);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_LONGITUDE,latitude);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_VICINITY,vicinity);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_TYPE,type);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_MEAN_PRICE,mean_price);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_WEBSITE,website);
        PlaceValues.put(PlacesContract.PlaceEntry.COLUMN_CITY,city);

        // Finally, insert location data into the database.
        Uri insertedUri = getContext().getContentResolver().insert(
                PlacesContract.PlaceEntry.CONTENT_URI,
                PlaceValues
        );

        Log.i(LOG_TAG,insertedUri.toString());
    }

    /**
     *  sending the request for update to the server
     *
     */
    private void sendRequestForUpdateToServer() {
        OkHttpClient client = new OkHttpClient();
        String TIMESTAMP = "timestamp";
        SharedPreferences sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.yebela_keys), Context.MODE_PRIVATE);

        String timestamp = sharedPref.getString(mContext.getString(R.string.timestamp),"0000-00-00 00:00:00");
        Log.i(LOG_TAG,"Saved timestamp is "+timestamp);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart(TIMESTAMP,timestamp)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String response_body = response.body().string();
            Log.i(LOG_TAG,"response body is : "+response_body);
            try {
                parseJSon(response_body);
            } catch (JSONException e) {
                //TODO//System.out.printf("received data  = " + response_body);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}