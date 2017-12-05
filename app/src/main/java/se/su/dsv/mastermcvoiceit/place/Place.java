package se.su.dsv.mastermcvoiceit.place;

import android.location.Location;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;

/**
 * Created by annika on 2017-12-05.
 */

public abstract class Place {
    private SensorList sensorList;
    private ActuatorList actuatorList;
    private Location locaion;

    public Place(SensorList sensorList, ActuatorList actuatorList, Location locaion) {
        this.sensorList = sensorList;
        this.actuatorList = actuatorList;
        this.locaion = locaion;
    }

    public abstract void tick(Location currentLocation);
}
