package se.su.dsv.mastermcvoiceit.remote.sensor;

/**
 * Created by felix on 2017-11-24.
 */

public class TelldusSensor implements Sensor{
    private int id;
    private String name;

    public TelldusSensor(int id, String sensorName) {
        this.id = id;
        this.name = sensorName;
    }

    // TODO: actually read from a sensor! (or simulate sensor)
    @Override
    public float getSensorValue() {
        return (float) Math.PI;
    }

    @Override
    public Type getType() {
        return Type.temperature;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return id;
    }

}
