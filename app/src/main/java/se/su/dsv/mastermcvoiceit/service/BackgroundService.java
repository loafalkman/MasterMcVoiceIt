package se.su.dsv.mastermcvoiceit.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
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
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.MainActivity;
import se.su.dsv.mastermcvoiceit.R;
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
    private Location lastLocation;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                testNotification();
            }
        }, 3000);
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

            if (lastLocation != null && gpsON) {
                for (Place place : places) {
                    place.tick(lastLocation);
                }
            }
            tickerHandler.postDelayed(this, UPDATE_INTERVAL_TICK);
        }
    };

    void testNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon_microphone_48);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("notifytest", "hello notification world!");

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        int mNotificationId = 378329572;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

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
