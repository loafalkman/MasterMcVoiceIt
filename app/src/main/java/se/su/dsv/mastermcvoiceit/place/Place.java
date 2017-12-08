package se.su.dsv.mastermcvoiceit.place;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.NotificationCompat;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;

/**
 * Created by annika on 2017-12-05.
 */

public abstract class Place {
    protected Context context;
    protected SensorList sensorList;
    protected ActuatorList actuatorList;
    protected Location location;

    public Place(Context context, SensorList sensorList, ActuatorList actuatorList, Location location) {
        this.sensorList = sensorList;
        this.actuatorList = actuatorList;
        this.location = location;
    }

    public abstract String[] tick(Location currentLocation);

    public SensorList getSensorList() {
        return sensorList;
    }

    public ActuatorList getActuatorList() {
        return actuatorList;
    }

    public Location getLocation() {
        return location;
    }
}
