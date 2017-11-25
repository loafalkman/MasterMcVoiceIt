package se.su.dsv.mastermcvoiceit.sensor;

/**
 * Created by felix on 2017-11-24.
 */

public class TelldusSensor {
    private int id;

    public TelldusSensor(int id) {
        this.id = id;
    }

    // TODO: actually read from a sensor! (or simulate sensor)
    public float getSensorValue() {
        return (float) Math.PI;
    }

    public int getID() {
        return id;
    }

}
