package se.su.dsv.mastermcvoiceit.place;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorType;
import se.su.dsv.mastermcvoiceit.remote.actuator.TelldusActuator;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorType;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;
import se.su.dsv.mastermcvoiceit.service.BackgroundService;
import se.su.dsv.mastermcvoiceit.service.NotificationService;


/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {

    private boolean bedroomLighOnService = true;

    public HomePlace(Context context, Location location) {
        super(context, null, null, location);
        initSensors();
        initActuators();
    }

    public void setBedroomLighOnService(boolean state) {
        this.bedroomLighOnService = state;
    }
    public String[] tick(Location currentLocation) {
        Log.d("HomePlace", "tick");
        if (currentLocation.distanceTo(super.location) < 1000) {
            if (actuatorList.get(1).fetchState() == 0 && bedroomLighOnService)
                return new String[]{"0", "Turn on bedroom light"};
            }

        return null;
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
