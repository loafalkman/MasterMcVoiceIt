package se.su.dsv.mastermcvoiceit.service;

import android.app.IntentService;
import android.app.Notification;
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
import android.util.Log;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.MainActivity;
import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.place.Place;

/**
 * Created by annika on 2017-11-28.
 */

public class BackgroundService extends IntentService {
    public static final String INTENT_KEY_GPS_ON = "startGps";

    private final int UPDATE_INTERVAL_TICK = 10000;
    private final int UPDATE_INTERVAL_GPS = 5000;

    private boolean gpsON;
    private MyLocationListener locationListener;
    private LocationManager locationManager;

    private Handler tickerHandler = new Handler();
    public static Location lastLocation; // TODO: temporary public for demo purpose

    public static ArrayList<Place> places = new ArrayList<>();

    public BackgroundService() {
        super("");
    }
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
                Log.d("handler thing", "postDepolyed");
            }
        }, 10000);
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
        mBuilder.setSmallIcon(R.drawable.icon_appbar_brain_transparent);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello World!");
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else {
            mBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        Intent resultIntent = new Intent(this, BackgroundService.class);
        resultIntent.putExtra("Notification", true);

        PendingIntent resultPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT // ??
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        int mNotificationId = 378329572;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && !extras.isEmpty()) {
                boolean isNotification = extras.getBoolean("Notification");
                if (isNotification) {
                    testNotification2();
                }
            }
        }
    }

    void testNotification2() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon_appbar_brain_transparent);
        mBuilder.setContentTitle("My notification");
        mBuilder.setContentText("Hello again!!");
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else {
            mBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int mNotificationId = 378329573;
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
