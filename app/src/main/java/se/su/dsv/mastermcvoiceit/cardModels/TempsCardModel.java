package se.su.dsv.mastermcvoiceit.cardModels;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.sensor.Sensor;

/**
 * Created by felix on 2017-11-28.
 */

public class TempsCardModel extends CardModel {
    private float[] temperatures;
    private String[] sensorNames;

    private ArrayList<Sensor> tempSensors;

    public TempsCardModel(ArrayList<Sensor> tempSensors) {
        super(CardModelType.TEMPERATURES);
        this.tempSensors = tempSensors;
    }

    public void fetchSensorReadings() {
        temperatures = new float[tempSensors.size()];
        sensorNames = new String[tempSensors.size()];

        for (int i = 0; i < tempSensors.size(); i++) {
            temperatures[i] = tempSensors.get(i).fetchSensorValue();
            sensorNames[i] = tempSensors.get(i).getName();
        }
    }

    public String[] getSensorNames() {
        return sensorNames;
    }

    public float[] getSensorValues() {
        return temperatures;
    }
}
