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

/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public HomePlace(Context context, SensorList sensorList, ActuatorList actuatorList, Location locaion) {
        super(context, sensorList, actuatorList, locaion);
    }

    public void tick(Location currentLocation) {
        if (currentLocation.distanceTo(this.location) < 1000) {

        } else {

        }

    }

    private void createNotification() {
        notificationBuilder = new NotificationCompat.Builder(this.context)
//                .setSmallIcon(R.drawable.notification_yellow)
                .setAutoCancel(true)
                .setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX);
    }

    public void sendNotification(
            String heading,
            String message,
            ArrayList<Notification.Action> notificatinActions,
            Intent intent,
            PendingIntent pendingIntent
    )  {
        // set notification and send
    }
}
