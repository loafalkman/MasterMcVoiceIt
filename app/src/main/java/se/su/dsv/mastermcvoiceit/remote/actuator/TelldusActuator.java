package se.su.dsv.mastermcvoiceit.remote.actuator;

import android.util.Log;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by annika on 2017-12-01.
 */

public class TelldusActuator implements Actuator {
    private int id;
    private String name;
    private ActuatorType type;
    private int state;

    private SSHConnDetails connDetails;

    public TelldusActuator(int id, String name, ActuatorType type, SSHConnDetails connDetails) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.connDetails = connDetails;

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
        setSSHState(state);
    }

    private void setSSHState(int state) {
        String command = "tdtool";
        if (state <= 0)
            command += "--off " + id;
        else
            command += "--on " + id;

        Log.v("TellsudAct", "cmd result: " + SSHUtil.runCommand(command, connDetails));
    }

    @Override
    public ActuatorType getType() {
        return type;
    }
}
