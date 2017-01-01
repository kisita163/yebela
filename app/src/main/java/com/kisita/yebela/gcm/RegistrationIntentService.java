package com.kisita.yebela.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kisita.yebela.R;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/*
 * Created by Hugues on 20/11/2016.
 */

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String REGISTER_URL = "http://192.168.1.42/gcm/register.php";
    private static final String KEY_TOKEN = "gcm_token";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getApplicationContext().getString(R.string.yebela_keys), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getApplicationContext().getString(R.string.update_request),true);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                InstanceID instanceID = InstanceID.getInstance(this);

                String token = instanceID.getToken(getString(R.string.SENDER_ID),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.i(TAG, "GCM Registration Token: " + token );

                sendRegistrationToServer(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                editor.putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,true).apply();
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            editor.putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,false).apply();
            onHandleIntent(intent);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     *  sending the token to the server
     *
     * @param token token
     */
    private void sendRegistrationToServer(String token) throws Exception {

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart(KEY_TOKEN, token)
                .build();

        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();
        client.newCall(request).execute();
    }
}
