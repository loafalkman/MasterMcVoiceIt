package se.su.dsv.mastermcvoiceit.place;

import android.content.Context;
import android.location.Location;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.actuator.TelldusActuator;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorType;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;


/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {
    private SSHConnDetails connDetails;

    private boolean bedroomLighOnService = true;

    public HomePlace(Context context, Location location, SSHConnDetails connDetails) {
        super(context, null, null, location);
        this.connDetails = connDetails;

        initSensors();
        initActuators();
    }

    public void setBedroomLighOnService(boolean state) {
        this.bedroomLighOnService = state;
    }

    public String[] tick(Location currentLocation) {
        sensorList.updateBatches();
        actuatorList.updateBatches();

        if (currentLocation != null && currentLocation.distanceTo(super.location) < 1000) {
            if (actuatorList.get(11).getState() == 0 && bedroomLighOnService)
                return new String[]{"0", "Turn on bedroom light"};
        }

        return null;
    }

    /**
     * should in the future ask R.pi. what sensorsById it has
     */
    private void initSensors() {
        sensorList = new SensorList();
        sensorList.addBatch(new TelldusSensor.TelldusSensorBatch(connDetails));
    }

    private void initActuators() {
        actuatorList = new ActuatorList();
        actuatorList.addBatch(new TelldusActuator.TelldusActuatorBatch(connDetails));
    }
}
