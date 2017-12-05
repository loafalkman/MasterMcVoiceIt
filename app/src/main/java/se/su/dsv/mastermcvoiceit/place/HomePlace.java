package se.su.dsv.mastermcvoiceit.place;

import android.content.Context;
import android.location.Location;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorType;
import se.su.dsv.mastermcvoiceit.remote.actuator.TelldusActuator;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorType;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;


/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {

    // test constructor
    public HomePlace(Context context, Location location) {
        super(context, null, null, location);
        initSensors();
        initActuators();
    }

    public void tick(Location currentLocation) {
        if (currentLocation.distanceTo(super.location) < 1000) {
            // someting!
        }
    }

    /**
     * should in the future ask R.pi. what sensorsById it has
     */
    private void initSensors() {
        sensorList = new SensorList();

        sensorList.add(new TelldusSensor(2, "Living room", SensorType.TEMPERATURE));
        sensorList.add(new TelldusSensor(15, "Garage", SensorType.TEMPERATURE));
        sensorList.add(new TelldusSensor(10, "Front porch", SensorType.WIND));
    }

    private void initActuators() {
        actuatorList = new ActuatorList();

        actuatorList.add(new TelldusActuator(1, "bedroom light", ActuatorType.POWER_SWITCH));
        actuatorList.add(new TelldusActuator(42, "coffeemaker", ActuatorType.POWER_SWITCH));
        actuatorList.add(new TelldusActuator(5, "central heating", ActuatorType.HEATER));
        actuatorList.add(new TelldusActuator(7, "element", ActuatorType.POWER_SWITCH));
    }
}
