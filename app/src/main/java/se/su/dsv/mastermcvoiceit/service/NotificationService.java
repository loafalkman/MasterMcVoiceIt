package se.su.dsv.mastermcvoiceit.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.place.HomePlace;

/**
 * Created by annika on 2017-12-08.
 */

public class NotificationService extends IntentService {

    static final String ACTION_YES = "YES";
    static final String ACTION_NO = "NO";
    static final String ACTION_CANCEL = "CANCEL";
    NotificationManager mNotificationManager;

    public NotificationService() {
        super("NotificationService");
        Log.d("N Service", "constructor");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("N Service", "onHandleIntent");

        if (intent != null) {
            Bundle extras = intent.getExtras();

            if (extras != null && !extras.isEmpty()) {

                String[] codes = intent.getStringArrayExtra("notification codes");
                if (codes != null) {
                    buildNotification(codes);
                }

                final String action = intent.getAction();
                int placeID = extras.getInt("place id");

                if (placeID == 0) {
                    HomePlace homePlace = (HomePlace) BackgroundService.places.get(0);
                    if (action.equals(ACTION_YES)) {
                        homePlace.getActuatorList().get(1).setState(1);
                        mNotificationManager.cancel(378329572);
                    }
                    if (action.equals(ACTION_CANCEL)) {
                        homePlace.setBedroomLighOnService(false);
                        mNotificationManager.cancel(378329572);
                    }
                }
            }
        }
    }

    private void buildNotification(String[] codes) {
        Log.d("N Service", "buildNotification");

        if (codes[0].equals("0")) {
            if (codes[1].equals("Turn on bedroom light")) {
                BLOffNotification();
            }
        }
    }

    private void BLOffNotification() {
        Log.d("N Service", "BLOffNotification");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon_appbar_brain_transparent);
        mBuilder.setContentTitle("Bedroom light off");
        mBuilder.setContentText("Turn on bedroom light?");
//        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else {
            mBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        // Action yes
        Intent yesAction = new Intent(this, NotificationService.class);
        yesAction.putExtra("place id", 0);
        yesAction.setAction(ACTION_YES);

        PendingIntent yesPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        yesAction,
                        PendingIntent.FLAG_UPDATE_CURRENT // ??
                );

        mBuilder.addAction(R.drawable.temp, "YES", yesPendingIntent);

        // Action no
        Intent noAction = new Intent(this, NotificationService.class);
        noAction.putExtra("place id", 0);
        noAction.setAction(ACTION_CANCEL);

        PendingIntent noPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        noAction,
                        PendingIntent.FLAG_UPDATE_CURRENT // ??
                );

        mBuilder.addAction(R.drawable.temp, "CANCEL", noPendingIntent);

        int mNotificationId = 378329572;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}
