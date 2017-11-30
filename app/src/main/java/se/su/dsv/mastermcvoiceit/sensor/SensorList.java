package se.su.dsv.mastermcvoiceit.sensor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by felix on 2017-11-30.
 */

public class SensorList {
    private HashMap<Integer, Sensor> sensorById = new HashMap<>();
    private HashMap<SensorType, ArrayList<Sensor>> sensorByType = new HashMap<>();

    public void add(Sensor sensor) {
        if (sensorById.put(sensor.getID(), sensor) != null)
            throw new RuntimeException("there was at least 2 sensors with same ID, not allowed");

        SensorType sensorType = sensor.getType();
        ArrayList<Sensor> sameTypeList = sensorByType.get(sensorType);
        if (sameTypeList == null) {
            sameTypeList = new ArrayList<>();
            sensorByType.put(sensorType, sameTypeList);
        }
        sameTypeList.add(sensor);
    }

    public void remove(Sensor sensor) {
        sensorById.remove(sensor.getID());

        ArrayList<Sensor> sameTypeList = sensorByType.get(sensor.getType());
        sameTypeList.remove(sensor);
    }

    public Sensor get(int id) {
        return sensorById.get(id);
    }

    public ArrayList<Sensor> get(SensorType type) {
        return sensorByType.get(type);
    }
}
