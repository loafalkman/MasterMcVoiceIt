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

        String myLine = getMyLine(list);
        if (myLine != null) {
            if (myLine.contains("ON")) {
                return 1;
            }
            if (myLine.contains("OFF")) {
                return 0;
            }
        }

        throw new IllegalStateException("SSH command 'tdtool -l' is faulty (is telldus installed?)");
    }

    private String getMyLine(String input) {
        String[] lines = input.split("\\n");

        if (!input.isEmpty() && lines[0].contains("Number of devices")) {

            String[] line0 = lines[0].split(" ");
            int size = Integer.valueOf(line0[line0.length - 1]);

            for (int i = 1; i < size + 1; i++) {
                if (lines[i].contains("" + id)) {
                    return lines[i];
                }
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
