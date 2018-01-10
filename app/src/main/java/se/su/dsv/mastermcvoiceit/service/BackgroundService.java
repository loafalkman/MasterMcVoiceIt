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

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.place.Place;

/**
 * Created by annika on 2017-11-28.
 */

public class BackgroundService extends Service {
    public static final String INTENT_KEY_GPS_ON = "startGps";

    private final int UPDATE_INTERVAL_TICK = 10000;
    private final int UPDATE_INTERVAL_GPS = 5000;

    private boolean gpsON;
    private MyLocationListener locationListener;
    private LocationManager locationManager;

    private Handler tickerHandler = new Handler();
    public static Location lastLocation; // TODO: temporary public for demo purpose

    public static ArrayList<Place> places = new ArrayList<>();

    /**
     * Location permission is already granted.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new MyLocationListener();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        tickerHandler.post(ticker);

        Log.d("Service", "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "onStartCommand");
        boolean startGPS = false;
        if (intent != null) {
            startGPS = intent.getBooleanExtra(INTENT_KEY_GPS_ON, true);
        }

        startGPS(startGPS);
        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable ticker = new Runnable() {
        @Override
        public void run() {
            Log.d("Service", "run location " + lastLocation);

            for (Place place : places) {
                ArrayList<String[]> notify = place.tick(gpsON? lastLocation : null);

                if (notify.size() != 0) {

                    for (String[] noti : notify) {
                        Intent nService = new Intent(BackgroundService.this, NotificationService.class);
                        nService.putExtra(NotificationService.EXTRA_NOTIFICATION_CODE, noti);
                        BackgroundService.this.startService(nService);
                    }
                }
            }

            tickerHandler.postDelayed(this, UPDATE_INTERVAL_TICK);
        }
    };

    private void startGPS(boolean on) {
        if (on) {
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_GPS, 0, locationListener);
        } else if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        gpsON = on;
    }

    /**
     * Makes sure that the GPS is stopped when the service is.
     */
    @Override
    public void onDestroy() {
        Log.d("Service", "onDestroy");

        super.onDestroy();
        startGPS(false);
        tickerHandler.removeCallbacks(ticker);
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