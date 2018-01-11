package se.su.dsv.mastermcvoiceit.place;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

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

    private int zonePadding = 50;
    private SSHConnDetails connDetails;
    private boolean bedroomLighOnService = true;
    private boolean locationWasNull;
    private boolean wasHere;
    private boolean isHere;

    public HomePlace(Location location, SSHConnDetails connDetails, int id) {
        super(null, null, location, id);
        this.connDetails = connDetails;

        initSensors();
        initActuators();
        initActions();
    }

    private void initActions() {

        // getting home / leaving home action.
        actions.add(new Action(ID_NOTIFICATION_GPS_DETECTED) {

            @Override
            public String[] checkCondition(Location currentLocation) {

                if (bedroomLighOnService) {

                    // approaching home
                    if (isHere && actuatorList.get(11).getState() == 0)
                        return new String[]{
                                "" + id,
                                "Turn on bedroom light",
                                "" + ID_NOTIFICATION_GPS_DETECTED
                        };

                    // leaving home
                    else if (!isHere && actuatorList.get(11).getState() > 0)
                        return new String[]{
                                "" + id,
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

    public ArrayList<String[]> tick(Location currentLocation) {
        sensorList.updateBatches();
        actuatorList.updateBatches();
        ArrayList<String[]> ret = new ArrayList<>();
        String[] tmp;

        if (currentLocation != null) { // gps on

            if (!locationWasNull) {
                if (currentLocation.distanceTo(HomePlace.super.location) < 1000 - zonePadding) {
                    isHere = true;
                } else if (currentLocation.distanceTo(HomePlace.super.location) > 1000 + zonePadding) {
                    isHere = false;
                }

                if (isHere != wasHere) { // user has moved in or out of the gateway area
                    bedroomLighOnService = true;
                } else {
                    bedroomLighOnService = false;
                }

                for (Action action : actions) {
                    tmp = action.checkCondition(currentLocation);
                    if (tmp != null)
                        ret.add(tmp);
                }
            }

            wasHere = isHere;
            locationWasNull = false;

        } else {
            locationWasNull = true;
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
