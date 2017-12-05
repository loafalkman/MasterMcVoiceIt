package se.su.dsv.mastermcvoiceit.place;

import android.location.Location;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;

/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {

    public HomePlace(SensorList sensorList, ActuatorList actuatorList, Location locaion) {
        super(sensorList, actuatorList, locaion);
    }

    public void tick(Location currentLocation) {

    }
}
