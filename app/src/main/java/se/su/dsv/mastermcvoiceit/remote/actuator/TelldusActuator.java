package se.su.dsv.mastermcvoiceit.remote.actuator;

import android.util.Log;
import android.util.SparseIntArray;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by annika on 2017-12-01.
 */

public class TelldusActuator implements Actuator {
    private int id;
    private String name;
    private int state;

    private SSHConnDetails connDetails;

    public TelldusActuator(int id, String name, int state, SSHConnDetails connDetails) {
        this.id = id;
        this.name = name;
        this.connDetails = connDetails;
        this.state = state;
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
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
        setSSHState(state);
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
        return ActuatorType.POWER_SWITCH;
    }


    public static class TelldusActuatorBatch implements Batch<TelldusActuator> {
        private SSHConnDetails connDetails;
        private ArrayList<TelldusActuator> batch = new ArrayList<>();

        public TelldusActuatorBatch(SSHConnDetails connDetails) {
            this.connDetails = connDetails;
            initBatch();
        }

        @Override
        public void updateBatch() {
            SparseIntArray statusById = new SparseIntArray();
            String list = SSHUtil.runCommand("tdtool -l", connDetails);
            String[] deviceLines = getDeviceLines(list);

            if (deviceLines == null)
                throw new IllegalStateException("SSH command 'tdtool -l' is faulty (is telldus installed?)");

            for (String devLine : deviceLines) {
                String[] cols = devLine.split("\t");
                int id = Integer.parseInt(cols[0]);
                int state = cols[2].equals("ON")? Integer.MAX_VALUE : 0;

                statusById.put(id, state);
            }

            for (TelldusActuator act : batch) {
                act.state = statusById.get(act.getID());
            }
        }

        @Override
        public ArrayList<TelldusActuator> getActuators() {
            return batch;
        }

        private void initBatch() {
            String list = SSHUtil.runCommand("tdtool -l", connDetails);
            String[] deviceLines = getDeviceLines(list);

            if (deviceLines == null)
                throw new IllegalStateException("SSH command 'tdtool -l' is faulty (is telldus installed?)");

            for (String devLine : deviceLines) {
                String[] cols = devLine.split("\t");
                int id = Integer.parseInt(cols[0]);
                String name = cols[1];
                int state = cols[2].equals("ON")? Integer.MAX_VALUE : 0;

                batch.add(new TelldusActuator(id, name, state, connDetails));
            }
        }

        private String[] getDeviceLines(String input) {
            String[] lines = input.split("\\n");

            if (!input.isEmpty() && lines[0].contains("Number of devices")) {
                String[] line0 = lines[0].split(" ");
                int size = Integer.valueOf(line0[line0.length - 1]);

                String[] deviceLines = new String[size];
                System.arraycopy(lines, 1, deviceLines, 0, size);
                return deviceLines;
            }
            return null;
        }
    }
}
