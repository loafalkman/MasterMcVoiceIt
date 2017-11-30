package se.su.dsv.mastermcvoiceit.remote.sensor;

/**
 * Created by felix on 2017-11-24.
 */

public class TelldusSensor implements Sensor{
    private int id;
    private String name;
    private SensorType type;

    public TelldusSensor(int id, String name, SensorType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // TODO: actually read from a sensor! (or simulate sensor)
    public float fetchSensorValue() {
        return (float) Math.random();
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
