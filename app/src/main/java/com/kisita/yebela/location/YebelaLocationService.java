package com.kisita.yebela.location;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.kisita.yebela.R;

public class YebelaLocationService extends Service {
    public YebelaLocationService() {
    }

    private LocationManager locationMgr = null;
    private LocationListener onLocationChange = new LocationListener()
    {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    getApplicationContext().getString(R.string.yebela_keys), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(getApplicationContext().getString(R.string.latitude),String.valueOf(latitude));
            editor.putString(getApplicationContext().getString(R.string.longitude),String.valueOf(longitude));

            editor.apply();
        }
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,
                0, onLocationChange);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,
                onLocationChange);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        locationMgr.removeUpdates(onLocationChange);
    }
}
