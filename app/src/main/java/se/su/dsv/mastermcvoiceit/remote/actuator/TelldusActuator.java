package se.su.dsv.mastermcvoiceit.remote.actuator;

import android.util.Log;

/**
 * Created by annika on 2017-12-01.
 */

public class TelldusActuator implements Actuator {
    private int id;
    private String name;
    private ActuatorType type;
    private int state;

    public TelldusActuator(int id, String name, ActuatorType type) {
        this.id = id;
        this.name = name;
        this.type = type;

        // TODO get state from real actuator?
        this.state = 0;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int fetchState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
        if (state > 0) {
            Log.v("TelldsAct", "Actuator " + name + " on");
        } else {
            Log.v("TelldsAct", "Actuator " + name + " off");
        }
    }

    @Override
    public ActuatorType getType() {
        return type;
    }
}
