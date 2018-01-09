package se.su.dsv.mastermcvoiceit.place;

import android.location.Location;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;

/**
 * Created by annika on 2017-12-05.
 */

public abstract class Place {
    protected SensorList sensorList;
    protected ActuatorList actuatorList;
    protected Location location;

    protected ArrayList<Action> actions = new ArrayList<>();

    public Place(SensorList sensorList, ActuatorList actuatorList, Location location) {
        this.sensorList = sensorList;
        this.actuatorList = actuatorList;
        this.location = location;
    }

    public abstract ArrayList<String[]> tick(Location currentLocation);

    public SensorList getSensorList() {
        return sensorList;
    }

    public ActuatorList getActuatorList() {
        return actuatorList;
    }

    public Location getLocation() {
        return location;
    }

    public void addAction(Action a) {
        actions.add(a);
    }

    public Action getAction(int actionID) {
        for (Action a : actions) {
            if (a.getID() == actionID)
                return a;
        }
        return null;
    }

    public abstract class Action {
        private int id;


        public Action(int id) {
            this.id = id;
        }

        /**
         * @param loc current location
         * @return {placeID, questionCode, actionID/notificationID}
         */
        public abstract String[] checkCondition(Location loc);

        public abstract void doAction(String action, Bundle bundle);

        public int getID() {
            return id;
        }
    }
}
