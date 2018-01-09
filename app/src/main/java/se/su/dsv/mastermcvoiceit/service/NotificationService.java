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
import se.su.dsv.mastermcvoiceit.place.Place;

/**
 * Created by annika on 2017-12-08.
 */

public class NotificationService extends IntentService {
    public static final String EXTRA_NOTIFICATION_CODE = "notification codes";

    public static final String ACTION_YES = "YES";
    public  static final String ACTION_CANCEL = "CANCEL";
    private NotificationManager mNotificationManager;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (intent != null) {
            Bundle extras = intent.getExtras();

            if (extras != null && !extras.isEmpty()) {

                String[] codes = intent.getStringArrayExtra(EXTRA_NOTIFICATION_CODE);
                if (codes != null) {
                    buildNotification(codes);
                }

                final String notifiAction = intent.getAction();
                if (notifiAction != null) {
                    int placeID = extras.getInt("place id");
                    int actionID = extras.getInt("actionID");
                    Log.v("NOTIFISERV", "Action id: "+actionID);

                    Place placeFromId = BackgroundService.places.get(placeID);
                    Place.Action action = placeFromId.getAction(actionID);

                    action.doAction(notifiAction, extras);
                    mNotificationManager.cancel(HomePlace.ID_NOTIFICATION_GPS_DETECTED);
                }
            }
        }
    }

    private void buildNotification(String[] codes) {
        if (codes[0].equals("0")) {
            if (codes[1].equals("Turn on bedroom light")) {
                BLOffNotification(0, true, Integer.parseInt(codes[2]), "Bedroom light off","Turn on bedroom light?");

            } else if (codes[1].equals("Turn off bedroom light")) {
                BLOffNotification(0, false, Integer.parseInt(codes[2]), "Bedroom light on","Turn off bedroom light?");
            }
        }
    }

    private void BLOffNotification(int placeID, boolean turnOn, int actionID, String title, String question) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon_appbar_brain_transparent);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(question);
//        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        } else {
            mBuilder.setPriority(Notification.PRIORITY_MAX);
        }

        // Action yes
        Intent yesAction = new Intent(this, NotificationService.class);
        yesAction.putExtra("place id", placeID);
        yesAction.putExtra("turnOn", turnOn);
        yesAction.putExtra("actionID", actionID);
        yesAction.setAction(ACTION_YES);

        PendingIntent yesPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        yesAction,
                        PendingIntent.FLAG_UPDATE_CURRENT // ??
                );

        mBuilder.addAction(R.drawable.temp, "YES", yesPendingIntent);

        // Action cancel
        Intent cancelAction = new Intent(this, NotificationService.class);
        cancelAction.putExtra("place id", placeID);
        cancelAction.putExtra("turnOn", turnOn);
        cancelAction.putExtra("actionID", actionID);
        cancelAction.setAction(ACTION_CANCEL);

        PendingIntent noPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        cancelAction,
                        PendingIntent.FLAG_UPDATE_CURRENT // ??
                );

        mBuilder.addAction(R.drawable.temp, "CANCEL", noPendingIntent);

        mNotificationManager.notify(actionID, mBuilder.build());
    }
}
