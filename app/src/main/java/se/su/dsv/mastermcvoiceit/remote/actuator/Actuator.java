package se.su.dsv.mastermcvoiceit.remote.actuator;

import java.util.ArrayList;

/**
 * Created by annika on 2017-12-01.
 */

public interface Actuator {
    int getID();

    String getName();

    /**
     * Should not get state from server, implement a Batch that update the state instead.
     */
    int getState();

    /**
     * @param state Default definition:
     *              (state == Integer.MAX_INT) --> full power ;
     *              (state == 0) --> off
     */
    void setState(int state);

    ActuatorType getType();

    /**
     * Tip: implement so that it creates own Actuators in its constructor, getting actuator info from server.
     */
    interface Batch<A extends Actuator> {

        /**
         * No need to create a new thread, ActuatorList.updateBatches() uses its own.
         */
        void updateBatch();

        ArrayList<A> getActuators();
    }
}
