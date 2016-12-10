package com.kisita.yebela.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.kisita.yebela.ServicesActivity;
import com.kisita.yebela.R;

/*
 * Created by Hugues on 20/11/2016.
 */
public class YebelaGCMListenerService extends GcmListenerService {
    private static final String TAG = "GCMListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getApplicationContext().getString(R.string.yebela_keys), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getApplicationContext().getString(R.string.update_request),true);
        editor.putString(getApplicationContext().getString(R.string.timestamp),message);

        editor.commit();

        sendNotification();
    }

    private void sendNotification(){
        NotificationManager mNotificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,new Intent(this, ServicesActivity.class),0);
        Bitmap largeIcon =
                BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuild  =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle("Yebela")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("New data available"))
                .setContentText("New data available")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuild.setContentIntent(contentIntent);
        mNotificationManager.notify(1,mBuild.build());
    }
}



