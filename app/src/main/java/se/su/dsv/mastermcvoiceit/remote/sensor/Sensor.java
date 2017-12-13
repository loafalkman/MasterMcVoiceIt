package se.su.dsv.mastermcvoiceit.remote.sensor;

import java.util.Collection;

/**
 * Created by felix on 2017-11-30.
 */

public interface Sensor {
    int getID();

    String getName();

    SensorType[] getSupportedTypes();

    /**
     * Should not get state from server, implement a Batch that update the readings instead.
     * Tip: Use getSupportedTypes()[i].ordinal() to check if an index is supported by this sensor.
     *
     * @return array of values for all SensorTypes, if this sensor doesn't support a SensorType, value at corresponding index will read 0.
     */
    float[] getSensorValues();

    /**
     * Tip: implement so that it creates own Sensors in its constructor, getting sensor info from server.
     */
    interface Batch<A extends Sensor> {

        /**
         * No need to create a new thread, ActuatorList.updateBatches() uses its own.
         */
        void updateBatch();

        Collection<A> getSensors();
    }
}
