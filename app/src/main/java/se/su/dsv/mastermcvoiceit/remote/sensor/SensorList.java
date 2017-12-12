package se.su.dsv.mastermcvoiceit.remote.sensor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by felix on 2017-11-30.
 */

public class SensorList {
    private HashMap<Integer, Sensor> sensorById = new HashMap<>();
    private HashMap<SensorType, ArrayList<Sensor>> sensorByType = new HashMap<>();
    private ArrayList<Sensor.Batch> batches = new ArrayList<>();

    public void add(Sensor sensor) {
        if (sensorById.put(sensor.getID(), sensor) != null)
            throw new RuntimeException("there was at least 2 sensors with same ID, not allowed");

        SensorType[] supportedTypes = sensor.getSupportedTypes();
        for (SensorType sensorType : supportedTypes) {
            ArrayList<Sensor> sameTypeList = sensorByType.get(sensorType);
            if (sameTypeList == null) {
                sameTypeList = new ArrayList<>();
                sensorByType.put(sensorType, sameTypeList);
            }
            sameTypeList.add(sensor);
        }
    }

    public void remove(Sensor sensor) {
        sensorById.remove(sensor.getID());

        SensorType[] supportedTypes = sensor.getSupportedTypes();
        for (SensorType sensorType : supportedTypes) {
            ArrayList<Sensor> sameTypeList = sensorByType.get(sensorType);
            sameTypeList.remove(sensor);
        }
    }

    public Sensor get(int id) {
        return sensorById.get(id);
    }

    public ArrayList<Sensor> get(SensorType type) {
        return sensorByType.get(type);
    }

    public <X extends Sensor> void addBatch(Sensor.Batch<X> batch) {
        batches.add(batch);
        for (Sensor act : batch.getSensors()) {
            add(act);
        }
    }

    public void updateBatches() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Sensor.Batch batch : batches) {
                    batch.updateBatch();
                }
            }
        }).start();
    }
}
