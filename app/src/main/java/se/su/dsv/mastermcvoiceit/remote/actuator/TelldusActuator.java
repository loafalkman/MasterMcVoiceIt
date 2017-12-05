package se.su.dsv.mastermcvoiceit.remote.actuator;

import android.util.Log;

/**
 * Created by annika on 2017-12-01.
 */

public class TelldusActuator implements Actuator {
    private int id;
    private String name;
    private ActuatorType type;

    public TelldusActuator(int id, String name, ActuatorType type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
        return (int) Math.round(Math.random());
    }

    @Override
    public void setState(int state) {
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
