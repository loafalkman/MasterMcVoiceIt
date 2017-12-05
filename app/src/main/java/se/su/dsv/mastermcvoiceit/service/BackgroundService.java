package se.su.dsv.mastermcvoiceit.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.MainActivity;

/**
 * Created by annika on 2017-11-28.
 */

public class BackgroundService extends Service {
    public static final String INTENT_KEY_GPS_ON = "startGps";

    private final int UPDATE_INTERVAL_TICK = 10000;
    private final int UPDATE_INTERVAL_GPS = 5000;
    private MyLocationListener locationListener;
    private LocationManager locationManager;

    private Handler tickerHanler = new Handler();
    private Location lastLocation;

    public static ArrayList<Object> places = new ArrayList<>();

    /**
     * Location permission is already granted.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new MyLocationListener();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        tickerHanler.post(ticker);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean startGPS = intent.getBooleanExtra(INTENT_KEY_GPS_ON, true);
        startGPS(startGPS);

        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable ticker = new Runnable() {
        @Override
        public void run() {

            for (Object place : places) {
                //TODO: place.tick();
            }
            tickerHanler.postDelayed(this, UPDATE_INTERVAL_TICK);
        }
    };

    private void startGPS(boolean on) {
        if (on) {
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_GPS, 0, locationListener);
        } else if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * Makes sure that the GPS is stopped when the service is.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        startGPS(false);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) { // TODO: just stop tracking via gps instead, don't open settings.
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
