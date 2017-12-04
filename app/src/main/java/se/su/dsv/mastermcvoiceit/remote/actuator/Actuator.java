package se.su.dsv.mastermcvoiceit.remote.actuator;

/**
 * Created by annika on 2017-12-01.
 */

public interface Actuator {
    int getID();
    String getName();
    int fetchState();

    /**
     * @param state Default definition:
     *              (state == Integer.MAX_INT) --> full power ;
     *              (state == 0) --> off
     */
    void setState(int state);

    ActuatorType getType();
}
