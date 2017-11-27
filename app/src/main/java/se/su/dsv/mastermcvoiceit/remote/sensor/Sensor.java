package se.su.dsv.mastermcvoiceit.remote.sensor;

/**
 * Created by felix on 2017-11-27.
 */

public interface Sensor {
    Type getType();
    String getName();
    int getID();
    float getSensorValue();

    enum Type {
        unknown,
        temperature,
        UV,
        wind,
    }
}
