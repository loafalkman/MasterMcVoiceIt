package se.su.dsv.mastermcvoiceit.remote.actuator;

import android.os.Handler;
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
        this.state = getSSHState();
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
        this.state = getSSHState();
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
        setSSHState(state);
    }

    private int getSSHState() {
        String list = SSHUtil.runCommand("tdtool -l", connDetails);
        String result = chopString(list);
        if (result != null) {
            if (result.contains("ON")) {
                return 1;
            }
            if (result.contains("OFF")) {
                return 0;
            }
        }
        return 0;
    }

    private String chopString(String input) {

        String[] lines = input.split("\\n");
        //TODO: first line of this string tells the number of actuators connected, make this dynamic
        int size = 3;

        String[] deviceLines = new String[size];
        System.arraycopy(lines, 1, deviceLines, 0, size);

        for (String line : deviceLines) {
            if (line.contains(""+id)) {
                return line;
            }
        }
        return null;
    }

    private void setSSHState(int state) {
        final String command;
        if (state <= 0)
            command = "tdtool --off " + id;
        else
            command = "tdtool --on " + id;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("TellsudAct", "cmd result: " + SSHUtil.runCommand(command, connDetails));
            }
        }).start();

    }

    @Override
    public ActuatorType getType() {
        return type;
    }
}
