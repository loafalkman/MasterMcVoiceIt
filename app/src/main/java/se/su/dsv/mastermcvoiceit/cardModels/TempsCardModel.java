package se.su.dsv.mastermcvoiceit.cardModels;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import se.su.dsv.mastermcvoiceit.sensor.Sensor;

/**
 * Created by felix on 2017-11-28.
 */

public class TempsCardModel extends CardModel {
    private float[] temperatures;
    private String[] sensorNames;

    private ArrayList<Sensor> tempSensors;
    private String answer;

    public TempsCardModel(ArrayList<Sensor> tempSensors) {
        super(CardModelType.TEMPERATURES);
        this.tempSensors = tempSensors;
    }

    public void fetchSensorReadings() {
        temperatures = new float[tempSensors.size()];
        sensorNames = new String[tempSensors.size()];

        answer = ""; // TODO: temporary
        for (int i = 0; i < tempSensors.size(); i++) {
            temperatures[i] = tempSensors.get(i).fetchSensorValue();
            sensorNames[i] = tempSensors.get(i).getName();

            answer += sensorNames[i] + " : " + temperatures[i] + '\n'; // TODO: also temporary
        }
    }

    // TODO: implement properly
    public String getTemperaturesAsString() { return answer; }

}
