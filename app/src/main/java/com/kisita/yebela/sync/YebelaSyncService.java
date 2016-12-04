package com.kisita.yebela.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/*
 * Created by Hugues on 17/11/2016.
 */
public class YebelaSyncService extends Service {
    private String TAG = getClass().getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private YebelaSyncAdapter sYebelaSyncAdapter = null;
    @Override
    public void onCreate() {
        Log.i(TAG,"Starting Sync Service");
        synchronized (sSyncAdapterLock) {
            if (sYebelaSyncAdapter == null) {
                System.out.println("Creating Adapter");
                sYebelaSyncAdapter = new YebelaSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        return sYebelaSyncAdapter.getSyncAdapterBinder();
    }
}
