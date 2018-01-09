package se.su.dsv.mastermcvoiceit.place;

import android.location.Location;
import android.os.Bundle;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.actuator.TelldusActuator;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;
import se.su.dsv.mastermcvoiceit.service.NotificationService;


/**
 * Created by annika on 2017-12-05.
 */

public class HomePlace extends Place {
    public static final int ID_NOTIFICATION_GPS_DETECTED = 475839235;
    private SSHConnDetails connDetails;

    public HomePlace(Location location, SSHConnDetails connDetails) {
        super(null, null, location);
        this.connDetails = connDetails;

        initSensors();
        initActuators();
        initActions();
    }

    private void initActions() {

        // getting home / leaving home action.
        actions.add(new Action(ID_NOTIFICATION_GPS_DETECTED) {
            private boolean bedroomLighOnService = true;

            @Override
            public String[] checkCondition(Location currentLocation) {
                // approaching home
                if (currentLocation != null && currentLocation.distanceTo(HomePlace.super.location) < 1000) {

                    if (actuatorList.get(11).getState() == 0 && bedroomLighOnService)
                        return new String[]{
                                "0",
                                "Turn on bedroom light",
                                "" + ID_NOTIFICATION_GPS_DETECTED
                        };

                // leaving home
                } else if (currentLocation != null && currentLocation.distanceTo(HomePlace.super.location) > 1000) {
                    if (actuatorList.get(11).getState() == 1 && bedroomLighOnService)
                        return new String[]{
                                "0",
                                "Turn off bedroom light",
                                "" + ID_NOTIFICATION_GPS_DETECTED
                        };
                }


                return null;
            }

            @Override
            public void doAction(String action, Bundle extras) {
                boolean turnActuatorOn = extras.getBoolean("turnOn");

                if (action.equals(NotificationService.ACTION_YES)) {
                    actuatorList.get(11).setState(turnActuatorOn ? 1 : 0);

                } else if (action.equals(NotificationService.ACTION_CANCEL)) {
                    bedroomLighOnService = false;
                }
            }

        });
    }

    public SSHConnDetails getConnDetails() {
        return this.connDetails;
    }

//    public void setBedroomLighOnService(boolean state) {
//        this.bedroomLighOnService = state;
//    }

    public ArrayList<String[]> tick(Location currentLocation) {
        sensorList.updateBatches();
        actuatorList.updateBatches();
        ArrayList<String[]> ret = new ArrayList<>();
        String[] tmp;

        for (Action action : actions) {
            tmp = action.checkCondition(currentLocation);
            if (tmp != null)
                ret.add(tmp);
        }

        return ret;
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
