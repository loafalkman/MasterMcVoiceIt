package se.su.dsv.mastermcvoiceit.remote.sensor;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by felix on 2017-11-24.
 */

public class TelldusSensor implements Sensor{
    private int id;
    private String name;
    private SensorType type;
    private SSHConnDetails connDetails;

    public TelldusSensor(int id, String name, SensorType type, SSHConnDetails connDetails) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.connDetails = connDetails;
    }

    public float fetchSensorValue() {
        String list = SSHUtil.runCommand("tdtool -l", connDetails);
        String result = chopString(list);

        if (result != null)
            return Float.valueOf(result);
        else
            return Float.MIN_VALUE;
    }

    private String chopString(String input) {
        String[] lines = input.split("\\n");

        for (String line : lines) {
            if (line.contains("135")) {
                String result = line.split("\\t")[3];
                
                return result.substring(0, result.length() - 1);
            }
        }
        return null;
    }

    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SensorType getType() {
        return type;
    }

}
