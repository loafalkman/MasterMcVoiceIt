package se.su.dsv.mastermcvoiceit.remote.actuator;

/**
 * Created by annika on 2017-12-01.
 */

public interface Actuator {
    int getID();
    String getName();
//    ActuatorType getType();
    int fetchState();
    void setState(int state);
}
