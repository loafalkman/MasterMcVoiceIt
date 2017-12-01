package se.su.dsv.mastermcvoiceit.remote.sensor;

/**
 * Created by felix on 2017-11-30.
 */

public interface Sensor {
    int getID();
    String getName();
    SensorType getType();
    float fetchSensorValue();
}
