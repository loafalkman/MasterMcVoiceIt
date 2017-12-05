package se.su.dsv.mastermcvoiceit.place;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {
    private NotificationManager notificationManager;

    public HomePlace(Context context, SensorList sensorList, ActuatorList actuatorList, Location locaion) {
        super(context, sensorList, actuatorList, locaion);
    }

    // test constructor
    public HomePlace(Context context, Location location) {
        super(context, null, null, location);
    }

    public void tick(Location currentLocation) {
        if (currentLocation.distanceTo(this.location) < 1000) {
            // test!

            testNotification("Close!", "Question");

        } else {

        }

    }

    private void testNotification(String title, String message) {
        NotificationCompat.Builder builder = createNotification();
        builder.setContentTitle(title);
        builder.setContentText(message);

        Intent resultIntent = createIntent();
        PendingIntent pendingIntent = createPendingIntent(resultIntent);
        builder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) this.context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(9999, builder.build());


    }

    private NotificationCompat.Builder createNotification() {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this.context)
//                .setSmallIcon(R.drawable.notification_yellow)
                .setAutoCancel(true)
                .setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX);

        return nb;
    }

    private Intent createIntent() {
        Intent resultIntent = new Intent(this.context, HomePlace.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return resultIntent;
    }

    private PendingIntent createPendingIntent(Intent resultIntent) {
                return PendingIntent.getActivity(this.context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    public void sendNotification(
//            String heading,
//            String message,
//            ArrayList<Notification.Action> notificatinActions,
//            Intent intent,
//            PendingIntent pendingIntent
//    )  {
//        // set notification and send
//    }
}
